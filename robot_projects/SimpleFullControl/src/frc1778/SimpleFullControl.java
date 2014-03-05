// SimpleFullControl.java

package frc1778;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SimpleFullControl extends SimpleRobot {
    
    private final int AUTOSTATE_IDLE = 0;
    private final int AUTOSTATE_DRIVE_GYRO = 1;
    private final int AUTOSTATE_DRIVE_CAMERA = 2;
    private final int AUTOSTATE_SHOOT = 3;
    
    // PID coefficients for gate jaguar
    private final double PID_GATE[] = { 0.5, 0.45, 0.6 };
    
    // potentiometer values for gate position
    private final double GATE_CLOSED = 0.8;
    private final double GATE_OPEN = 0.57;
    
    // how close to the goal before shooting ball
    private final double AUTO_RANGE_MM = 200;
    
    // where do we read the position switch from
    private final int POSITION_SWITCH_SLOT = 3;
    
    // drive motors
    private CANJaguar mFrontLeft;
    private CANJaguar mFrontRight;
    private CANJaguar mBackLeft;
    private CANJaguar mBackRight;
    private RobotDrive drive;
    
    // gate and roller motors
    private CANJaguar gate;
    private CANJaguar rollers;   
    
    // drive control
    private Joystick leftStick;
    private Joystick rightStick;
    private Gyro gyro;
    //private double acceleration;
    //private ADXL345_SPI accel;
    
    // gate and roller control
    private Joystick gamepad;
    
    // sensors
    private Camera1778 camera;
    private Ultrasonic1778 ultrasonic;
    private DigitalInput positionSwitch;
    private double rangeMM = 0;
    private boolean isRobotLeft = true;
    
    public SimpleFullControl() throws CANTimeoutException {  
        
        // sensors
        camera = new Camera1778();
        ultrasonic = new Ultrasonic1778();
        
        // read switch and set robot position
        positionSwitch = new DigitalInput(POSITION_SWITCH_SLOT);
       
        // drive system
        getWatchdog().setEnabled(false);
        mFrontLeft = new CANJaguar(2);
        mBackLeft = new CANJaguar(1);
        mFrontRight = new CANJaguar(8);
        mBackRight = new CANJaguar(5);
        gyro = new Gyro(1);
        //accel = new ADXL345_SPI(1, 1, 2, 3, 4, ADXL345_SPI.DataFormat_Range.k2G);

        // set up gate motor
        gate = new CANJaguar(6);
        
        // do not uncomment these lines unless you are using PID!!
        //gate.changeControlMode(CANJaguar.ControlMode.kPosition);
        //gate.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
        //gate.setPID(PID_GATE[0], PID_GATE[1], PID_GATE[2]);

        // set up roller motor
        rollers = new CANJaguar(4);
        rollers.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        
        // drive control
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);

        // gate & roller control
        gamepad = new Joystick(3);
        
        drive = new RobotDrive(mBackLeft, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        getWatchdog().setEnabled(false);

        // CALIBRATION ROBOT 1:
        // autoSpeed constant = 0.5 ====> ~4 ft/sec
        // autoSpeed constant = 0.375 ====> ~3 ft/sec (best choice for stability/speed)
        // autoSpeed constant = 0.25  ====> ~2 ft/sec
        
        // autoSpeed of 0.375 should cross 18 ft (to goal) in 6 seconds

        final double AUTOSPEED_DEFAULT = 0.375;
        double autoSpeed;
        double startTime = Timer.getFPGATimestamp();
        double totalTime;
        int autoState = AUTOSTATE_DRIVE_GYRO;
        
        autoSpeed = SmartDashboard.getNumber("AutoSpeed",AUTOSPEED_DEFAULT);
        
        // tell camera if we are right or left (read from digital switch)
        isRobotLeft = positionSwitch.get();
        SmartDashboard.putBoolean("IsRobotLeft", isRobotLeft);      
        System.out.println("isRobotLeft = " + isRobotLeft);          
        camera.setLeft(isRobotLeft);
       
        // reset gyro to initial robot position
        gyro.reset();

        // enable control for gate and rollers
        try {    
            gate.enableControl();
            rollers.enableControl();
        } 
        catch (CANTimeoutException ex) {
            System.out.println ("CANTimeoutException happened :(");
        }

        while(isAutonomous()) {
            
            // check the total time elapsed
            totalTime = Timer.getFPGATimestamp() - startTime;
            
            // autonomous state machine
            switch (autoState)
            {
                case AUTOSTATE_DRIVE_GYRO:
                    autoState = driveStateGyro(autoSpeed,totalTime);
                    break;
                case AUTOSTATE_DRIVE_CAMERA:
                    autoState = driveStateCamera(autoSpeed,totalTime);
                    break;
                case AUTOSTATE_SHOOT:
                    autoState = shootState(totalTime);
                    break;
                case AUTOSTATE_IDLE:
                default:
                    autoState = idleState();
                    break;
            }   
            // time slice
            //Timer.delay(timeSliceSec);    
        }
        
    }

    // auto state methods - NO WHILE LOOPS IN THESE METHODS
    // they need to enter and exit in one pass
    // only one while loop, and that is in autonomous()
        
    private int driveStateGyro(double autoSpeed, double travelTime)
    {
        final double TRAVEL_TIME_DEFAULT = 2.8; // absolute time marker for 9 ft
        //final double TRAVEL_TIME_DEFAULT = 5.6;  // absolute time marker for 18 ft
        double travelTimeSec;  
        int state = AUTOSTATE_DRIVE_GYRO;
        double goalRange = 1000;
        boolean hasVisionTarget;

        // read in desired drive time from smart dashboard
        travelTimeSec = SmartDashboard.getNumber("TravelTimeSec",TRAVEL_TIME_DEFAULT);

        // report out current timer value
        SmartDashboard.putNumber("GyroDriveTime", travelTime);      
        System.out.println("Auto state is drive_gyro: timer = " + travelTime);          

        // get range to target
        goalRange = ultrasonic.getRangeMM();
        SmartDashboard.putNumber("DistanceMM", goalRange);      
        //System.out.println("DistanceMM = " + goalRange);          
        
        // do we have a vision target?
        hasVisionTarget = camera.hasTarget();

        // continue driving until either
        // a) vision target is detected or
        // b) we are at the goal or
        // c) drive time exceeded
        if (hasVisionTarget == false && 
            (goalRange > AUTO_RANGE_MM) && 
            (travelTime < travelTimeSec))
            driveStraight(autoSpeed);
        else if (goalRange <= AUTO_RANGE_MM)
            // at the target!  raise gate
            state = AUTOSTATE_SHOOT;
        else if (travelTime >= travelTimeSec)
            // times up - stop
            state = AUTOSTATE_IDLE;
        else
            // target acquired - switch to camera guidance
            state = AUTOSTATE_DRIVE_CAMERA;
            
        return state;
    }
    
    private int driveStateCamera(double autoSpeed, double travelTime)
    {
        final double TRAVEL_TIME_DEFAULT = 2.8; // absolute time marker for 9 ft
        //final double TRAVEL_TIME_DEFAULT = 5.6;  // absolute time marker for 18 ft
        double travelTimeSec;  
        int state = AUTOSTATE_DRIVE_CAMERA;
        double goalRange = 1000;
        boolean hasVisionTarget;
        
         // read in desired drive time from smart dashboard
        travelTimeSec = SmartDashboard.getNumber("TravelTimeSec",TRAVEL_TIME_DEFAULT);

        // report out current timer value
        SmartDashboard.putNumber("CameraDriveTime", travelTime);      
        System.out.println("Auto state is drive_camera: timer = " + travelTime);          

        // get range to target
        goalRange = ultrasonic.getRangeMM();
        SmartDashboard.putNumber("DistanceMM", goalRange);      
        //System.out.println("DistanceMM = " + goalRange);          
        
        // do we have a camera target?
        hasVisionTarget = camera.hasTarget();
        
        // continue driving until 
        // a) we lose the vision target or
        // b) we are at the goal or 
        // c) drive time exceeded
        if (hasVisionTarget == true && 
            (goalRange > AUTO_RANGE_MM) &&
            (travelTime < travelTimeSec))
            driveTowardGoal(autoSpeed);
        else if (goalRange <= AUTO_RANGE_MM)
            // at the target!  raise gate
            state = AUTOSTATE_SHOOT;
        else if (travelTime >= travelTimeSec)
            // times up - stop
            state = AUTOSTATE_IDLE;
        else
            // no more vision target - switch back to gyro
            state = AUTOSTATE_DRIVE_GYRO;

        return state;
    }
    
    private int shootState(double shootTime)
    {
        final double SHOOT_TIME_DEFAULT = 10.0;
        double shootTimeSec;  // absolute time marker
        final double rollerStep = 0.35;
        final double gateOpen = 0.35;
        
        int state = AUTOSTATE_SHOOT;  
        
        // read in desired shoot time from smart dashboard
        shootTimeSec = SmartDashboard.getNumber("ShootTimeSec",SHOOT_TIME_DEFAULT);
        
        // report out current timer value
        SmartDashboard.putNumber("shootTime", shootTime);
        //System.out.println("Auto state is shoot: timer = " + shootTime);          
        
        try {    
            rollers.setX(rollerStep);
            gate.setX(gateOpen);
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        // shoot time exceeded
        if (shootTime >= shootTimeSec)
            // gate raised! shoot the ball
            state = AUTOSTATE_IDLE;
        
        return state;
    }
       
    private int idleState()
    {
        int state = AUTOSTATE_IDLE;
             
        //System.out.println("Auto state is idle");          
       
        // stop drive if still going
        driveStraight(0);
        
        // turn off gate and rollers
        try {
            rollers.setX(0);
            gate.setX(0);
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        // end state - do not transition out of this state
        
        return state;
    }
        
    private boolean inRangeMM(double rangeThresholdMM)
    {
        // if closer than the range threshold, return false;
        //if (ultrasonic.getRangeMM() > rangeThresholdMM)
        //    return false;
        
        // otherwise return true;
        return true;        
    }
    
    private void driveStraight(double speed) {
           final double Kp = 0.03;
           double angle = 0;
           //acceleration = accel.getAcceleration(ADXL345_SPI.Axes.kY);
            
            angle = gyro.getAngle();
            drive.drive(-1*speed, -angle*Kp);
            //System.out.println("angle is: " + angle + ", acceleration is: " + acceleration);          
    }
    
    private void driveTowardGoal(double speed) {
    
            // TODO: use vision target to guide robot to goal
    }
    
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        getWatchdog().setEnabled(false);
        int mode = 0;
        
        // enable control for gate & rollers
        double lock_pos = 0.275;
        double step = 0.7;
        double increment = 0.0;
        double pot_pos = 0;
        try {    
            gate.enableControl();
            rollers.enableControl();
        } 
        catch (CANTimeoutException ex) {
            System.out.println ("CANTimeoutException :(");
        }
        
        gyro.reset();
        while(isEnabled() && isOperatorControl()) {
            //************* drive control section
            if(leftStick.getButton(Joystick.ButtonType.kTrigger) == true) {
                mode = 1-mode;
                while(leftStick.getButton(Joystick.ButtonType.kTrigger) == true) {
                }
            }
            if(mode == 0) {
                drive.tankDrive(leftStick, rightStick);
            } else {
                drive.arcadeDrive(leftStick);
            }
            
            //*********** gate and roller control section
            increment = gamepad.getRawAxis(2)*step;
            lock_pos += increment;
            lock_pos = Math.max(Math.min(lock_pos, 0.4),0.1);
            
            //System.out.println("increment: " + increment + "  :  lock_pos: " + lock_pos);
            // gate motor operation
            try {
                pot_pos = gate.getPosition();
                //System.out.println("Pot pos = "+pot_pos);
                //gate.setX(lock_pos);     // only used for PID
                gate.setX(increment);
                rollers.setX(gamepad.getRawAxis(4));
            } catch(CANTimeoutException e) {
                e.printStackTrace();
            }
            if(mode == 0) {
                SmartDashboard.putString("Drive Mode", "Tank Drive");
            } else {
                SmartDashboard.putString("Drive Mode", "Arcade Drive");
            }
            SmartDashboard.putNumber("Direction", gyro.getAngle());
            //SmartDashboard.putNumber("rangeMM", ultrasonic.getRangeMM());
         }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}

package Systems;

import NetworkComm.InputOutputComm;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


//Chill Out 1778 class for controlling the drivetrain

public class CANDriveAssembly {
	
	private static boolean initialized = false;
	
	// Speed controller IDs
	private static final int LEFT_FRONT_TALON_ID = 8;
	//private static final int LEFT_REAR_TALON_ID = 4;
	private static final int RIGHT_FRONT_TALON_ID = 3;
	//private static final int RIGHT_REAR_TALON_ID = 7;
		
	// joystick axis ids
	private static final int JOY_X_AXIS = 0;
	private static final int JOY_Y_AXIS = 1;
	private static final int JOY_Z_AXIS = 2;
	private static final int JOY_SLIDER_AXIS = 3;
	
	// joystick device ids
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	
	//threshold constants
	private static final double LATERAL_DEADZONE = 0.3;
	private static final double DRIVE_DEADZONE = 0.2;
	private static final double EQUALIZATION_THRESHOLD = 0.1;
	
	private static final long AUTO_DRIVE_TIME_SEC = 4;
	private static final double AUTO_DRIVE_SPEED = -0.5;
	private static final double AUTO_DRIVE_CORRECT_COEFF = 0.02;
	
	//other constants
	private static final boolean USE_SQUARED_INPUTS = true;
	private static final double GYRO_CORRECT_COEFF = 0.03;
	
	// speed controllers and drive class
	private static CANTalon mFrontLeft, mBackLeft, mFrontRight, mBackRight;
    //private static RobotDrive drive;  // no longer used
    
    // drive control
    private static Joystick leftStick, rightStick;
	        
	// static initializer
	public static void initialize()
	{
		if (!initialized) {
	        mFrontLeft = new CANTalon(LEFT_FRONT_TALON_ID);
	        //mBackLeft = new CANTalon(LEFT_REAR_TALON_ID);
	        mFrontRight = new CANTalon(RIGHT_FRONT_TALON_ID);
	        //mBackRight = new CANTalon(RIGHT_REAR_TALON_ID);
	        	        
	        //drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
	        //drive = new RobotDrive(mFrontLeft, mFrontRight);
	        	        
	        //drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
	        //drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
	        //drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
	        //drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
	        	        
	        //leftStick = new Joystick(LEFT_JOYSTICK_ID);
	        //rightStick = new Joystick(RIGHT_JOYSTICK_ID);
	        
	        // initialize the NavXSensor
	        NavXSensor.initialize();
	        
	        initialized = true;
		}
	}


	public static void autoInit() {
		NavXSensor.reset();
   	}
	
	private static double getGyroAngle() {
		
		//double gyroAngle = 0.0;
		//double gyroAngle = NavXSensor.getYaw();	  // -180 deg to +180 deg
		double gyroAngle = NavXSensor.getAngle();     // continuous angle (can be larger than 360 deg)
		
		//System.out.println("autoPeriodicStraight:  Gyro angle = " + gyroAngle);
			
		// send output data for test & debug
	    InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Connected",NavXSensor.isConnected());
	    InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Calibrating",NavXSensor.isCalibrating());

		String gyroAngleStr = String.format("%.2f", gyroAngle);
	    String myString = new String("gyroAngle = " + gyroAngleStr);
		//System.out.println(myString);
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"Auto/GyroAngle", myString);		

		return gyroAngle;
	}
	
	
	public static void autoPeriodicStraight(double speed) {
		// autonomous operation of drive straight
		
		double gyroAngle = getGyroAngle();
		
		// calculate speed adjustment for left and right sides (negative sign added as feedback)
		double driveAngle = -gyroAngle * AUTO_DRIVE_CORRECT_COEFF;
				
		double leftSpeed = speed+driveAngle;		
		double rightSpeed = speed-driveAngle;
		
		String leftSpeedStr = String.format("%.2f", leftSpeed);
		String rightSpeedStr = String.format("%.2f", rightSpeed);
		String myString2 = new String("leftSpeed = " + leftSpeedStr + " rightSpeed = " + rightSpeedStr);
		//System.out.println(myString2);
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"Auto/autoPeriodicStraight", myString2);
		
		// adjust speed of left and right sides
		drive(leftSpeed, rightSpeed, 0.0);		 
	}

	public static void autoStop() {
		drive(0.0, 0.0, 0.0);
	}
		
	public static void teleopInit() {
		NavXSensor.reset();
	}
	
	public static void teleopPeriodic() {
		
		/********************** No longer used - reference cheesy drive code 
		// Left Stick Throttle Control
		double throttleVal = 1.0 - ((leftStick.getRawAxis(JOY_Z_AXIS))+1.0)/2.0;
		
		// invert sign to compensate for joystick polarity (forward is neg Y, backward is pos Y)
		double leftValue = -throttleVal*leftStick.getY();
		double rightValue = -throttleVal*rightStick.getY();
		
		// Deadzones
		if(Math.abs(leftValue) <= DRIVE_DEADZONE) {
			leftValue = 0;
		}
		if(Math.abs(rightValue) <= DRIVE_DEADZONE) {
			rightValue = 0;
		}
		
		// Throttle Equalization Compensation
		if(Math.abs(leftValue-rightValue) <= EQUALIZATION_THRESHOLD) {
			double avgValue = (leftValue+rightValue)/2;
			//System.out.println("EQUALIZING: "+avgValue);
			leftValue = avgValue;
			rightValue = avgValue;
		}
		
		// Slowmode
		if(leftStick.getRawButton(1)) {
			leftValue /= 1.5;
			rightValue /= 1.5;
		}
				
		//Set the drive train 
		drive(leftValue, rightValue, 0);
		*********************/
	}
	
	// CORE DRIVE METHOD
	// Assumes parameters are PercentVbus (0.0 to 1.0)
	public static void drive(double leftValue, double rightValue, double strafe) {
		
		double rightMotorPolarity = -1.0;  // right motor is inverted 
		double leftMotorPolarity = 1.0;    // left motor is not inverted
				
		// set motor values directly
		mFrontLeft.set(leftMotorPolarity*leftValue);
		mFrontRight.set(rightMotorPolarity*rightValue);
	}
	
	public static void driveDirection(double angle, double speed) {
		double gyroAngle = getGyroAngle();	
		double driveAngle = (angle-gyroAngle)*GYRO_CORRECT_COEFF;
		drive(driveAngle+speed, -driveAngle+speed, 0);
	}
	
	public static void turnToDirection(double angle, double power) {
		double gyroAngle = getGyroAngle();
		double driveAngle = (angle-gyroAngle)*(1/360)*power;
		drive(driveAngle, -driveAngle, 0);
	}
	
	public static void driveForward(double forwardVel) {
		drive(forwardVel, forwardVel, 0);
	}
	
	public static void rotate(double angularVel) {
		drive(angularVel, -angularVel, 0);
	}
	
	public static void driveVelocity(double forwardVel, double angularVel) {
		drive((forwardVel+angularVel)/2.0,(forwardVel-angularVel)/2.0,0);
	}
		
	//Turn methods
	//===================================================
	public static void rotateLeft(double speed) {		
		drive(-speed, speed, 0);
	}

	public static void rotateRight(double speed) {
		drive(speed, -speed, 0);
	}
}


package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
	
    // minimum increment (for joystick dead zone)
	private final double ARM_DEADZONE = 0.1;
	
    // limits
    // forward arm gear is 208:1 - for quarter turn of arm, about 50 motor revs
    private static final double SOFT_ENCODER_LIMIT_MAX = 0.0;  // high limit of arm -  vertical (matches need to start with arm in this position)
    private static final double ENCODER_POS_HIGH = -(4096.0*5.0);     // arm high
    private static final double ENCODER_POS_MIDDLE = -(4096.0*10.0);  // arm partially down
    private static final double ENCODER_POS_LOW = -(4096.0*25.0);     // arm low
    private static final double SOFT_ENCODER_LIMIT_FLOOR = -(4096.0*35.0);  // low limit of arm (floor)
    private final double ARM_SPEED_MULTIPLIER = 400.0;
    //private static final double ARM_SPEED_MULTIPLIER = 1024.0;

    private final double ARM_MULTIPLIER = -0.5;
    private static final double ARM_POS_MULTIPLIER = -4096.0;

	private static final int FRONT_ARM_GROUND_CAL_BUTTON1 = 1;
	private static final int FRONT_ARM_GROUND_CAL_BUTTON2 = 3;

	// controller gamepad ID - assumes no other controllers connected
	private final int GAMEPAD_ID = 2;
	
    // control objects
    private Joystick gamepad;
           
    // motor ids
    private final int TEST_MOTOR_ID = 5;
    
    private static CANTalon testMotor;
    
   
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);

        initialize();
    }
    
    private void initialize()
    {    	
        gamepad = new Joystick(GAMEPAD_ID);
                
        // create and initialize arm motor
        testMotor = new CANTalon(TEST_MOTOR_ID);
        if (testMotor != null) {
        	
	        System.out.println("Initializing test motor (position control)...");

	        System.out.println("Initializing front arm motor (position control)...");

	        //frontArmMotor.clearStickyFaults();
	        
        	// set up motor for position control (PID) mode
	        testMotor.enableControl();        // enables PID control
	        testMotor.changeControlMode(CANTalon.TalonControlMode.Position);
	        testMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	        //testMotor.clearIAccum();   // clear error in PID control
	        testMotor.reverseOutput(true);  // reverse output needed - used for closed loops modes only
	        testMotor.reverseSensor(false);  // encoder does not need to be reversed
	        //testMotor.setPID(0.1, 0, 0.0);   // first test - works, but a little wobbly (low gain)
	        testMotor.setPID(0.5, 0, 0.0);		// 5x gain   
	        //testMotor.setPID(1.0, 0, 0.0);   // 10x gain
	        
	        //testMotor.setForwardSoftLimit(SOFT_ENCODER_LIMIT_MAX);
	        //testMotor.setReverseSoftLimit(SOFT_ENCODER_LIMIT_FLOOR);

	        //testMotor.set(testMotor.getPosition());   // set motor to current position
	        testMotor.setPosition(0);	      // initializes encoder to current position as zero	        	
	        testMotor.enableBrakeMode(true);  
	        
	        // set up brake mode
	        testMotor.enableBrakeMode(true);
	        
	        // PercentVbus test ONLY!!!
	        //testMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);	 
	        
	        // set and enable soft motor limits  
	        /*
        	testMotor.setForwardSoftLimit(SOFT_ENCODER_LIMIT_1);    	
        	testMotor.enableForwardSoftLimit(true);
        	testMotor.setReverseSoftLimit(SOFT_ENCODER_LIMIT_2);
        	testMotor.enableReverseSoftLimit(true);	
        	
        	
	        // reset position
	        //testMotor.set(testMotor.getPosition());
	        
        	// initializes encoder to zero 		        
	        //testMotor.setPosition(0);    	
	         */
        }
        else
        	System.out.println("ERROR: Test motor not initialized!");
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    public void teleopInit() {
    	System.out.println("teleopInit: calling positionMode");
    	positionMode();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

		// both buttons pressed simultaneously, time to cal to ground
		if (gamepad.getRawButton(FRONT_ARM_GROUND_CAL_BUTTON1) && gamepad.getRawButton(FRONT_ARM_GROUND_CAL_BUTTON2)) {
			processGroundCal();
		}

		// PID CONTROL ONLY
		double armDeltaPos = gamepad.getRawAxis(1);
		if (Math.abs(armDeltaPos) < ARM_DEADZONE) {
			armDeltaPos = 0.0f;
		}
		else
		{
			armDeltaPos *= ARM_POS_MULTIPLIER;
			double currPos = testMotor.getPosition();
			
			if (((currPos > SOFT_ENCODER_LIMIT_MAX) && armDeltaPos > 0.0) || ((currPos < SOFT_ENCODER_LIMIT_FLOOR) && armDeltaPos < 0.0)) {
				System.out.println("SOFT ARM LIMIT HIT! Setting armDeltaPos to zero");
				armDeltaPos = 0.0;
			}
			
			
			double newPos =  currPos + armDeltaPos;
			testMotor.set(newPos);
			System.out.println("Setting new front arm pos = " + newPos);	
		}		

    	/*
		double newArmPos = gamepad.getRawAxis(1);
		if(Math.abs(newArmPos) <= ARM_DEADZONE) {
			newArmPos = 0.0;
		}
		double newMotorPos = (newArmPos * ARM_SPEED_MULTIPLIER) + testMotor.getPosition();
		positionMoveByCount(newMotorPos);
		System.out.println("input = " + newArmPos + " target pos = " + newMotorPos + " enc pos = " + testMotor.getPosition());
    	*/
    	
		// PercentVbus test ONLY!!
    	/*
		double armSpeed = gamepad.getRawAxis(1);
		if (Math.abs(armSpeed) < ARM_DEADZONE) {
			armSpeed = 0.0f;
		}	
		armSpeed *= ARM_MULTIPLIER;
		
		double pos= testMotor.getPosition();
		if (((pos > SOFT_ENCODER_LIMIT_1) && armSpeed < 0.0) || ((pos < SOFT_ENCODER_LIMIT_2) && armSpeed > 0.0))
			armSpeed = 0.0;
		testMotor.set(armSpeed);
		
		System.out.println("armSpeed = " + armSpeed + " enc pos = " + testMotor.getPosition());
		 */      
    }
 
	// if we are on the ground, reinitialize encoder to this value
	// this may be needed if arm position gets skewed
	// NOTE:  ASSUMES ARM IS PHYSICALLY ON FLOOR
	private static void processGroundCal()
	{
		if (testMotor != null) {
			System.out.println("Calibrating arm to floor position");
			testMotor.setPosition(SOFT_ENCODER_LIMIT_FLOOR);
		}
	}		

	// Change to closed loop control mode and hold the current position
	private void positionMode() {
		testMotor.setProfile(0);
		testMotor.ClearIaccum();
		testMotor.set(testMotor.getPosition());
		testMotor.ClearIaccum();
	}

	// Change to closed loop control mode and move "count" ticks 
	private void positionMoveByCount(double count) {
		testMotor.setProfile(0);
		testMotor.ClearIaccum();
		testMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		testMotor.set((testMotor.getPosition()+count));
		testMotor.ClearIaccum();
	}

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

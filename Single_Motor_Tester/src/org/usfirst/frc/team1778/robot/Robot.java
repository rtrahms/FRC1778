
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
    private final double SOFT_ENCODER_LIMIT_1 = (4096.0*40.0);
    private final double SOFT_ENCODER_LIMIT_2 = 0.0;
    private final double ARM_SPEED_MULTIPLIER = 400.0;
    //private static final double ARM_SPEED_MULTIPLIER = 1024.0;

    private final double ARM_MULTIPLIER = -0.5;
    
	// controller gamepad ID - assumes no other controllers connected
	private final int GAMEPAD_ID = 2;
	
    // control objects
    private Joystick gamepad;
           
    // motor ids
    private final int TEST_MOTOR_ID = 11;
    
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
    	double p;
    	double i;
    	double d;
    	double f;
    	int izone;
    	double ramprate;  // this should leave the ramp rate uncapped.
    	int profile;
    	
        gamepad = new Joystick(GAMEPAD_ID);
                
        // create and initialize arm motor
        testMotor = new CANTalon(TEST_MOTOR_ID);
        if (testMotor != null) {
        	
	        System.out.println("Initializing test motor (position control)...");

	        // VERY IMPORTANT - resets talon faults to render them usable again!!
	        testMotor.clearStickyFaults();
	        //testMotor.setInverted(true);
        		       
	        /*
	        testMotor.enableControl();
			testMotor.changeControlMode(CANTalon.TalonControlMode.Position);
	        testMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	        // set up motor for position control PID feedback mode only
	        p = 3.0;  i = 0.0;  d = 0.0; f = 60.0;
	        izone = 100;  ramprate = 120;  profile = 0;	        
	        testMotor.setPID(p, i, d, f, izone, ramprate, profile);	 
	        */
	        
	        // set up brake mode
	        testMotor.enableBrakeMode(true);
	        
	        // PercentVbus test ONLY!!!
	        testMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);	 
	        
	        // set and enable soft motor limits  
	        /*
        	testMotor.setForwardSoftLimit(SOFT_ENCODER_LIMIT_1);    	
        	testMotor.enableForwardSoftLimit(true);
        	testMotor.setReverseSoftLimit(SOFT_ENCODER_LIMIT_2);
        	testMotor.enableReverseSoftLimit(true);	
        	*/ 
        	
	        // reset position
	        //testMotor.set(testMotor.getPosition());
	        
        	// initializes encoder to zero 		        
	        testMotor.setPosition(0);    	
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

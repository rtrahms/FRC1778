package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class FrontArmAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
	private static final double ARM_DEADZONE = 0.1;
	
	private static final double CONTROL_CYCLE_US = 250000;
	
    // limits
    // forward arm gear is 208:1 - for quarter turn of arm, about 50 motor revs
    private static final double SOFT_ENCODER_LIMIT_MAX = 0.0;  // high limit of arm -  vertical (matches need to start with arm in this position)
    private static final double ENCODER_POS_HIGH = -(4096.0*10.0);     // arm high
    private static final double ENCODER_POS_MIDDLE = -(4096.0*17.0);  // arm partially down
    private static final double ENCODER_POS_LOW = -(4096.0*25.0);     // arm low
    private static final double ENCODER_POS_VERY_LOW = -(4096.0*30.0);     // arm very low (but above floor)
    private static final double SOFT_ENCODER_LIMIT_FLOOR = -(4096.0*35.0);  // low limit of arm (floor)

    private static final int ARM_HARD_LIMIT_CHANNEL = 5;  // hard limit switch on arm - normally closed (0), will open (1) on contact
    
    // speeds
    private static final double ARM_ROLLER_SPEED = 0.5;
    private static final double CONVEYER_SPEED = 0.75;
    private static final double ARM_POS_MULTIPLIER = -4096.0;
    
    private static final double AUTO_CONVEYER_RUN_US = 2000000;  // auto conveyer run in microsec
    private static boolean isAutoConveyerDone;
    
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
	private static final int ARM_AXIS = 1;
	
	private static final int ROLLER_IN_BUTTON = 5;
	//private static final int ROLLER_OUT_BUTTON = 7;
	private static final int ROLLER_OUT_AXIS = 2;
	
	private static final int CONVEYER_IN_BUTTON = 6;
	//private static final int CONVEYER_OUT_BUTTON = 8;
	private static final int CONVEYER_OUT_AXIS = 3;
	private static final int CONVEYER_DEPOSIT_BUTTON = 2;
	
	private static final int FRONT_ARM_GROUND_CAL_BUTTON1 = 1;
	private static final int FRONT_ARM_GROUND_CAL_BUTTON2 = 3;
	
    // control objects
    private static Joystick gamepad;
           
    // motor ids
    private static final int FRONT_ARM_MOTOR_ID = 11;
    private static final int FRONT_ARM_ROLLER_ID = 12;
    private static final int CONVEYER_MOTOR_ID = 13;
    
    private static CANTalon frontArmMotor, frontArmRollerMotor, conveyerMotor;
    
    private static DigitalInput frontArmLimitSwitch;
    
    private static boolean hardLimit;
    
	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        
	        frontArmLimitSwitch = new DigitalInput(ARM_HARD_LIMIT_CHANNEL);
	        hardLimit = false;
	        
	        //************ create and initialize arm motor
	        frontArmMotor = new CANTalon(FRONT_ARM_MOTOR_ID);
	        if (frontArmMotor != null) {
	        	
		        System.out.println("Initializing front arm motor (position control)...");

		        //frontArmMotor.clearStickyFaults();
		        
	        	// set up motor for position control (PID) mode
		        frontArmMotor.enableControl();        // enables PID control
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        //frontArmMotor.clearIAccum();   // clear error in PID control
		        frontArmMotor.reverseOutput(true);  // reverse output needed - used for closed loops modes only
		        frontArmMotor.reverseSensor(false);  // encoder does not need to be reversed
		        //frontArmMotor.setPID(0.1, 0, 0.0);   // first test - works, but a little wobbly (low gain)
		        //frontArmMotor.setPID(0.5, 0, 0.0);		// 5x gain   
		        frontArmMotor.setPID(0.75, 0, 0.0);   // 7.5x gain

		        //frontArmMotor.setForwardSoftLimit(SOFT_ENCODER_LIMIT_MAX);
		        //frontArmMotor.setReverseSoftLimit(SOFT_ENCODER_LIMIT_FLOOR);

		        //frontArmMotor.set(catapultMotor.getPosition());   // set motor to current position
		        frontArmMotor.setPosition(0);	      // initializes encoder to current position as zero	        	
		        frontArmMotor.enableBrakeMode(true);  
		        
		        /*
		        // VBUS MODE ONLY
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);	 
	        	frontArmMotor.enableBrakeMode(false);
		        frontArmMotor.setPosition(0);    // initializes encoder to zero 		
		        */

				// make sure we're in fast accel mode
				armFastAccel();

	        }
	        else
	        	System.out.println("ERROR: Front Arm motor not initialized!");
		  
	        //*********** create and initialize roller motor
	        frontArmRollerMotor = new CANTalon(FRONT_ARM_ROLLER_ID);
	        if (frontArmRollerMotor != null) {
	        	
		        System.out.println("Initializing front arm roller motor (PercentVbus control)...");
	        	
		        //frontArmRollerMotor.clearStickyFaults();
		        
	        	// set up roller motor for percent Vbus control mode
		        frontArmRollerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		        frontArmRollerMotor.enableBrakeMode(false);
	        	
	        	// initializes speed of rollers to zero
		        frontArmRollerMotor.set(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Front Arm roller motor not initialized!");

	        //********** create and initialize conveyer motor
	        conveyerMotor = new CANTalon(CONVEYER_MOTOR_ID);
	        if (conveyerMotor != null) {
	        	
		        System.out.println("Initializing conveyer motor (PercentVbus control)...");
	        	
		        //conveyerMotor.clearStickyFaults();
		        
	        	// set up conveyer motor for percent Vbus control mode
		        conveyerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		        conveyerMotor.enableBrakeMode(false);
	        	
	        	// initializes speed of conveyers to zero
		        conveyerMotor.set(0);
	        }
	        else
	        	System.out.println("ERROR: Conveyer motor not initialized!");
	        
		}
	}
			
	public static void autoInit() {
		
		// for auto control, arm motor ramp should be slow by default
		armSlowAccel();

		// check current arm position
		double armPos = frontArmMotor.getPosition();
				
		// if the arm is up (above middle)
		if (armPos > ENCODER_POS_MIDDLE) {
			// move arm to low position
			autoArmToMiddle();
		}
	}
	
	public static void teleopInit() {
		
		// for teleop control, arm motor ramp should be fast by default
		armFastAccel();
	}
	
	public static void teleopPeriodic()
	{			
		// both buttons pressed simultaneously, time to cal to ground
		if (gamepad.getRawButton(FRONT_ARM_GROUND_CAL_BUTTON1) && gamepad.getRawButton(FRONT_ARM_GROUND_CAL_BUTTON2)) {
			processGroundCal();
		}
			
		// PID CONTROL ONLY --- check for front arm control motion
		double armDeltaPos = gamepad.getRawAxis(ARM_AXIS);
		if (Math.abs(armDeltaPos) < ARM_DEADZONE) {
			armDeltaPos = 0.0f;
		}
		else
		{
			armDeltaPos *= ARM_POS_MULTIPLIER;
			double currPos = frontArmMotor.getPosition();
			
			if (((currPos > SOFT_ENCODER_LIMIT_MAX || hardLimit) && armDeltaPos > 0.0) || ((currPos < ENCODER_POS_VERY_LOW) && armDeltaPos < 0.0)) {
				System.out.println("SOFT or HARD ARM LIMIT HIT! Setting armDeltaPos to zero");
				armDeltaPos = 0.0;
			}
						
			double newPos =  currPos + armDeltaPos;
			frontArmMotor.set(newPos);
			//System.out.println("Setting new front arm pos = " + newPos);	
		}		
		
		// VBUS CONTROL ONLY --- check for front arm control motion
		/*
		double armSpeed = gamepad.getRawAxis(1);
		if (Math.abs(armSpeed) < ARM_DEADZONE) {
			armSpeed = 0.0f;
		}	
		armSpeed *= ARM_MULTIPLIER;
		double pos= frontArmMotor.getPosition();
		
		// soft limit check (based on encoder value)
		if (((pos > SOFT_ENCODER_LIMIT_MAX) && armSpeed < 0.0) || ((pos < SOFT_ENCODER_LIMIT_FLOOR) && armSpeed > 0.0))
		{
			System.out.println("SOFT ARM LIMIT HIT! Setting speed to zero");
			armSpeed = 0.0;
		}	
		frontArmMotor.set(armSpeed);
		*/

		// hard limit HIT when trying to go higher - DISABLE MOTOR
		if (frontArmLimitSwitch.get())
		{
			System.out.println("HARD ARM LIMIT HIT! Disabling motor");
			//frontArmMotor.disable();
			hardLimit = true;
		}
		else
			hardLimit = false;
		//System.out.println("Arm Limit Switch state = " + frontArmLimitSwitch.get());
		
		// check for roller motion
		double rollerSpeed = 0.0;
		if (gamepad.getRawButton(ROLLER_IN_BUTTON))
			rollerSpeed = -ARM_ROLLER_SPEED;
		//else if (gamepad.getRawButton(ROLLER_OUT_BUTTON))
		//	rollerSpeed = -ARM_ROLLER_SPEED;	
		else if (gamepad.getRawAxis(ROLLER_OUT_AXIS) > 0.1)
			rollerSpeed = ARM_ROLLER_SPEED;	
		frontArmRollerMotor.set(rollerSpeed);
					
		//System.out.println("Roller = " + rollerSpeed);
		
		// check for conveyer motion control
		double conveyerSpeed = 0.0;
		boolean ballDetected = UltrasonicSensor.isBallPresent();
		//if ((gamepad.getRawButton(CONVEYER_IN_BUTTON))||
		//	gamepad.getRawButton(CONVEYER_DEPOSIT_BUTTON))
		//if (((gamepad.getRawButton(CONVEYER_IN_BUTTON)) && !ballDetected) ||
		if ((gamepad.getRawButton(CONVEYER_IN_BUTTON)) ||
			gamepad.getRawButton(CONVEYER_DEPOSIT_BUTTON))
			conveyerSpeed = CONVEYER_SPEED;
		//else if (gamepad.getRawButton(CONVEYER_OUT_BUTTON))
		//	conveyerSpeed = -CONVEYER_SPEED;
		else if (gamepad.getRawAxis(CONVEYER_OUT_AXIS) > 0.1)
			conveyerSpeed = -CONVEYER_SPEED;
		conveyerMotor.set(conveyerSpeed);
		
		//System.out.println(" Conveyer = " + conveyerSpeed + " ballDetected = " + ballDetected);
		
	}
	
	public static void startConveyer(boolean inDirection) 
	{
		if (inDirection)
			conveyerMotor.set(CONVEYER_SPEED);		
		else
			conveyerMotor.set(-CONVEYER_SPEED);
	}
	
	public static void stopConveyer()
	{
		conveyerMotor.set(0);		
	}
			
	// query method to determine whether arm is low enough to shoot catapult
	// purpose:  to avoid robot damaging itself when arm is too far up
	public static boolean isArmLowEnoughForCatapult()  {
		
		if (frontArmMotor == null)
			return false;
		
		// if arm position in encoder ticks is less than low position, return true
		double armPos = frontArmMotor.getPosition();
		if (armPos < ENCODER_POS_MIDDLE)
			return true;
		else {
			System.out.println("WARNING: front arm is too high for catapult operation!!");
			return false;
		}
	}
	
	/****************  automatic arm movement methods *******************/
	public static void autoArmToVeryLow() {
		armSlowAccel();
		autoArmMove(ENCODER_POS_VERY_LOW);
	}

	public static void autoArmToLow() {
		armSlowAccel();
		autoArmMove(ENCODER_POS_LOW);
	}

	public static void autoArmToMiddle() {
		armSlowAccel();
		autoArmMove(ENCODER_POS_MIDDLE);
	}

	public static void autoArmToHigh() {
		armSlowAccel();
		autoArmMove(ENCODER_POS_HIGH);
	}
	
	private static void autoArmMove(double encoderPos) {
		// during auto movement, lower voltage ramp rate
		frontArmMotor.setPosition(encoderPos);
	}

	private static void armFastAccel() {
		frontArmMotor.setVoltageRampRate(96.0);   // 96 V/sec ramp - full ramp up to 12v in 0.125s	
	}
	
	private static void armSlowAccel() {
		frontArmMotor.setVoltageRampRate(12.0);   // 12 V/sec ramp - full ramp up to 12v in 1s
	}

	private static void armVerySlowAccel() {
		frontArmMotor.setVoltageRampRate(6.0);   // 6 V/sec ramp - full ramp up to 12v in 2s
	}

	
	// if we are on the ground, reinitialize encoder to this value
	// this may be needed if arm position gets skewed
	// NOTE:  ASSUMES ARM IS PHYSICALLY ON FLOOR
	private static void processGroundCal()
	{
		if (frontArmMotor != null) {
			System.out.println("Calibrating arm to floor position");
			frontArmMotor.setPosition(SOFT_ENCODER_LIMIT_FLOOR);
		}
	}		

}

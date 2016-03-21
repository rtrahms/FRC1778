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
    // for quarter turn of arm, about 80 motor revs (est)
    private static final double SOFT_ENCODER_LIMIT_MAX = 0.0;  // high limit of arm -  vertical (matches need to start with arm in this position)
    private static final double ENCODER_POS_HIGH = -(4096.0*20.0);     // arm high
    private static final double ENCODER_POS_MIDDLE = -(4096.0*35.0);  // arm partially down
    private static final double ENCODER_POS_LOW = -(4096.0*50.0);     // arm low
    private static final double ENCODER_POS_VERY_LOW = -(4096.0*60.0);     // arm very low (but above floor)
    private static final double SOFT_ENCODER_LIMIT_FLOOR = -(4096.0*80.0);  // low limit of arm (floor)

    private static final int ARM_HARD_LIMIT_CHANNEL = 5;  // hard limit switch on arm - normally closed (0), will open (1) on contact
    
    // speeds
    private static final double ARM_ROLLER_SPEED = 0.5;
    private static final double CONVEYER_SPEED = 0.75;
    private static final double ARM_POS_MULTIPLIER = -4096.0;
    private static final double ARM_VBUS_MULTIPLIER = 0.6;
    
    private static final double AUTO_CONVEYER_RUN_US = 2000000;  // auto conveyer run in microsec
    private static boolean isAutoConveyerDone;
    
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
	private static final int ARM_AXIS = 1;
	
	private static final int ROLLER_IN_BUTTON = 5;
	private static final int ROLLER_OUT_AXIS = 2;
	
	private static final int CONVEYER_IN_BUTTON = 6;
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
                
	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        	        	        
	        //************ create and initialize arm motor
	        frontArmMotor = new CANTalon(FRONT_ARM_MOTOR_ID);
	        if (frontArmMotor != null) {
	        	
	        	/*
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
		        */
		        
		        // VBUS MODE ONLY
		        System.out.println("Initializing front arm motor (PercentVbus control)...");
		        
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);	 
	        	frontArmMotor.enableBrakeMode(true);
		        frontArmMotor.setPosition(0);    // initializes encoder to zero 
		        frontArmMotor.set(0);   // speed to zero
		        
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
			
	
	public static void teleopInit() {
		
	}
	
	public static void teleopPeriodic()
	{			
		// both buttons pressed simultaneously, time to cal to ground
		if (gamepad.getRawButton(FRONT_ARM_GROUND_CAL_BUTTON1) && gamepad.getRawButton(FRONT_ARM_GROUND_CAL_BUTTON2)) {
			processGroundCal();
		}
			
		// PID CONTROL ONLY --- check for front arm control motion
		/*
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
		*/	
		
		// VBUS CONTROL ONLY --- check for front arm control motion
		double armSpeed = gamepad.getRawAxis(1);
		if (Math.abs(armSpeed) < ARM_DEADZONE) {
			armSpeed = 0.0f;
		}	
		armSpeed *= ARM_VBUS_MULTIPLIER;
		double pos= frontArmMotor.getPosition();
		
		// soft limit check (based on encoder value)
		if ((pos > SOFT_ENCODER_LIMIT_MAX) && armSpeed < 0.0)
		{
			//System.out.println("UPPER SOFT ARM LIMIT HIT! Setting speed to zero");
			//armSpeed = 0.0;
		}	
		
		if ((pos < ENCODER_POS_LOW) && armSpeed > 0.0) {
			//System.out.println("LOWER SOFT ARM LIMIT HIT! Setting speed to zero");
			//armSpeed = 0.0;			
		}
		
		// set the arm speed
		frontArmMotor.set(armSpeed);
		
		// check for roller motion
		double rollerSpeed = 0.0;
		if (gamepad.getRawButton(ROLLER_IN_BUTTON))
			rollerSpeed = -ARM_ROLLER_SPEED;
		else if (gamepad.getRawAxis(ROLLER_OUT_AXIS) > 0.1)
			rollerSpeed = ARM_ROLLER_SPEED;	
		frontArmRollerMotor.set(rollerSpeed);
					
		//System.out.println("Roller = " + rollerSpeed);
		
		// check for conveyer motion control
		double conveyerSpeed = 0.0;
		boolean ballDetected = UltrasonicSensor.isBallPresent();
		if ((gamepad.getRawButton(CONVEYER_IN_BUTTON)) ||
			gamepad.getRawButton(CONVEYER_DEPOSIT_BUTTON))
			conveyerSpeed = CONVEYER_SPEED;
		else if (gamepad.getRawAxis(CONVEYER_OUT_AXIS) > 0.1)
			conveyerSpeed = -CONVEYER_SPEED;
		conveyerMotor.set(conveyerSpeed);
		
		//System.out.println(" Conveyer = " + conveyerSpeed + " ballDetected = " + ballDetected);
		
	}
		
	public static void startArm(double speed) {
		if (frontArmMotor == null)
			return;
		
		frontArmMotor.set(speed);
	}
	
	public static void stopArm() {
		if (frontArmMotor == null)
			return;
		
		frontArmMotor.set(0);
	}
	
	public static double getPosition() {
		if (frontArmMotor == null)
			return 0.0;
		
		return frontArmMotor.getPosition();
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
		
		// always return true for now
		return true;
		/*
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
		*/
	}
	
	// if we are on the ground, reinitialize encoder to this value
	// this may be needed if arm position gets skewed
	// NOTE:  ASSUMES ARM IS PHYSICALLY AT LOWEST POINT
	private static void processGroundCal()
	{
		if (frontArmMotor != null) {
			System.out.println("Calibrating arm to lowest position");
			frontArmMotor.setPosition(ENCODER_POS_LOW);
		}
	}		

}

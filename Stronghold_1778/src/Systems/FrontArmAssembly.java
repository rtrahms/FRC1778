package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class FrontArmAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
	private static final double ARM_DEADZONE = 0.1;
	
    // limits
    // forward arm gear is 208:1 - for quarter turn of arm, about 50 motor revs
    private static final double SOFT_ENCODER_LIMIT_MAX = (4096.0*40.0);  // about a quarter turn up
    private static final double SOFT_ENCODER_LIMIT_LOW_MOBILE = (4096*4.0);  // just above the floor
    private static final double SOFT_ENCODER_LIMIT_FLOOR = 0.0;

    private static final int ARM_HARD_LIMIT_CHANNEL = 5;  // hard limit switch on arm - normally closed (0), will open (1) on contact
    
    // speeds
    private static final double ARM_ROLLER_SPEED = 0.5;
    private static final double CONVEYER_SPEED = 0.75;
    private static final double ARM_MULTIPLIER = -0.5;
    
    private static final double AUTO_CONVEYER_RUN_US = 2000000;  // auto conveyer run in microsec
    private static boolean isAutoConveyerDone;
    
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
	private static final int ROLLER_IN_BUTTON = 5;
	private static final int ROLLER_OUT_BUTTON = 7;
	
	private static final int CONVEYER_IN_BUTTON = 6;
	private static final int CONVEYER_OUT_BUTTON = 8;
	private static final int CONVEYER_DEPOSIT_BUTTON = 2;
	
    // control objects
    private static Joystick gamepad;
           
    // motor ids
    private static final int FRONT_ARM_MOTOR_ID = 11;
    private static final int FRONT_ARM_ROLLER_ID = 12;
    private static final int CONVEYER_MOTOR_ID = 13;
    
    private static CANTalon frontArmMotor, frontArmRollerMotor, conveyerMotor;
    
    private static DigitalInput frontArmLimitSwitch;
    
    private static long initTime;
    private static boolean teleopMode;
    private static boolean firstTimeCalComplete;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        teleopMode = false;
	        firstTimeCalComplete = false;
	        
	        frontArmLimitSwitch = new DigitalInput(ARM_HARD_LIMIT_CHANNEL);
	        
	        // create and initialize arm motor
	        frontArmMotor = new CANTalon(FRONT_ARM_MOTOR_ID);
	        if (frontArmMotor != null) {
	        	
		        System.out.println("Initializing front arm motor (position control)...");

		        //frontArmMotor.clearStickyFaults();
		        
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);	 
		        
		        // set brake mode    
	        	frontArmMotor.enableBrakeMode(true);
		        
	        	// initializes encoder to zero 		        
		        frontArmMotor.setPosition(0);    	
	        }
	        else
	        	System.out.println("ERROR: Front Arm motor not initialized!");
		  
	        // create and initialize roller motor
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

	        // create and initialize conveyer motor
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
		if (!firstTimeCalComplete)		
			startArmCal();
	}
	
	public static void autoPeriodic() {
		if (!firstTimeCalComplete)			
			processArmCal();			
	}
		
	public static void teleopInit() {        
        
        teleopMode = true;	
        
		if (!firstTimeCalComplete)		
			startArmCal();
	}
	
	public static void teleopPeriodic()
	{		

		if (!firstTimeCalComplete)			
			processArmCal();
		
		// check for front arm control motion
		double armSpeed = gamepad.getRawAxis(1);
		if (Math.abs(armSpeed) < ARM_DEADZONE) {
			armSpeed = 0.0f;
		}	
		armSpeed *= ARM_MULTIPLIER;
		double pos= frontArmMotor.getPosition();
		
		// soft limit check (based on encoder value)
		if (((pos > SOFT_ENCODER_LIMIT_MAX) && armSpeed < 0.0) || ((pos < SOFT_ENCODER_LIMIT_LOW_MOBILE) && armSpeed > 0.0))
		{
			System.out.println("SOFT ARM LIMIT HIT! Setting speed to zero");
			armSpeed = 0.0;
		}
		
		// hard limit HIT when trying to go higher - STOP ARM
		if (frontArmLimitSwitch.get() && armSpeed < 0.0)
		{
			System.out.println("HARD ARM LIMIT HIT! Setting speed to zero");
			armSpeed = 0.0;
		}
		
		frontArmMotor.set(armSpeed);
		//System.out.println("armSpeed = " + armSpeed + " enc pos = " + frontArmMotor.getPosition());	
		
		// check for roller motion
		double rollerSpeed = 0.0;
		if (gamepad.getRawButton(ROLLER_IN_BUTTON))
			rollerSpeed = ARM_ROLLER_SPEED;
		else if (gamepad.getRawButton(ROLLER_OUT_BUTTON))
			rollerSpeed = -ARM_ROLLER_SPEED;	
		frontArmRollerMotor.set(rollerSpeed);
					
		//System.out.println("Roller = " + rollerSpeed);
		
		// check for conveyer motion control
		double conveyerSpeed = 0.0;
		boolean ballDetected = UltrasonicSensor.isBallPresent();
		if ((gamepad.getRawButton(CONVEYER_IN_BUTTON))||
			gamepad.getRawButton(CONVEYER_DEPOSIT_BUTTON))
		//if (((gamepad.getRawButton(CONVEYER_IN_BUTTON)) && !ballDetected) ||
		//	gamepad.getRawButton(CONVEYER_DEPOSIT_BUTTON))
			conveyerSpeed = CONVEYER_SPEED;
		else if (gamepad.getRawButton(CONVEYER_OUT_BUTTON))
			conveyerSpeed = -CONVEYER_SPEED;
		conveyerMotor.set(conveyerSpeed);
		
		//System.out.println(" Conveyer = " + conveyerSpeed + " ballDetected = " + ballDetected);
		
		//System.out.println("Arm Limit Switch state = " + frontArmLimitSwitch.get());
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
	
	public static void disabledInit()
	{
		if (!initialized)
			initialize();	
		
		// if exiting from teleop mode (game is over)...
		if (teleopMode)
		{
			System.out.println("FrontArmAssembly: exiting teleop, moving to coast mode");
			// relax front arm motor (coast mode)
			frontArmMotor.enableBrakeMode(false);
		}
	}
	
	// start positioning the arm to correct position on power up
	private static void startArmCal()
	{
		// if we haven't calibrated the arm yet (first time power-up)
		/*
		double calMotorSpeed = -0.1;
		frontArmMotor.set(calMotorSpeed);		
		*/				
	}
	
	// status and/or stop calibration of the arm on first time power up
	private static void processArmCal()
	{
		/*
		// continue moving front arm just a bit (off floor)
		double pos = frontArmMotor.getPosition();
		
		// if the arm is high enough off the floor
		if (pos > SOFT_ENCODER_LIMIT_LOW_MOBILE) {
			// stop motor and mark cal as complete
			frontArmMotor.set(0.0);
			firstTimeCalComplete = true;
		}
		*/
	}		

}

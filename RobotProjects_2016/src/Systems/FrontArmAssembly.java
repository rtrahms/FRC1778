package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class FrontArmAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
	private static final double ARM_DEADZONE = 0.2;
	private static final double ROLLER_DEADZONE = 0.1;
	
    private static final long CYCLE_USEC = 250000;
    
    // limits
    // forward arm gear is 208:1 - for quarter turn of arm, about 50 motor revs
    //private static final double FORWARD_SOFT_ENCODER_LIMIT = (4096.0*50.0);
    private static final double FORWARD_SOFT_ENCODER_LIMIT = (4096.0*5.0);
    private static final double REVERSE_SOFT_ENCODER_LIMIT = 0.0;
    private static final double ARM_SPEED_MULTIPLIER = 2148.0;
    
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 0;
	
    // control objects
    private static Joystick gamepad;
           
    // motor ids
    private static final int FRONT_ARM_MOTOR_ID = 9;
    private static final int FRONT_ARM_ROLLER_ID = 6;
    
    private static CANTalon frontArmMotor, frontArmRollerMotor;
    
    private static long initTime;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        
	        // create and initialize arm motor
	        frontArmMotor = new CANTalon(FRONT_ARM_MOTOR_ID);
	        if (frontArmMotor != null) {
	        	
		        System.out.println("Initializing front arm motor (speed control)...");
	        	
	        	// set up motor for speed control mode
		        frontArmMotor.disableControl();
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        frontArmMotor.setPID(2.0, 0, 18.0);     // works pretty well	        	
		        //frontArmMotor.setPID(2.0, 0, 9.0);     // 1:4 PID ratio        	
		        //frontArmMotor.setPID(4.0, 0, 8.0);     // 1:2 PID ratio        	
	        	frontArmMotor.setForwardSoftLimit(FORWARD_SOFT_ENCODER_LIMIT);    	
	        	frontArmMotor.enableForwardSoftLimit(true);
	        	frontArmMotor.setReverseSoftLimit(REVERSE_SOFT_ENCODER_LIMIT);
	        	frontArmMotor.enableReverseSoftLimit(true);	        	
		        frontArmMotor.enableBrakeMode(true);
		        
		        // set speed to zero and enable control
		        frontArmMotor.set(0);
		        frontArmMotor.enableControl();
		        
		        // PercentVbus test ONLY!!!
		        //frontArmMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		        
	        	// initializes encoder to zero
		        frontArmMotor.setPosition(0);        	
	        }
	        else
	        	System.out.println("ERROR: Front Arm motor not initialized!");
		  /*
	        // create and initialize roller motor
	        frontArmRollerMotor = new CANTalon(FRONT_ARM_ROLLER_ID);
	        if (frontArmRollerMotor != null) {
	        	
		        System.out.println("Initializing front arm roller motor (PercentVbus control)...");
	        	
	        	// set up roller motor for percent Vbus control mode
		        frontArmRollerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        		        	
		        // no brake mode, no limits on rollers
		        frontArmRollerMotor.enableBrakeMode(false);
		        frontArmRollerMotor.enableForwardSoftLimit(false);
		        frontArmRollerMotor.enableReverseSoftLimit(false);
	        	
	        	// initializes speed of rollers to zero
		        frontArmRollerMotor.set(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Front Arm roller motor not initialized!");
	        */
		}
	}
	
	public static void autoInit() {
        initTime = Utility.getFPGATime();
	}
	
	public static void autoPeriodic(boolean liftCommand)
	{
		long currentTime = Utility.getFPGATime();

		// if not long enough, just return
		if ((currentTime - initTime) < CYCLE_USEC)
			return;

	}
	
	public static void autoStop()
	{
		// nothing to clean up here
	}
		
	public static void teleopInit() {
        initTime = Utility.getFPGATime();
        
    	// initializes encoder to zero
        frontArmMotor.setPosition(0);        	
		
	}
	
	public static void teleopPeriodic()
	{		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initTime) < CYCLE_USEC)
			return;
		
		// check for arm motion (left gamepad joystick)
		double armSpeed = gamepad.getRawAxis(1);
		if(Math.abs(armSpeed) <= ARM_DEADZONE) {
			armSpeed = 0.0;
		}
		armSpeed *= ARM_SPEED_MULTIPLIER;
		System.out.println("armspeed = " + armSpeed + " motor position = " + frontArmMotor.getPosition());
		frontArmMotor.set(armSpeed);
		
		// PercentVbus test ONLY!!
		//frontArmMotor.set(gamepad.getRawAxis(1));
		
		/*
		// check for roller motion (right gamepad joystick)
		double rollerSpeed = gamepad.getRawAxis(3);
		if (Math.abs(rollerSpeed) < ROLLER_DEADZONE) {
			rollerSpeed = 0.0f;
		}	
		frontArmRollerMotor.set(rollerSpeed);
		
		// reset input timer;
		initTime = Utility.getFPGATime();
		*/
	}

}

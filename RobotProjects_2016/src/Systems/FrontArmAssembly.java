package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class FrontArmAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
	private static final double ARM_DEADZONE = 0.1;
	private static final double ROLLER_DEADZONE = 0.1;
	
    // limits
    // forward arm gear is 208:1 - for quarter turn of arm, about 50 motor revs
    //private static final double SOFT_ENCODER_LIMIT_1 = (4096.0*40.0);  // about a turn
    private static final double SOFT_ENCODER_LIMIT_1 = (4096.0*20.0);  // about a quarter turn
    private static final double SOFT_ENCODER_LIMIT_2 = 0.0;
    private static final double ARM_SPEED_MULTIPLIER = 4096.0;
    private static final double ARM_ROLLER_MULTIPLIER = 0.1;
    
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
    // control objects
    private static Joystick gamepad;
           
    // motor ids
    private static final int FRONT_ARM_MOTOR_ID = 12;
    private static final int FRONT_ARM_ROLLER_ID = 11;
    
    private static CANTalon frontArmMotor, frontArmRollerMotor;
    
    private static long initTime;
    private static boolean teleopMode;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        teleopMode = false;
	        
	        // create and initialize arm motor
	        frontArmMotor = new CANTalon(FRONT_ARM_MOTOR_ID);
	        if (frontArmMotor != null) {
	        	
		        System.out.println("Initializing front arm motor (position control)...");

		        // VERY IMPORTANT - resets talon faults to render them usable again!!
		        frontArmMotor.clearStickyFaults();
	        	
	        	// set up motor for position control PID feedback mode only
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        frontArmMotor.setPID(2.0, 0, 18.0);     // works pretty well	        	
		        frontArmMotor.enableControl();    // enable PID control
		        		        
		        // PercentVbus test ONLY!!!
		        //frontArmMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);	 
		        
		        // set limits and brake mode
	        	frontArmMotor.setForwardSoftLimit(SOFT_ENCODER_LIMIT_1);    	
	        	frontArmMotor.enableForwardSoftLimit(true);
	        	frontArmMotor.setReverseSoftLimit(SOFT_ENCODER_LIMIT_2);
	        	frontArmMotor.enableReverseSoftLimit(true);	        	
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
	        	
		        frontArmRollerMotor.clearStickyFaults();
		        
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
	        
		}
	}
	
	public static void autoInit() 
	{
        teleopMode = false;
	}
	
	public static void autoPeriodic(boolean liftCommand)
	{
	}
	
	public static void autoStop()
	{
		// nothing to clean up here
	}
		
	public static void teleopInit() {        
        
        teleopMode = true;		
	}
	
	public static void teleopPeriodic()
	{		
				
		double newArmPos = gamepad.getRawAxis(1);
		if(Math.abs(newArmPos) <= ARM_DEADZONE) {
			newArmPos = 0.0;
		}
		newArmPos = (newArmPos * ARM_SPEED_MULTIPLIER) + frontArmMotor.getPosition();
		frontArmMotor.set(newArmPos);
		
		// PercentVbus test ONLY!!
		/*
		double armSpeed = gamepad.getRawAxis(1);
		if (Math.abs(armSpeed) < ARM_DEADZONE) {
			armSpeed = 0.0f;
		}	
		frontArmMotor.set(armSpeed);
		*/
		
		//System.out.println("enc pos = " + frontArmMotor.getPosition());
		
		// check for roller motion (right gamepad joystick)
		//double rollerSpeed = gamepad.getRawAxis(3);
		double rollerSpeed = gamepad.getRawAxis(5);
		if (Math.abs(rollerSpeed) < ROLLER_DEADZONE) {
			rollerSpeed = 0.0f;
		}	
		rollerSpeed *= ARM_ROLLER_MULTIPLIER;
		frontArmRollerMotor.set(rollerSpeed);
					
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

}

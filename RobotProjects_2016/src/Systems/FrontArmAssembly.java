package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class FrontArmAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
	private static final double ARM_DEADZONE = 0.2;
	private static final double ROLLER_DEADZONE = 0.1;
	
    // limits
    // forward arm gear is 208:1 - for quarter turn of arm, about 50 motor revs
    //private static final double FORWARD_SOFT_ENCODER_LIMIT = (4096.0*50.0);
    private static final double SOFT_ENCODER_LIMIT_1 = (4096.0*5.0);
    private static final double SOFT_ENCODER_LIMIT_2 = 0.0;
    private static final double ARM_SPEED_MULTIPLIER = 512.0;
    
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
    // control objects
    private static Joystick gamepad;
           
    // motor ids
    private static final int FRONT_ARM_MOTOR_ID = 11;
    private static final int FRONT_ARM_ROLLER_ID = 12;
    
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
	        	
		        System.out.println("Initializing front arm motor (speed control)...");
	        	
		        /*
	        	// set up motor for control mode
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        frontArmMotor.setSafetyEnabled(false);
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        frontArmMotor.setPID(2.0, 0, 18.0);     // works pretty well	        	
		        //frontArmMotor.setPID(2.0, 0, 9.0);     // 1:4 PID ratio        	
		        //frontArmMotor.setPID(4.0, 0, 8.0);     // 1:2 PID ratio    
		        
	        	frontArmMotor.setForwardSoftLimit(SOFT_ENCODER_LIMIT_1);    	
	        	frontArmMotor.enableForwardSoftLimit(true);
	        	frontArmMotor.setReverseSoftLimit(SOFT_ENCODER_LIMIT_2);
	        	frontArmMotor.enableReverseSoftLimit(true);	        	
		        frontArmMotor.enableBrakeMode(true);
		        
		        // set speed to to current
		        //frontArmMotor.set(frontArmMotor.getPosition());
		        */
		        // PercentVbus test ONLY!!!
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		        
	        	// initializes encoder to zero
		        frontArmMotor.setPosition(0);        	
	        }
	        else
	        	System.out.println("ERROR: Front Arm motor not initialized!");
		  
	        // create and initialize roller motor
	        frontArmRollerMotor = new CANTalon(FRONT_ARM_ROLLER_ID);
	        if (frontArmRollerMotor != null) {
	        	
		        System.out.println("Initializing front arm roller motor (PercentVbus control)...");
	        	
	        	// set up roller motor for percent Vbus control mode
		        frontArmRollerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        		        	
		        // no brake mode, no limits on rollers
		        frontArmRollerMotor.setSafetyEnabled(false);
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
        // enable motors
        //frontArmMotor.enable();
        //frontArmRollerMotor.enable();
        
        teleopMode = true;
        
    	// initializes encoder to zero
        frontArmMotor.setPosition(0);        	
		
	}
	
	public static void teleopPeriodic()
	{		
				
		// check for arm motion (left gamepad joystick)
		/*
		double armSpeed = gamepad.getRawAxis(1);
		if(Math.abs(armSpeed) <= ARM_DEADZONE) {
			armSpeed = 0.0;
		}
		
		armSpeed *= ARM_SPEED_MULTIPLIER;
		System.out.println("armspeed = " + armSpeed + " motor position = " + frontArmMotor.getPosition());
		frontArmMotor.set(armSpeed);
		*/
		
		/*
		double newArmPos = gamepad.getRawAxis(1);
		if(Math.abs(newArmPos) <= ARM_DEADZONE) {
			newArmPos = 0.0;
		}
		newArmPos = (newArmPos * ARM_SPEED_MULTIPLIER);
		System.out.println(" input = " + newArmPos + "enc pos = " + frontArmMotor.getPosition());
		//frontArmMotor.set(newArmPos);
		*/
		
		// PercentVbus test ONLY!!
		frontArmMotor.set(gamepad.getRawAxis(1));
		
		// check for roller motion (right gamepad joystick)
		double rollerSpeed = gamepad.getRawAxis(3);
		if (Math.abs(rollerSpeed) < ROLLER_DEADZONE) {
			rollerSpeed = 0.0f;
		}	
		frontArmRollerMotor.set(rollerSpeed);
					
	}
	
	public static void disabledInit()
	{
		/*
		if (teleopMode) {
	        frontArmMotor.enableBrakeMode(false);
	        frontArmRollerMotor.enableBrakeMode(false);
		}
		*/
	}

}

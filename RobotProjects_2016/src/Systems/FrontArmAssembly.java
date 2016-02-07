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
            	
	// controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
    // control objects
    private static Joystick gamepad;
           
    // motor ids
    private static final int FRONT_ARM_MOTOR_ID = 5;
    private static final int FRONT_ARM_ROLLER_ID = 6;
    
    private static CANTalon frontArmMotor, frontArmRollerMotor;
    
    private static long initTime;
    private static boolean pressed;
    private static double armSpeed, rollerSpeed;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        pressed = false;
	        armSpeed = 0.0;
	        rollerSpeed = 0.0;
	        
	        frontArmMotor = new CANTalon(FRONT_ARM_MOTOR_ID);
	        if (frontArmMotor != null) {
	        	
		        System.out.println("Initializing front arm motor (position control)...");
	        	
	        	// set up motor for position control mode
		        frontArmMotor.disableControl();
		        frontArmMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        frontArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	        	
	        	// P and D should be at a 1:4 ratio;  I should be ZERO
	        	// higher numbers equate to higher gain/current draw
	        	//frontArmMotor.setPID(8.0, 0, 32.0);  // DO NOT USE - FUN STUFF HAPPENS
	        	//frontArmMotor.setPID(3.0, 0, 12.0);
		        frontArmMotor.setPID(2.0, 0, 18.0);     // works pretty well
	        	//frontArmMotor.setPID(0.5, 0, 2.0);
	        	//frontArmMotor.setPID(0.1, 0, 0.5);    // good but weak
	        		        	
		        frontArmMotor.enableBrakeMode(true);
	        	//frontArmMotor.enableForwardSoftLimit(false);
	        	//frontArmMotor.enableReverseSoftLimit(false);
		        frontArmMotor.set(frontArmMotor.getPosition());
		        frontArmMotor.enableControl();
	        	
	        	// initializes encoder to zero
		        frontArmMotor.setPosition(0);        	
	        }
	        else
	        	System.out.println("ERROR: Front Arm motor not initialized!");
		  
	        frontArmRollerMotor = new CANTalon(FRONT_ARM_ROLLER_ID);
	        if (frontArmRollerMotor != null) {
	        	
		        System.out.println("Initializing front arm roller motor (speed control, no encoder)...");
	        	
	        	// set up motor for percent Vbus control mode
		        frontArmRollerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        		        		        	
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
		
	}
	
	public static void teleopPeriodic()
	{		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initTime) < CYCLE_USEC)
			return;
		
		armSpeed = gamepad.getRawAxis(2);
		if(Math.abs(armSpeed) <= ARM_DEADZONE) {
			armSpeed = 0.0f;
		}
		
		rollerSpeed = gamepad.getRawAxis(5);
		if (Math.abs(rollerSpeed) < ROLLER_DEADZONE) {
			rollerSpeed = 0.0f;
		}
					
		frontArmMotor.set(armSpeed);
		frontArmRollerMotor.set(rollerSpeed);

		// reset input timer;
		initTime = Utility.getFPGATime();
	}

}

package Systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
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
    
    private static TalonSRX frontArmMotor, frontArmRollerMotor;
    
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
	        
	        frontArmMotor = new TalonSRX(FRONT_ARM_MOTOR_ID);
	        frontArmRollerMotor = new TalonSRX(FRONT_ARM_ROLLER_ID);
	        
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

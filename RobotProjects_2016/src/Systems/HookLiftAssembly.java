package Systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Utility;

public class HookLiftAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
    private static final long CYCLE_USEC = 250000;
            	
	//  controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
    private static Joystick gamepad;
    
    private static final int DEPLOY_MOTOR_ID = 7;
    private static final int TELESCOPE_MOTOR_ID = 8;
    private static final int WINCH_MOTOR_ID = 9;
           
    private static final int ARM_STOW_POS = 0;
    private static final int ARM_DEPLOY_POS = 90;
    
    private static final int TELESCOPE_RETRACT_POS = 0;
    private static final int TELESCOPE_EXTEND_POS = 100;
    
    private static final double WINCH_DEAD_ZONE = 0.2;
    
    private static TalonSRX deployMotor, telescopeMotor, winchMotor;
    
    private static long initTime;
    private static boolean armDeployed;
    private static boolean armExtended;
    private static double winchSpeed;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        
	        armDeployed = false;
	        armExtended = false;
	        winchSpeed = 0.0;
	        
	        deployMotor = new TalonSRX(DEPLOY_MOTOR_ID);
	        telescopeMotor = new TalonSRX(TELESCOPE_MOTOR_ID);
	        winchMotor = new TalonSRX(WINCH_MOTOR_ID);
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
		
					
		// get right bumper button - deploy control
		if (gamepad.getRawButton(7))
		{
			armDeployed = !armDeployed;
			
			if (!armDeployed) {
				armExtended = false;
				telescopeMotor.setPosition(TELESCOPE_RETRACT_POS);  // retract telescope before stowing
				deployMotor.setPosition(ARM_STOW_POS);
			}
			else {
				deployMotor.setPosition(ARM_DEPLOY_POS);
			}
		}
		
		// if button to extend telescope is pressed (and arm is deployed)
		if ((gamepad.getRawButton(8)) && (armDeployed))
		{
			armExtended = true;
			telescopeMotor.setPosition(TELESCOPE_EXTEND_POS);
		}
		
		// if button to retract telescope is pressed
		if (gamepad.getRawButton(9))
		{
			armExtended = false;
			telescopeMotor.setPosition(TELESCOPE_RETRACT_POS);
		}
		
		// ONLY WHEN ARM IS DEPLOYED do we execute winch control
		if (armDeployed)
		{
			// check thumbpad Y axis
			winchSpeed = gamepad.getRawAxis(10);
			if (winchSpeed < WINCH_DEAD_ZONE)
				winchSpeed = 0.0;
			
			winchMotor.set(winchSpeed);
		}
		
		// reset init timer
        initTime = Utility.getFPGATime();
	
	}

}

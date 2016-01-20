package Systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Utility;

public class CatapultAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
    private static final long CYCLE_USEC = 250000;
    
    // catapult reset time (after firing) - 4 sec
    private static final long CATAPULT_RESET_WAIT_USEC = 4000000;
            	
	// joystick device ids
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	
    //  control objects
    private static Joystick leftJoy, rightJoy;
           
    // catapult reset motor
    private static final int CATAPULT_MOTOR_ID = 4;
    private static final int CATAPULT_READY_POSITION = 0;
    
    private static TalonSRX catapultMotor;
    
    private static long initTime, catapultFireTime;
    private static boolean pressed;
    private static boolean autoTargeting;
    private static boolean catapultFired;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        leftJoy = new Joystick(LEFT_JOYSTICK_ID);
	        rightJoy = new Joystick(RIGHT_JOYSTICK_ID);
	        	                	        
	        initialized = true;
	        pressed = false;
	        autoTargeting = false;
	        catapultFired = false;
	        
	        // initialize catapult motor
	        catapultMotor = new TalonSRX(CATAPULT_MOTOR_ID);
	        
	        if (catapultMotor != null) {
	        	catapultMotor.setPosition(CATAPULT_READY_POSITION);
	        	catapultMotor.stopMotor();
	        }
	        else
	        	System.out.println("ERROR: Catapult motor not initialized!");
	        
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
		
		// if the catapult is not yet fired
		if (!catapultFired) 
		{
			// check for clicking of either joystick trigger
			if (leftJoy.getTrigger())
			{
				// reset timer
				initTime = Utility.getFPGATime();
				
				// Set "fire in the hole" color
				RioDuinoAssembly.sendColor(RioDuinoAssembly.Color.Purple);
				
				// fire catapult - manual fire
				catapultMotor.free();
				
				catapultFired = true;
				catapultFireTime = Utility.getFPGATime();
					
			}
			else if (rightJoy.getTrigger())
			{
				// reset timer
				initTime = Utility.getFPGATime();
				
				// Set "fire in the hole" color
				RioDuinoAssembly.sendColor(RioDuinoAssembly.Color.Purple);
				
				// fire catapult - auto targeting fire
				catapultMotor.free();
				
				catapultFired = true;
				catapultFireTime = Utility.getFPGATime();
			}
		}
		else {
			if ((currentTime - catapultFireTime) > CATAPULT_RESET_WAIT_USEC)
			{
				// reset catapult motor
				catapultMotor.setPosition(CATAPULT_READY_POSITION);
				catapultFired = false;
				
				// reset team color on robot
				RioDuinoAssembly.setTeamColor();
			}
		}
		
		// reset input timer;
		initTime = Utility.getFPGATime();

	}

}

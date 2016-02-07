package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class HookLiftAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
    private static final long CYCLE_USEC = 250000;
            	
	//  controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
    private static Joystick gamepad;
    
    // deployment motor constants
    //private static final int DEPLOY_MOTOR_ID = 7;
    //private static final int ARM_STOW_POS = 0;
    //private static final int ARM_DEPLOY_POS = 90;
    
    // telescoping motor constants
    private static final int TELESCOPE_MOTOR_ID = 8;
    private static final int TELESCOPE_RETRACT_POS = 0;
    private static final int TELESCOPE_EXTEND_POS = (int) (4096.0*12.0);
    
    // winch motor constants
    private static final int WINCH_MOTOR_ID = 9;  
    private static final double WINCH_DEAD_ZONE = 0.2;
    
    private static CANTalon deployMotor, telescopeMotor, winchMotor;
    
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
	        
	        //deployMotor = new CANTalon(DEPLOY_MOTOR_ID);
	        
	        // initialize telescope motor (position control with encoder feedback)
	        telescopeMotor = new CANTalon(TELESCOPE_MOTOR_ID);
	        if (telescopeMotor != null) {
	        	
		        System.out.println("Initializing telescoping motor...");
	        	
	        	// set up motor for position control mode
		        telescopeMotor.disableControl();
		        telescopeMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        telescopeMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	        	
	        	// P and D should be at a 1:4 ratio;  I should be ZERO
	        	// higher numbers equate to higher gain/current draw
	        	//telescopeMotor.setPID(8.0, 0, 32.0);  // DO NOT USE - FUN STUFF HAPPENS
	        	//telescopeMotor.setPID(3.0, 0, 12.0);
		        telescopeMotor.setPID(2.0, 0, 18.0);     // works pretty well
	        	//telescopeMotor.setPID(0.5, 0, 2.0);
	        	//telescopeMotor.setPID(0.1, 0, 0.5);    // good but weak
	        		        	
		        telescopeMotor.enableBrakeMode(true);
	        	//telescopeMotor.enableForwardSoftLimit(false);
	        	//telescopeMotor.enableReverseSoftLimit(false);
		        telescopeMotor.set(telescopeMotor.getPosition());
		        telescopeMotor.enableControl();
	        	
	        	// initializes encoder to zero
		        telescopeMotor.setPosition(0);
	        	
	        	// sets catapult into ready position!
	            //telescopeMotor.set(CATAPULT_READY_POSITION);
		        
		        // motor speed test ONLY - do not set during position control
	        	//telescopeMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	        }
	        else
	        	System.out.println("ERROR: Telescope motor not initialized!");
	        
	        winchMotor = new CANTalon(WINCH_MOTOR_ID);
	        if (winchMotor != null) {
	        	
		        System.out.println("Initializing winch motor (speed control, no encoder)...");
	        	
	        	// set up motor for percent Vbus control mode
		        winchMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        		        		        	
		        winchMotor.enableBrakeMode(false);
		        winchMotor.enableForwardSoftLimit(false);
		        winchMotor.enableReverseSoftLimit(false);
	        	
	        	// initializes speed of rollers to zero
		        winchMotor.set(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Winch motor not initialized!");
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
				//deployMotor.setPosition(ARM_STOW_POS);
			}
			else {
				//deployMotor.setPosition(ARM_DEPLOY_POS);
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

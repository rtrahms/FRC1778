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
    private static final double TELESCOPE_RETRACT_POS = 0.0;
    private static final double TELESCOPE_EXTEND_POS = (4096.0*12.0);
    
    // winch motor constants
    private static final int WINCH_MOTOR_ID = 9;  
    private static final double WINCH_DEAD_ZONE = 0.2;
    private static final double WINCH_IN_POS = 0.0;
    private static final double WINCH_MAX_OUT_POS = (4096.0*12.0);
    private static final double WINCH_MOTION_MULTIPLIER = 1.0;
    
    private static CANTalon telescopeMotor, winchMotor;
    
    private static long initTime;
    private static boolean telescopePressed;    
    private static boolean telescopeUp;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        
	        telescopePressed = false;	        
	        telescopeUp = false;
	        
	        // deployMotor not used
	        // deployMotor = new CANTalon(DEPLOY_MOTOR_ID);
	        
	        // create and initialize telescope motor (position control with encoder feedback)
	        telescopeMotor = new CANTalon(TELESCOPE_MOTOR_ID);
	        if (telescopeMotor != null) {
	        	
		        System.out.println("Initializing telescoping motor (position control with encoder)...");
	        	
	        	// set up motor for position control mode
		        telescopeMotor.disableControl();
		        telescopeMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        telescopeMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	        	
	        	// P and D should be at a ~1:4 ratio;  I should be ZERO
	        	// higher numbers equate to higher gain/current draw
		        telescopeMotor.setPID(2.0, 0, 18.0);     // works pretty well
	        	//telescopeMotor.setPID(0.1, 0, 0.5);    // good but weak
	        		 
		        // set brake mode on, and set soft limits on telescope arm motion
		        telescopeMotor.enableBrakeMode(true);
		        telescopeMotor.setForwardSoftLimit(TELESCOPE_EXTEND_POS);    	
		        telescopeMotor.enableForwardSoftLimit(true);
		        telescopeMotor.setReverseSoftLimit(TELESCOPE_RETRACT_POS);
		        telescopeMotor.enableReverseSoftLimit(true);

		        telescopeMotor.set(telescopeMotor.getPosition());
		        telescopeMotor.enableControl();
	        	
	        	// initializes encoder to zero
		        telescopeMotor.setPosition(0);
	        }
	        else
	        	System.out.println("ERROR: Telescope motor not initialized!");
	        
	        // create and initialize winch motor
	        winchMotor = new CANTalon(WINCH_MOTOR_ID);
	        if (winchMotor != null) {
	        	
		        System.out.println("Initializing winch motor (position control with encoder)...");
	        	
	        	// set up motor for percent Vbus control mode
		        winchMotor.disableControl();
		        winchMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        winchMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        		        	
	        	// P and D should be at a ~1:4 ratio;  I should be ZERO
	        	// higher numbers equate to higher gain/current draw
		        winchMotor.setPID(2.0, 0, 18.0);     // works pretty well
	        	//winchMotor.setPID(0.1, 0, 0.5);    // good but weak

		        // enable brake mode and soft limits for winch motor
		        winchMotor.enableBrakeMode(true);
		        winchMotor.setForwardSoftLimit(WINCH_MAX_OUT_POS);    	
		        winchMotor.enableForwardSoftLimit(true);
		        winchMotor.setReverseSoftLimit(WINCH_IN_POS);
		        winchMotor.enableReverseSoftLimit(true);
	        	
	        	// initializes speed of winch to zero
		        winchMotor.set(winchMotor.getPosition());
		        winchMotor.enableControl();
	        	
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
		
		// check for telescope trigger
		if (gamepad.getRawButton(3) && !telescopePressed)
			telescopePressed = true;

		
		// if button to extend telescope is pressed
		if (telescopePressed)
		{
			if (telescopeUp)
			{
				telescopeMotor.set(TELESCOPE_RETRACT_POS);
				telescopeUp = false;
			}
			else
			{
				telescopeMotor.set(TELESCOPE_EXTEND_POS);
				telescopeUp = true;				
			}
			
			// reset pressed flag
			telescopePressed = false;
		}
		
		// check thumbpad Y axis for winch motion
		double incrementalWinchPos = gamepad.getRawAxis(10);
		if (Math.abs(incrementalWinchPos) <= WINCH_DEAD_ZONE)
			incrementalWinchPos = 0.0;
		
		double newWinchTarget = winchMotor.getPosition() + (incrementalWinchPos * WINCH_MOTION_MULTIPLIER);
		if ((newWinchTarget >= WINCH_IN_POS) && (newWinchTarget <= WINCH_MAX_OUT_POS))
			winchMotor.set(newWinchTarget);
		
		// reset init timer
        initTime = Utility.getFPGATime();
	
	}

}

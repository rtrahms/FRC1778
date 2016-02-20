package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class HookLiftAssembly {
	private static boolean initialized = false;
	            	
	//  controller gamepad ID - assumes no other controllers connected
	private static final int GAMEPAD_ID = 2;
	
    private static Joystick gamepad;
        
    // telescoping motor constants
    private static final int TELESCOPE_MOTOR_ID = 14;
    private static final double TELESCOPE_RETRACT_POS = 0.0;
    private static final double TELESCOPE_EXTEND_POS = (4096.0*12.0);
    
    // winch motor constants
    private static final int WINCH_MOTOR_ID = 15;  
    private static final double WINCH_DEAD_ZONE = 0.2;
    private static final double WINCH_IN_POS = 0.0;
    private static final double WINCH_MAX_OUT_POS = (4096.0*12.0);
    private static final double WINCH_MOTION_MULTIPLIER = 1.0;
    
    private static CANTalon telescopeMotor, winchMotor;
    
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
	        	        
	        // create and initialize telescope motor (position control with encoder feedback)
	        telescopeMotor = new CANTalon(TELESCOPE_MOTOR_ID);
	        if (telescopeMotor != null) {
	        	
		        System.out.println("Initializing telescoping motor (position control)...");
		        
		        // VERY IMPORTANT - resets talon faults to render them usable again!!
		        telescopeMotor.clearStickyFaults();
	        	
	        	// set up motor for position control mode
		        telescopeMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        telescopeMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        telescopeMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        telescopeMotor.enableBrakeMode(true);
		        
		        telescopeMotor.setForwardSoftLimit(TELESCOPE_EXTEND_POS);    	
		        telescopeMotor.enableForwardSoftLimit(true);
		        telescopeMotor.setReverseSoftLimit(TELESCOPE_RETRACT_POS);
		        telescopeMotor.enableReverseSoftLimit(true);
	        	
	        	// initializes encoder to zero
		        telescopeMotor.setPosition(0);
	        }
	        else
	        	System.out.println("ERROR: Telescope motor not initialized!");
	        
	        // create and initialize winch motor
	        winchMotor = new CANTalon(WINCH_MOTOR_ID);
	        if (winchMotor != null) {
	        	
		        System.out.println("Initializing winch motor (speed control)...");
		        
		        // VERY IMPORTANT - resets talon faults to render them usable again!!
		        winchMotor.clearStickyFaults();
	        	
	        	// set up motor for speed control mode
		        winchMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        winchMotor.setSafetyEnabled(false);
		        winchMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        winchMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        winchMotor.enableBrakeMode(true);
		        winchMotor.setForwardSoftLimit(WINCH_MAX_OUT_POS);    	
		        winchMotor.enableForwardSoftLimit(true);
		        winchMotor.setReverseSoftLimit(WINCH_IN_POS);
		        winchMotor.enableReverseSoftLimit(true);
		        		        
	        	// initializes encoder to zero
		        winchMotor.setPosition(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Winch motor not initialized!");
		}
	}
	
	public static void autoInit() 
	{
	}
	
	public static void autoPeriodic()
	{
	}
	
	public static void autoStop()
	{
		// nothing to clean up here
	}
		
	public static void teleopInit() 
	{
	}
	
	public static void teleopPeriodic()
	{		
		
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
	}

	public static void disabledInit()
	{
		if (!initialized)
			initialize();
	}

}

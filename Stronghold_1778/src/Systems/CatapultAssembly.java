package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class CatapultAssembly {
	private static boolean initialized = false;
	                	
	// joystick device ids
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	
    //  control objects
    private static Joystick leftJoy, rightJoy;
           
    // catapult reset motor
    private static final int CATAPULT_MOTOR_ID = 9;
    
    private static final double NUM_TICKS_PER_REV = 4096;
    private static final double VERSAPLANETARY_RATIO = 100;
    private static final double CHOO_CHOO_GEAR_RATIO = 84.0/18.0;
    private static final double FIRE_ROTATION_BACKOFF_REV = 0.25;
    
    private static final double CATAPULT_FIRE_INCREMENT = FIRE_ROTATION_BACKOFF_REV*NUM_TICKS_PER_REV*VERSAPLANETARY_RATIO*CHOO_CHOO_GEAR_RATIO;
    private static final double CATAPULT_READY_POSITION = (NUM_TICKS_PER_REV*VERSAPLANETARY_RATIO*CHOO_CHOO_GEAR_RATIO) - CATAPULT_FIRE_INCREMENT;
    
    private static final int TRIGGER_CYCLE_WAIT_US = 1000000;
    
    private static CANTalon catapultMotor;
    
    private static boolean pressed;
    private static boolean catapultFired;
    private static boolean teleopMode;
    
    private static double initTriggerTime;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        leftJoy = new Joystick(LEFT_JOYSTICK_ID);
	        rightJoy = new Joystick(RIGHT_JOYSTICK_ID);
	        	                	        
	        initialized = true;
	        catapultFired = true;
	        pressed = false;
	        teleopMode = false;
	        
	        System.out.println("Creating catapult motor object...");
	        
	        // initialize master catapult motor
	        catapultMotor = new CANTalon(CATAPULT_MOTOR_ID);
	        
	        if (catapultMotor != null) {
	        	
		        System.out.println("Initializing catapult motor (position mode)...");
		        
		        // VERY IMPORTANT - resets talon faults to render them usable again!!
		        //catapultMotor.clearStickyFaults();
		        
	        	// set up motor for position control mode
		        catapultMotor.enableControl();        // enables PID control
		        catapultMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        catapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        catapultMotor.setPID(0.1, 0, 0.0);   
		        //catapultMotor.set(catapultMotor.getPosition());   // set motor to current position
		        catapultMotor.setPosition(0);	      // initializes encoder to zero	        	
		      
		        // set brake mode
		        //catapultMotor.enableBrakeMode(true);
		       
	        }
	        else
	        	System.out.println("ERROR: Catapult motor not initialized!");		
		}
	}
	
	public static void autoInit() {
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
        pressed = false;
        
        initTriggerTime = Utility.getFPGATime();
	}
	
	public static void teleopPeriodic()
	{
		double currentTime = Utility.getFPGATime();
		
		if ((currentTime - initTriggerTime) < TRIGGER_CYCLE_WAIT_US)
			return;
		
		// check for catapult triggers
		if (leftJoy.getTrigger() && rightJoy.getTrigger() && !pressed)
		{
			pressed = true;
			System.out.println("trigger pressed!");

			// reset trigger init time
			initTriggerTime = Utility.getFPGATime();
					
			// check for catapult trigger
			if (pressed) {
				if (!catapultFired) 
					shoot();
				else
					reset();
			}	

			pressed = false;
		}
		
	}
	
	public static void disabledInit()
	{
		if (!initialized)
			initialize();
		
		// if we are exiting teleop mode...
		if (teleopMode)
		{
			// fire catapult if ready to fire
			if (!catapultFired)
				shoot();			
		}
	}

	public static void shoot()
	{
		// fire catapult
		catapultMotor.setPosition(0);
		catapultMotor.set(CATAPULT_FIRE_INCREMENT);
		System.out.println("Catapult Fired!  new pos = " + catapultMotor.getPosition());
		
		// set fired flag
		catapultFired = true;
		pressed = false;		
	}
	
	public static void reset()
	{		
		// reset catapult motor
		catapultMotor.setPosition(0);
		catapultMotor.set(CATAPULT_READY_POSITION);
		System.out.println("Catapult Reset!  new pos = " + catapultMotor.getPosition());
		
		// reset fired flag
		catapultFired = false;
		pressed = false;
	}
	
	public static boolean isFired()
	{
		return catapultFired;
	}
}
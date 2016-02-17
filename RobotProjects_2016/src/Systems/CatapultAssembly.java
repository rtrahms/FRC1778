package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class CatapultAssembly {
	private static boolean initialized = false;
	                	
	// joystick device ids
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	
    //  control object
    private static Joystick leftJoy, rightJoy;
           
    // catapult reset motor
    private static final int CATAPULT_MOTOR_ID = 9;
    
    private static final int CATAPULT_FIRE_INCREMENT = 1024;
    private static final int CATAPULT_READY_POSITION = (int) (4096.0*5.25 - CATAPULT_FIRE_INCREMENT);
    
    private static CANTalon catapultMotor;
    
    private static boolean pressed;
    private static boolean catapultFired;
    private static boolean teleopMode;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        leftJoy = new Joystick(LEFT_JOYSTICK_ID);
	        rightJoy = new Joystick(RIGHT_JOYSTICK_ID);
	        	                	        
	        initialized = true;
	        catapultFired = false;
	        pressed = false;
	        teleopMode = false;
	        
	        System.out.println("Creating motor objects...");
	        
	        // initialize master catapult motor
	        catapultMotor = new CANTalon(CATAPULT_MOTOR_ID);
	        
	        if (catapultMotor != null) {
	        	
		        System.out.println("Initializing catapult motor (position mode)...");
		        
		        // VERY IMPORTANT - resets talon faults to render them usable again!!
		        catapultMotor.clearStickyFaults();
	        	
	        	// set up motor for position control mode
		        catapultMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        catapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        catapultMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        catapultMotor.enableControl();        // enables PID control
		        catapultMotor.setPosition(0);	      // initializes encoder to zero
	        
		        // set brake mode and limits to false
		        catapultMotor.enableBrakeMode(true);
		        catapultMotor.enableForwardSoftLimit(false);
		        catapultMotor.enableReverseSoftLimit(false);		        
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
	}
	
	public static void teleopPeriodic()
	{				
		//System.out.println("Read enc position =" + masterCatapultMotor.getEncPosition());
		
		// check for catapult triggers
		if (leftJoy.getTrigger() && rightJoy.getTrigger() && !pressed)
			pressed = true;
				
		//System.out.println("motor enc = "+ masterCatapultMotor.getEncPosition());
		
		// check for catapult trigger
		if (pressed) {
			// if the catapult is not fired
			if (!catapultFired) 
				shoot();
			else
				reset();
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
		System.out.println("TRIGGER!  Catapult firing");
		
		// Set "fire in the hole" color
		//RioDuinoAssembly.sendColor(RioDuinoAssembly.Color.Purple);
		
		// fire catapult - manual fire
		catapultMotor.setPosition(0);
		catapultMotor.set(CATAPULT_FIRE_INCREMENT);
		//System.out.println("Fired!  new encoder pos = " + catapultMotor.getPosition());
		
		// set fired flag
		catapultFired = true;
		pressed = false;		
	}
	
	public static void reset()
	{
		System.out.println("catapult resetting");
		
		// reset catapult motor
		catapultMotor.setPosition(0);
		catapultMotor.set(CATAPULT_READY_POSITION);
		//System.out.println("Reset!  new encoder pos = " + catapultMotor.getPosition());
		
		// reset fired flag
		catapultFired = false;
		pressed = false;
						
		// reset team color on robot
		//RioDuinoAssembly.setTeamColor();				
	}
}
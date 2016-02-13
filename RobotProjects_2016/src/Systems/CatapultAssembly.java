package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class CatapultAssembly {
	private static boolean initialized = false;
	
    // minimum increment (for joystick dead zone)
    private static final long CYCLE_USEC = 250000;
    
    // catapult reset time (after firing) - 4 sec
    private static final long CATAPULT_RESET_WAIT_USEC = 2000000;
            	
	// joystick device ids
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	
    //  control object
    private static Joystick leftJoy, rightJoy;
           
    // catapult reset motor
    private static final int MASTER_CATAPULT_MOTOR_ID = 9;
    private static final int SLAVE_CATAPULT_MOTOR_ID = 10;
    
    private static final int CATAPULT_FIRE_INCREMENT = 1024;
    private static final int CATAPULT_READY_POSITION = (int) (4096.0*5.25 - CATAPULT_FIRE_INCREMENT);
    
    private static CANTalon masterCatapultMotor, slaveCatapultMotor;
    
    private static long initCycleTime;
    private static boolean pressed;
    private static boolean catapultFired;
    private static boolean teleopMode;
    private static int counter;

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
	        counter = 0;
	        
	        System.out.println("Creating motor objects...");
	        
	        // initialize master catapult motor
	        masterCatapultMotor = new CANTalon(MASTER_CATAPULT_MOTOR_ID);
	        
	        if (masterCatapultMotor != null) {
	        	
		        System.out.println("Initializing master catapult motor (position mode)...");
	        	
	        	// set up motor for position control mode
		        masterCatapultMotor.setSafetyEnabled(false);
		        masterCatapultMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        masterCatapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        masterCatapultMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        masterCatapultMotor.enableBrakeMode(true);
		        masterCatapultMotor.enableForwardSoftLimit(false);
		        masterCatapultMotor.enableReverseSoftLimit(false);
		        
		        // initialize position
		        masterCatapultMotor.set(masterCatapultMotor.getPosition());
	        	
	        	// initializes master encoder to zero
		        masterCatapultMotor.setPosition(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Master catapult motor not initialized!");

	        // initialize slave catapult motor
	        slaveCatapultMotor = new CANTalon(SLAVE_CATAPULT_MOTOR_ID);
	        
	        if (slaveCatapultMotor != null) {
	        	
		        System.out.println("Initializing slave catapult motor (follower mode)...");
	        	
	        	// set up slave motor for follower control mode (follows master above, but mirrored)
		        masterCatapultMotor.setSafetyEnabled(false);
		        slaveCatapultMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		        slaveCatapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        slaveCatapultMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        slaveCatapultMotor.enableBrakeMode(true);
		        slaveCatapultMotor.enableForwardSoftLimit(false);
		        slaveCatapultMotor.enableReverseSoftLimit(false);
		        slaveCatapultMotor.reverseOutput(true);		       
		        
		        // initialize master id
		        slaveCatapultMotor.set(MASTER_CATAPULT_MOTOR_ID);
	        	
	        	// initializes slave encoder to zero
		        slaveCatapultMotor.setPosition(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Slave catapult motor not initialized!");
		
		
		}
	}
	
	public static void autoInit() {
        initCycleTime = Utility.getFPGATime();
        teleopMode = false;
        
	}
	
	public static void autoPeriodic(boolean liftCommand)
	{
		long currentTime = Utility.getFPGATime();

		// if not long enough, just return
		if ((currentTime - initCycleTime) < CYCLE_USEC)
			return;

	}
	
	public static void autoStop()
	{
		// nothing to clean up here
	}
		
	public static void teleopInit() {
        initCycleTime = Utility.getFPGATime();	
                
        // enable motors
        masterCatapultMotor.enable();
        slaveCatapultMotor.enable();
        
        // simple vbus test ONLY - NOT FOR POSITION MODE
        //masterCatapultMotor.set(0.5);
        
		//System.out.println("teleop_init: motor enc = "+ masterCatapultMotor.getEncPosition());

        teleopMode = true;
        pressed = false;
	}
	
	public static void teleopPeriodic()
	{		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initCycleTime) < CYCLE_USEC)
			return;
		
		//System.out.println("Read enc position =" + masterCatapultMotor.getEncPosition());
		
		// check for catapult triggers
		if (leftJoy.getTrigger() && rightJoy.getTrigger() && !pressed)
			pressed = true;
				
		System.out.println("motor enc = "+ masterCatapultMotor.getEncPosition());
		
		// check for catapult trigger
		if (pressed) {
			// if the catapult is not fired
			if (!catapultFired) 
				shoot();
			else
				reset();
		}
		
		// reset cycle timer;
		initCycleTime = Utility.getFPGATime();
		
	}
	
	public static void disabledInit()
	{
		// if we are exiting teleop mode...
		if (teleopMode)
		{
			// fire catapult if ready to fire
			if (!catapultFired)
				shoot();
			
			// set motors to coast mode
			masterCatapultMotor.enableBrakeMode(false);
			slaveCatapultMotor.enableBrakeMode(false);
			
			// disable motors
			masterCatapultMotor.disable();
			slaveCatapultMotor.disable();
		}
	}

	public static void shoot()
	{
		System.out.println("TRIGGER!  Catapult firing");
		
		// Set "fire in the hole" color
		//RioDuinoAssembly.sendColor(RioDuinoAssembly.Color.Purple);
		
		// fire catapult - manual fire
		masterCatapultMotor.setPosition(0);
		masterCatapultMotor.set(CATAPULT_FIRE_INCREMENT);
		//System.out.println("Fired!  new encoder pos = " + masterCatapultMotor.getPosition());
		
		// set fired flag
		catapultFired = true;
		pressed = false;		
	}
	
	public static void reset()
	{
		System.out.println("catapult resetting");
		
		// reset catapult motor
		masterCatapultMotor.setPosition(0);
		masterCatapultMotor.set(CATAPULT_READY_POSITION);
		//System.out.println("Reset!  new encoder pos = " + catapultMotor.getPosition());
		
		// reset fired flag
		catapultFired = false;
		pressed = false;
						
		// reset team color on robot
		//RioDuinoAssembly.setTeamColor();				
	}
}

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
            	
	// gamepad device id
	private static final int GAMEPAD_ID = 0;
	
    //  control object
    private static Joystick gamepad;
           
    // catapult reset motor
    private static final int CATAPULT_MOTOR_ID = 9;
    private static final int CATAPULT_FIRE_INCREMENT = 1024;
    private static final int CATAPULT_READY_POSITION = (int) (4096.0*5.25 - CATAPULT_FIRE_INCREMENT);
    
    private static CANTalon catapultMotor;
    
    private static long initCycleTime;
    private static boolean pressed;
    private static boolean catapultFired;
    private static int counter;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        gamepad = new Joystick(GAMEPAD_ID);
	        	                	        
	        initialized = true;
	        catapultFired = false;
	        pressed = false;
	        counter = 0;
	        
	        System.out.println("Creating motor object...");
	        
	        // initialize catapult motor
	        catapultMotor = new CANTalon(CATAPULT_MOTOR_ID);
	        
	        if (catapultMotor != null) {
	        	
		        System.out.println("Initializing motor...");
	        	
	        	// set up motor for position control mode
	        	catapultMotor.disableControl();
	        	catapultMotor.changeControlMode(CANTalon.TalonControlMode.Position);
	        	catapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	        	
	        	// P and D should be at a 1:4 ratio;  I should be ZERO
	        	// higher numbers equate to higher gain/current draw
	        	//catapultMotor.setPID(8.0, 0, 32.0);  // DO NOT USE - FUN STUFF HAPPENS
	        	//catapultMotor.setPID(3.0, 0, 12.0);
	        	catapultMotor.setPID(2.0, 0, 18.0);     // works pretty well
	        	//catapultMotor.setPID(0.5, 0, 2.0);
	        	//catapultMotor.setPID(0.1, 0, 0.5);    // good but weak
	        		        	
	        	// enable brake mode, but no soft limits needed
	        	catapultMotor.enableBrakeMode(true);
	        	catapultMotor.enableForwardSoftLimit(false);
	        	catapultMotor.enableReverseSoftLimit(false);
	        	catapultMotor.set(catapultMotor.getPosition());
	        	catapultMotor.enableControl();
	        	
	        	// initializes encoder to zero
	        	catapultMotor.setPosition(0);
	        	
	        	// sets catapult into ready position!
	            //catapultMotor.set(CATAPULT_READY_POSITION);
		        
		        // motor speed test ONLY - do not set during position control
	        	//catapultMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	        }
	        else
	        	System.out.println("ERROR: Catapult motor not initialized!");
		}
	}
	
	public static void autoInit() {
        initCycleTime = Utility.getFPGATime();
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
        
        // calibrate encoders to zero
        catapultMotor.setPosition(0);
        
        // set motor to a position (test only)
        catapultMotor.set(CATAPULT_READY_POSITION);    

        // simple vbus test ONLY - NOT FOR POSITION MODE
        //catapultMotor.set(0.5);
        
		//System.out.println("teleop_init: motor enc = "+ catapultMotor.getEncPosition());

        pressed = false;
	}
	
	public static void teleopPeriodic()
	{		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initCycleTime) < CYCLE_USEC)
			return;
		
		//System.out.println("Read enc position =" + catapultMotor.getEncPosition());
		
		// check for catapult trigger
		if (gamepad.getRawButton(1) && !pressed)
			pressed = true;
				
		System.out.println("motor enc = "+ catapultMotor.getEncPosition());
		
		// if the catapult is not yet fired
		if (!catapultFired) 
		{
			// check for catapult trigger
			if (pressed)
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
		}
		else {
			if (pressed)
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
		
		// reset cycle timer;
		initCycleTime = Utility.getFPGATime();
		
	}

}

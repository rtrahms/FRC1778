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
    private static final int MASTER_CATAPULT_MOTOR_ID = 9;
    private static final int SLAVE_CATAPULT_MOTOR_ID = 10;
    
    private static final int CATAPULT_FIRE_INCREMENT = 1024;
    private static final int CATAPULT_READY_POSITION = (int) (4096.0*5.25 - CATAPULT_FIRE_INCREMENT);
    
    private static CANTalon masterCatapultMotor, slaveCatapultMotor;
    
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
	        
	        System.out.println("Creating motor objects...");
	        
	        // initialize master catapult motor
	        masterCatapultMotor = new CANTalon(MASTER_CATAPULT_MOTOR_ID);
	        
	        if (masterCatapultMotor != null) {
	        	
		        System.out.println("Initializing master catapult motor (position mode)...");
	        	
	        	// set up motor for position control mode
		        masterCatapultMotor.disableControl();
		        masterCatapultMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        masterCatapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        masterCatapultMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        masterCatapultMotor.enableBrakeMode(true);
		        masterCatapultMotor.enableForwardSoftLimit(false);
		        masterCatapultMotor.enableReverseSoftLimit(false);
		        
		        // initialize position and enable control
		        masterCatapultMotor.set(masterCatapultMotor.getPosition());
		        masterCatapultMotor.enableControl();
	        	
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
		        slaveCatapultMotor.disableControl();
		        slaveCatapultMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		        slaveCatapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        slaveCatapultMotor.setPID(2.0, 0, 18.0);     // works pretty well
		        slaveCatapultMotor.enableBrakeMode(true);
		        slaveCatapultMotor.enableForwardSoftLimit(false);
		        slaveCatapultMotor.enableReverseSoftLimit(false);
		        slaveCatapultMotor.reverseOutput(true);		       
		        
		        // initialize master id and enable control
		        slaveCatapultMotor.set(MASTER_CATAPULT_MOTOR_ID);
		        slaveCatapultMotor.enableControl();
	        	
	        	// initializes slave encoder to zero
		        slaveCatapultMotor.setPosition(0);
	        	
	        }
	        else
	        	System.out.println("ERROR: Slave catapult motor not initialized!");
		
		
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
        masterCatapultMotor.setPosition(0);
        
        // set motor to a position (test only)
        masterCatapultMotor.set(CATAPULT_READY_POSITION);    

        // simple vbus test ONLY - NOT FOR POSITION MODE
        //masterCatapultMotor.set(0.5);
        
		//System.out.println("teleop_init: motor enc = "+ masterCatapultMotor.getEncPosition());

        pressed = false;
	}
	
	public static void teleopPeriodic()
	{		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initCycleTime) < CYCLE_USEC)
			return;
		
		//System.out.println("Read enc position =" + masterCatapultMotor.getEncPosition());
		
		// check for catapult trigger
		if (gamepad.getRawButton(1) && !pressed)
			pressed = true;
				
		System.out.println("motor enc = "+ masterCatapultMotor.getEncPosition());
		
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
				masterCatapultMotor.setPosition(0);
				masterCatapultMotor.set(CATAPULT_FIRE_INCREMENT);
				//System.out.println("Fired!  new encoder pos = " + masterCatapultMotor.getPosition());
				
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
		
		// reset cycle timer;
		initCycleTime = Utility.getFPGATime();
		
	}

}

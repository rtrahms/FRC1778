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
    private static final double AUTO_RESET_TIME_US = 1000000;
    
    private static final double CATAPULT_ROTATION_MULTIPLIER = 1.005;
    private static final double CATAPULT_FIRE_INCREMENT = FIRE_ROTATION_BACKOFF_REV*NUM_TICKS_PER_REV*VERSAPLANETARY_RATIO*CHOO_CHOO_GEAR_RATIO;
    private static final double CATAPULT_READY_POSITION = (NUM_TICKS_PER_REV*VERSAPLANETARY_RATIO*CHOO_CHOO_GEAR_RATIO) - CATAPULT_FIRE_INCREMENT;
    private static final double CATAPULT_FULL_REVOLUTION = (NUM_TICKS_PER_REV*VERSAPLANETARY_RATIO*CHOO_CHOO_GEAR_RATIO)*CATAPULT_ROTATION_MULTIPLIER;

    // test values for choo-choo - not for normal operation
    //private static final double CATAPULT_FIRE_INCREMENT = 4096*50;
    //private static final double CATAPULT_READY_POSITION = 4096*50;
    
    private static final int TRIGGER_CYCLE_WAIT_US = 1000000;
    
    private static CANTalon catapultMotor;
    private static boolean catapultKill;
    
    private static boolean pressed;
    private static boolean catapultFired;    
    private static double initTriggerTime;

	// static initializer
	public static void initialize()
	{
		if (!initialized) {

	        leftJoy = new Joystick(LEFT_JOYSTICK_ID);
	        rightJoy = new Joystick(RIGHT_JOYSTICK_ID);
	        	                	        
	        initialized = true;
	        catapultFired = false;  // Assumption!  catapult starts in the high-energy (ready to shoot) state
	        pressed = false;
	        catapultKill = false;    // disable catapult motor if goes wonky
	        
	        System.out.println("Creating catapult motor object...");
	        
	        // initialize master catapult motor
	        catapultMotor = new CANTalon(CATAPULT_MOTOR_ID);
	        
	        if (catapultMotor != null) {
	        
		        System.out.println("Initializing catapult motor (position mode)...");
		        
		        //catapultMotor.clearStickyFaults();
		        
		        // VERY IMPORTANT - CATAPULT MOTOR CAN ONLY BE RUN IN ONE DIRECTION 
		        
	        	// set up motor for position control mode (PID)
		        catapultMotor.enableControl();        // enables PID control
		        catapultMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		        catapultMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		        //catapultMotor.clearIAccum();   // clear error in PID control
		        catapultMotor.reverseOutput(true);  // NEED TO REVERSE OUTPUT - used for closed loops modes only
		        catapultMotor.reverseSensor(true);  // encoder needs to be reversed
		        catapultMotor.setPID(0.1, 0, 0.0);   
		        //catapultMotor.set(catapultMotor.getPosition());   // set motor to current position
		        catapultMotor.setPosition(0);	      // initializes encoder to zero	        	
		        catapultMotor.enableBrakeMode(true);
		        
		        //******* VBUS CONTROL ONLY
		        /*
		        catapultMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		        catapultMotor.enableBrakeMode(true);
		        catapultMotor.setInverted(true);    // NEED TO REVERSE OUTPUT - used for Vbus mode ONLY
		        catapultMotor.reverseSensor(true);
		        catapultMotor.setPosition(0);	      // initializes encoder to zero	        	
		        */     
	        }
	        else
	        	System.out.println("ERROR: Catapult motor not initialized!");		
		}
	}

	public static void autoInit() {
        
        pressed = false;        
        initTriggerTime = Utility.getFPGATime();
        
        // reset on mode entry, if not ready to shoot
        if (!readyToShoot())
        	reset();
	}

	public static void teleopInit() {
		                
        pressed = false;        
        initTriggerTime = Utility.getFPGATime();
        
        // reset on mode entry, if not ready to shoot
        if (!readyToShoot())
        	reset();
	}
	
	public static void teleopPeriodic()
	{
		double currentTime = Utility.getFPGATime();
		
		// if not enough time has passed, no polling allowed!
		if ((currentTime - initTriggerTime) < TRIGGER_CYCLE_WAIT_US)
			return;
				
		if ((leftJoy.getRawButton(8) || rightJoy.getRawButton(8)) && !catapultKill) {
			catapultKill = true;
			catapultMotor.disable();
		}
			
		if (!catapultKill) {
			// check for catapult triggers
			if (leftJoy.getTrigger() && rightJoy.getTrigger() && !pressed)
			{
				pressed = true;
				System.out.println("trigger pressed!");
	
				// reset trigger init time
				initTriggerTime = Utility.getFPGATime();
						
				// shoot catapult and reset to ready state
				if (pressed) {
					shootAndReset();
				}
			}
		}
		
		// quick vbus test - only use when CANDrive not active
		/*
		double speed = 0.0;
		if (leftJoy.getTrigger() && rightJoy.getTrigger()) {
			speed = 0.2;
		}	
		
		System.out.println("catapult motor speed = " + speed + " enc pos = " + catapultMotor.getPosition());
		catapultMotor.set(speed);
		*/
		
	}
	
	public static void disabledInit()
	{
	}

	public static void shoot()
	{
		// if front arm is low enough and catapult ready to shoot...
		if (FrontArmAssembly.isArmLowEnoughForCatapult() && readyToShoot()) {	
			// fire catapult
			catapultMotor.setPosition(0);
			catapultMotor.set(CATAPULT_FIRE_INCREMENT);
			System.out.println("Catapult Fired!  new pos = " + catapultMotor.getPosition());
			
			// set fired flag
			catapultFired = true;
		}
		
		pressed = false;		
	}
	
	public static void reset()
	{		
		// if in need of a reset...
		if (isFired()) {
			// reset catapult motor
			catapultMotor.setPosition(0);
			catapultMotor.set(CATAPULT_READY_POSITION);
			System.out.println("Catapult Reset!  new pos = " + catapultMotor.getPosition());
			
			// TODO: resets are not instantaneous.  From the command to the final reset state takes a couple of seconds.
			// Although this delay may introduce a race condition, we do NOT want to introduce wait/delay states.
			
			// reset fired flag
			catapultFired = false;
		}
		
		pressed = false;
	}

	public static void shootAndReset()
	{
		// if front arm is low enough and catapult is ready to shoot...
		if (FrontArmAssembly.isArmLowEnoughForCatapult() && readyToShoot()) {
			// fire and reset catapult
			catapultMotor.setPosition(0);
			catapultMotor.set(CATAPULT_FULL_REVOLUTION);
			System.out.println("Catapult fired AND reset!  new pos = " + catapultMotor.getPosition());
		
			// TODO: motor movements are not instantaneous.  From the command to the final reset state takes a couple of seconds.
			// Although this delay may introduce a race condition, we do NOT want to introduce wait/delay states.
		
			// reset fired flag
			catapultFired = false;
		}
		
		pressed = false;
	}

	public static boolean isFired()
	{
		return catapultFired;
	}
	
	public static boolean readyToShoot()
	{
		return !catapultFired;
	}
}
package NetworkComm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class GRIPDataComm {
    
    private static NetworkTable table;
	
    private static boolean initialized = false;
        
    private static final double GOAL_X_PX = 267.0;  // where target should be relative to left of image
    private static final double GOAL_Y_PX = 250.0;  // where target should be relative to top of image
    
    // camera parameters
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEIGHT = 480;
   
	// Target Objectives
	private static final double TARGET_X = 320;
	private static final double TARGET_Y = 240;
	private static final double TARGET_AREA = 1000;
	
	// Target accuracy thresholds
	private static final double X_THRESHOLD = 20;
	private static final int Y_THRESHOLD = 20;
	private static final double AREA_THRESHOLD = 20;
	
	// Target Filter/Constraints
	private static final double TARGET_AREA_MIN = 200.0;
	private static final double TARGET_AREA_MAX = 3000.0;
	private static final double TARGET_WIDTH_MIN = 50;
	private static final double TARGET_WIDTH_MAX = 500;
	private static final double TARGET_HEIGHT_MIN = 50;
	private static final double TARGET_HEIGHT_MAX = 300;

	// Arrays of data pulled from the network table published by grip
	private static double[] grip_area;
	private static double[] grip_centerx;
	private static double[] grip_centery;
	private static double[] grip_width;
	private static double[] grip_height;
	
	// Actual target values filtered from grip data
	private static double target_area;
	private static double target_centerx;
	private static double target_centery;
	private static double target_width;
	private static double target_height;

	// Robot drive output
	private static double driveLeft;
	private static double driveRight;
	
	private static int readyTimer = 0;

	// Robot targeting speed (% how fast it moves and turns)
	private static final double DRIVE_SPEED = 0.5;
	
	// Number of loops to perform to guarantee the robot is lined up with the target
	private static final int IS_CENTERED_DELAY = 15;

	private static boolean hasTarget = false;
	private static boolean targetCentered = false;

	//private static PollingThread poller;
	
	public static boolean running = false;
	
	private static final double TARGETX_INIT = 320.0;  
	private static final double TARGETY_INIT = 240.0;  
	
    public static void initialize() {
    	if (!initialized) {
    		
	        table = NetworkTable.getTable("GRIP/myContoursReport");
	        
	        reset();
	        
	        //poller = new PollingThread();
	        //poller.start();
	        	        
       		initialized = true;
    	}
	}
    
    public static void autoInit() {
		target_area = 0.0;
		target_centerx = TARGETX_INIT;
		target_centery = TARGETY_INIT;
		target_width = 0.0;
		target_height = 0.0;
		
		reset();
    }
    public static void reset() {
    					
		driveLeft = 0;
		driveRight = 0;
		
		readyTimer = 0;

		hasTarget = false;    	
		targetCentered = false;
    }
    
    // Network polling thread class
    /*
    static class PollingThread extends Thread {
    	public void run() {
    		int ctr = 0;
    		
	    	while (true) {
	    		if(running) {
	    			
	    			// Default data if network table data pull fails
	    			double[] defaultValue = new double[0];
	    			
	    			// Pull data from grip
	    			grip_area = table.getNumberArray("area", defaultValue);
	    			grip_centerx = table.getNumberArray("centerX", defaultValue);
	    			grip_centery = table.getNumberArray("centerY", defaultValue);
	    			grip_width = table.getNumberArray("width", defaultValue);
	    			grip_height = table.getNumberArray("height", defaultValue);
					
					//System.out.println("pollingThread updating tables! ctr = " + ctr++);
					try {
						Thread.sleep(250);
					} catch (Exception e) {
						System.out.println(e);
					}
	    		}
    		}
    	}
		
    }
    */
    
	public static void stop() {
    	running = false;
    }
    
    public static void start() {
    	running = true;
    }
    
    public static void updateValues() {
        
    	if (!initialized)
    		return;
    	
    	// Default data if network table data pull fails
		double[] defaultValue = new double[0];
		
		// Pull data from grip
		grip_area = table.getNumberArray("area", defaultValue);
		grip_centerx = table.getNumberArray("centerX", defaultValue);
		grip_centery = table.getNumberArray("centerY", defaultValue);
		grip_width = table.getNumberArray("width", defaultValue);
		grip_height = table.getNumberArray("height", defaultValue);

    	// if a valid target exists (one that meets filter criteria)
		if (findTarget()) {
			
			// First, focus on centering X!		
			// neg delta X = actual left of goal, turn LEFT => angular velocity = NEG
			// pos delta X = actual right of goal, turn RIGHT => angular velocity = POS
			double deltaX = (target_centerx - GOAL_X_PX);
			
			if(Math.abs(deltaX) < X_THRESHOLD)  {
				// X is now centered!  Next focus on Y!
				// neg delta Y = actual above goal, move backward => forward velocity = NEG
				// pos delta Y = actual below goal, move forward => forward velocity = POS
				double deltaY = (target_centery - GOAL_Y_PX); 
				
				if(Math.abs(deltaY) < Y_THRESHOLD)  {
					// Both X and Y are centered!
					driveLeft = 0;
					driveRight = 0;
					readyTimer++;
									
					// if we continued to be centered for a reasonable number of iterations
					if(readyTimer >= IS_CENTERED_DELAY) {
						//System.out.println("NetworkCommAssembly: TARGET CENTERED!.... X: " + target_centerx + " Y: " + target_centery + 
						//		"driveLeft = " + driveLeft +
						//		"driveRight = " + driveRight);
						targetCentered = true;
					}
					return;
				}
				else {
					// Set the left and right motors to help center Y
					driveLeft = Math.copySign(DRIVE_SPEED, deltaY);
					driveRight = Math.copySign(DRIVE_SPEED, deltaY);
					targetCentered = false;
					readyTimer = 0;
					//System.out.println("NetworkCommAssembly: CENTERING Y.... X: " + target_centerx + " Y: " + target_centery + 
					//		"driveLeft = " + driveLeft +
					//		"driveRight = " + driveRight);
					return;
				}
			}
			else {
				// Set the left and right motors to help center X
				driveLeft = Math.copySign(DRIVE_SPEED, deltaX);
				driveRight = Math.copySign(DRIVE_SPEED, -deltaX);
				targetCentered = false;
				readyTimer = 0;
				
				//System.out.println("NetworkCommAssembly: CENTERING X.... X: " + target_centerx + " Y: " + target_centery + 
				//		"driveLeft = " + driveLeft +
				//		"driveRight = " + driveRight);
				return;
			}		
		}
		else {
			// no target found!  Reset targeting params
			reset();
		}
		
		Timer.delay(0.02);
    }
        
	// Returns the value for the left side drivetrain
	public static double getLeftDriveValue() {
		return driveLeft;
	}
	
	// Returns the value for the right side drivetrain
	public static double getRightDriveValue() {
		return driveRight;
	}
	
	// Returns true if the target is visible, returns false otherwise
	public static boolean hasTarget() {
		return hasTarget;
	}
	
	// Returns true if the catapult is ready to shoot, returns false otherwise
	public static boolean targetCentered() {
		return targetCentered;
	}
   
    
	// Filters data from grip and determines if the target is visible
	// Writes target data to target variables if the target is found.
	private static boolean findTarget() {
		
		// If none of the data received from grip is null
		if (grip_area != null && grip_centerx != null && grip_centery != null && grip_width != null && grip_height != null) {
			// If none of the arrays have an impossible or zero length
			if (grip_area.length > 0 && grip_centerx.length > 0 && grip_centery.length > 0 && grip_width.length > 0 && grip_height.length > 0) {
				// Loop through all the data received from grip
				/*
				for (int n = 0; n < grip_area.length; n++) {
					
					// If the data matches the targets area criteria
					if (grip_area[n] >= TARGET_AREA_MIN && grip_area[n] <= TARGET_AREA_MAX) {
						// If the data matches the targets width critera
						if (grip_width[n] >= TARGET_WIDTH_MIN && grip_width[n] <= TARGET_WIDTH_MAX) {
							// If the data matches the targets height criteria
							if (grip_height[n] >= TARGET_HEIGHT_MIN && grip_height[n] <= TARGET_HEIGHT_MAX) {
							
								// Set the target variables to the found targets values
								
							}
						}

					}
					
				}
				*/
				
				target_area = grip_area[grip_area.length-1];
				target_centerx = grip_centerx[grip_centerx.length-1];
				target_centery = grip_centery[grip_centery.length-1];
				target_width = grip_width[grip_width.length-1];
				target_height = grip_height[grip_height.length-1];
				
				hasTarget = true;
				
				return true;
			}
		}
		
		// no target found that matches criteria, return false
		return false;
		
	}
}

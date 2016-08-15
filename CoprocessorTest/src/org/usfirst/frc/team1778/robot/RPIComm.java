package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RPIComm {
    
    private static NetworkTable table;
	
    private static boolean initialized = false;
        
	//private static PollingThread poller;
	
	public static boolean running = false;
	
	private static double targets, targetX, targetY;
	
    public static void initialize() {
    	if (!initialized) {
    		
	        table = NetworkTable.getTable("RPIComm/Data_Table");
	        	        
	        //poller = new PollingThread();
	        //poller.start();
	        	        
       		initialized = true;
    	}
	}
    
    public static void autoInit() {
		
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
		double defaultDoubleVal = 0.0;
		String defaultStrVal = "";
		
		// Pull data from grip
		targets = table.getNumber("targets", defaultDoubleVal);
		targetX = table.getNumber("targetX", defaultDoubleVal);
		targetY = table.getNumber("targetY", defaultDoubleVal);

		if (targets > 0) {
			
			// Debug only - print out values read from network table
			//System.out.println("Time_ms= " + System.currentTimeMillis() + " targets = " + targets + ", coord = (" + targetX + ", " + targetY + ")");
			
			// do something with position information
		
		}
		Timer.delay(0.02);
    }
    
}

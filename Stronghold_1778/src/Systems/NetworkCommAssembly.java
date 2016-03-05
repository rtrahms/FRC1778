package Systems;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkCommAssembly {
    
    private static NetworkTable table;
	
    private static boolean initialized = false;
    private static final double FOCAL = 824.4; // Focal length of lens
    private static final double WIDTH = 7.5;//20.0;  // Width of target in inches
    private static final double HEIGHT = 5.5;//90.0; // Height of target in inches
    private static final int IMAGE_WIDTH = 640;
    private static final double IMAGE_CENTER_X = IMAGE_WIDTH/2.0;
    private static final int IMAGE_HEIGHT = 480;
    private static final double TARGET_DIST = 95.0; //Distance to get to in inches
    private static final double MIN_TARGET_AREA = 10.0;  // threshold target area to process
    
    private static final double FORWARD_THRESHOLD = 0.05;
    private static final double ANGULAR_THRESHOLD = 0.1;
    
	private static double[] area;
	private static double[] centerx;
	private static double[] centery;
	private static double[] height;
	private static double[] width;
	    
	private static double forwardVelocity, angularVelocity;
	private static boolean hasTarget = false;
	
    public static void initialize() {
    	if (!initialized) {
    		
	        table = NetworkTable.getTable("GRIP/myContoursReport");
	        
	        reset();
	        
       		initialized = true;
    	}
	}
    
    public static void reset() {
		forwardVelocity = 0;
		angularVelocity = 0;
		hasTarget = false;    	
    }
    
    public static void updateValues() {
        
    	if (!initialized)
    		return;
    	
		double[] defaultValue = {0.0};
		area = table.getNumberArray("area",defaultValue);
		centerx = table.getNumberArray("centerX",defaultValue);
		centery = table.getNumberArray("centerY",defaultValue);
		height = table.getNumberArray("height",defaultValue);
		width = table.getNumberArray("width",defaultValue);
		
		double targetArea = 0.0;
		double targetX = 0.0;
		double targetY = 0.0;
		double targetWidth = 0.0;
		double targetHeight = 0.0;
		double distance = 0.0;
				
		if(area.length != 0) {
			targetArea = area[area.length-1];
			targetX = centerx[centerx.length-1];
			targetY = centery[centery.length-1];
			targetHeight = height[height.length-1];
			targetWidth = width[width.length-1];
			distance = (WIDTH*FOCAL)/targetWidth;
			hasTarget = true;

			// calculate forward velocity
			double distanceDelta = distance-TARGET_DIST;
			if (distanceDelta != 0.0) {
				forwardVelocity = Math.pow(Math.abs(distanceDelta)/(TARGET_DIST*4),0.85)*(distanceDelta/Math.abs(distanceDelta));
				if(Math.abs(forwardVelocity) < FORWARD_THRESHOLD) 
					forwardVelocity = 0.0;
			}

			// calculate angular velocity
			double lateralDelta = (targetX-IMAGE_CENTER_X)/IMAGE_CENTER_X;		
			if (lateralDelta != 0) {
				angularVelocity = Math.pow(Math.abs(lateralDelta),0.85)*(lateralDelta/Math.abs(lateralDelta));
				if(Math.abs(angularVelocity) < ANGULAR_THRESHOLD) 
					angularVelocity = 0.0;
			}
								
			/*
			System.out.println("NetworkCommAssembly: X: " + targetX + " Y: " + targetY + " area: " + targetArea + 
			   										" width: " + targetWidth + " height:" + targetHeight);
			*/
			
			//System.out.println("NetworkCommAssembly: hasTarget: " + hasTarget + " forwardVel: " + forwardVelocity + " angularVel: " + angularVelocity);
		}
		else {
			// no target found!  Reset targeting params
			reset();
		}
		
    }
    
    public static double getForwardVelocity()
    {
    	return forwardVelocity;
    }
    
    public static double getAngularVelocity()
    {
    	return angularVelocity;
    }
    
    public static boolean hasTarget()
    {
    	return hasTarget;
    }
   
    public static int findLargestTarget()
    {
    	int targetIndex = -1;
    	double largestArea = -1.0;
    	
    	// if list is empty, return -1 (no target found)
    	if (area.length == 0)
    		return -1;
    	
    	// check each recognized target
    	for (int i=0; i<area.length; i++)
    	{	
    		// if we've found an area larger than current
    		if (area[i] > largestArea)
    		{
    			// mark it as largest
    			targetIndex = i;
    			largestArea = area[i];
    		}
    	}
    	
    	// if largest target is not big enough, return -1
    	if (largestArea < MIN_TARGET_AREA)
    		return -1;
    	
    	// return index of largest area found
    	return targetIndex;
    }
    
    /*
    public static double getTargetCenterX(int index)
    {
    	if (centerx.length == 0)
    		return -1.0;
    	else
    		return centerx[index];
    }
    
    public static double getTargetCenterY(int index)
    {
    	if (centery.length == 0)
    		return -1.0;
    	else
    		return centery[index];
    }
    */

}

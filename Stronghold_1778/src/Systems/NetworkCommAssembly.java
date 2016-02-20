package Systems;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkCommAssembly {
    
    private static NetworkTable table;
	
    private static boolean initialized = false;
    private static final double FOCAL = 824.4; // Focal length of lens
    private static final double WIDTH = 20.0;  // Width of target in inches
    private static final double HEIGHT = 90.0; // Height of target in inches
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEIGHT = 480;
    private static final double TARGET_DIST = 48.0; //Distance to get to in feet
    
    public static void initialize() {
    	if (!initialized) {
    		
	        table = NetworkTable.getTable("GRIP/myContoursReport");
	        
    		initialized = true;
    	}
	}
    
    public static void updateValues() {
        
    	if (!initialized)
    		return;
    	
		double[] defaultValue = {0.0};
		double[] area = table.getNumberArray("area",defaultValue);
		double[] centerx = table.getNumberArray("centerX",defaultValue);
		double[] centery = table.getNumberArray("centerY",defaultValue);
		double[] height = table.getNumberArray("height",defaultValue);
		double[] width = table.getNumberArray("width",defaultValue);
		double ta = 0.0;
		double tx = 0.0;
		double ty = 0.0;
		double tw = 0.0;
		double th = 0.0;
		double distance = 0.0;
		double motorLeft = 0.0;
		double motorRight = 0.0;
		
		boolean hasTarget = false;
		/*
		if(area.length != 0) {
			ta = area[area.length-1];
			tx = centerx[centerx.length-1];;
			ty = centery[centery.length-1];
			th = height[height.length-1];
			tw = width[width.length-1];
			distance = (WIDTH*FOCAL)/tw;
			hasTarget = true;
			double dd = distance-TARGET_DIST;
			double fp = Math.pow(Math.abs(dd)/100.0,0.5)*(dd/Math.abs(dd));
			double xn = (tx-IMAGE_WIDTH/2.0)/(IMAGE_WIDTH/2.0);
			double tp = Math.pow(Math.abs(xn),0.5)*(xn/Math.abs(xn));
			//System.out.println(fp+", "+tp);
			motorLeft = fp+tp;
			motorRight = fp-tp;
			System.out.println("MotorLeft="+motorLeft+" MotorRight="+motorRight);
		}
		*/
		
		//System.out.println(distance);
		//System.out.println("X:"+tx+", Y:"+ty+", area:"+ta+", width:"+tw+", height:"+th);
    }

}

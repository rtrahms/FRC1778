package Systems;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkCommAssembly {
    
    private static NetworkTable table;
    
	private static double[] area;
	private static double[] centerx;
	private static double[] centery;
	private static double[] height;
	private static double[] width;
	
    private static boolean initialized = false;
    
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
		area = table.getNumberArray("area",defaultValue);
		centerx = table.getNumberArray("centerX",defaultValue);
		centery = table.getNumberArray("centerY",defaultValue);
		height = table.getNumberArray("height",defaultValue);
		width = table.getNumberArray("width",defaultValue);
		
		//System.out.println("X:"+centerx+", Y:"+centery+", area:"+area+", width:"+width+", height:"+height);

    }
    
    public static double getTargetCenterX()
    {
    	if (centerx.length == 0)
    		return -1.0;
    	else
    		return centerx[0];
    }
    
    public static double getTargetCenterY()
    {
    	if (centery.length == 0)
    		return -1.0;
    	else
    		return centery[0];
    }

}

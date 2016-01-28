package Systems;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkCommAssembly {
    
    private static NetworkTable table;
    
	private static double area;
	private static double centerx;
	private static double centery;
	private static double height;
	private static double width;

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
    	
		double defaultValue = 0.0;
		area = table.getNumber("area",defaultValue);
		centerx = table.getNumber("centerX",defaultValue);
		centery = table.getNumber("centerY",defaultValue);
		height = table.getNumber("height",defaultValue);
		width = table.getNumber("width",defaultValue);
		
		System.out.println("X:"+centerx+", Y:"+centery+", area:"+area+", width:"+width+", height:"+height);

    }

}

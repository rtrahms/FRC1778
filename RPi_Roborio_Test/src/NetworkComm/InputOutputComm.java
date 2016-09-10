package NetworkComm;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class InputOutputComm {
	
    private static NetworkTable table;
    
    public static enum LogTable { kMainLog, kRPICommLog, kDriveLog };
	
	private static boolean initialized = false;
	
    public static void initialize() {
    	if (!initialized) {
    		
	        table = NetworkTable.getTable("InputOutput1778/DataTable");
	        	        	        
       		initialized = true;
    	}
    }
    
    public static void putBoolean(LogTable log, String key, boolean value) {
    	
    	double numVal = 0;
    	
    	if (value == true) numVal = 1.0;
    	
    	if (table != null)
    		table.putNumber(key,numVal);
    	else
    		System.out.println("No network table to write to!!");
    }
    
    public static void putDouble(LogTable log, String key, double value) {
    	if (table != null)
    		table.putNumber(key,value);
    	else
    		System.out.println("No network table to write to!!");
    }
    
    public static void putString(LogTable log, String key, String outputStr) {
    	if (table != null)
    		table.putString(key, outputStr);
    	else
    		System.out.println("No network table to write to!!");
    }
    
}

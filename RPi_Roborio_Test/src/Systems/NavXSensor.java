
package Systems;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

public class NavXSensor {
	private static boolean initialized = false;
	
	private static AHRS ahrs;
	
	public static class Angles {
		float roll = 0f;
		float pitch = 0f;
		float yaw = 0f;
	}
			
	public static void initialize()
	{
		if (!initialized) {
				
			System.out.println("NavXSensor::initialize() called...");
			
			try {
				ahrs = new AHRS(SPI.Port.kMXP);     
			} catch (RuntimeException ex ) {
	            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
	        }

			reset();
			
			initialized = true;
		}
	}
	
	public static void reset()
	{
		System.out.println("NavXSensor::reset called!");
		
		if (ahrs != null) 
		{
			ahrs.reset();
			ahrs.resetDisplacement();
			ahrs.zeroYaw();
			
			// allow zeroing to take effect
			Timer.delay(0.1);
		}
	}

	public static boolean isConnected() {
		if (ahrs != null) {
			return ahrs.isConnected();
		}
		
		return false;
	}
	
	public static boolean isCalibrating() {
		if (ahrs != null) {
			return ahrs.isCalibrating();
		}
		
		return false;
	}
	
	public static Angles getAngles()
	{
		Angles angles = new Angles();
		
		if (ahrs != null) {
			angles.roll = ahrs.getRoll();	
			angles.pitch = ahrs.getPitch();	
			angles.yaw = ahrs.getYaw();	
		}			
		
		return angles;
	}
	
	public static float getYaw() 
	{
		float yaw = 0f;
		
		if (ahrs != null) {
			yaw = ahrs.getYaw();	
		}			
		
		return yaw;
		
	}
	
}

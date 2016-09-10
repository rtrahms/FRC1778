
package Systems;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

public class NavXSensor {
	private static boolean initialized = false;
	
	private static AHRS ahrs;
		
	//private static final double GYRO_SENSITIVITY = 0.007;
	
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
			ahrs.reset();
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
	
	public static double getAngle()
	{
		double angle = 0;
		
		if (ahrs != null) {
			angle = ahrs.getAngle();
		
			//System.out.println("NavXSensor::getAngle(): angle = " + angle);
			
			// slow down reading of sensor
			Timer.delay(0.02);
		}
		//else
		//	System.out.println("NavXSensor::getAngle(): ahrs object is null! ");
			
		
		return angle;
	}

}

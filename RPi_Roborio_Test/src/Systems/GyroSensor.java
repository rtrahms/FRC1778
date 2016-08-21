
package Systems;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;

public class GyroSensor {
	private static boolean initialized = false;
	
	private static final int GYRO_CHANNEL = 0;
	private static AnalogGyro myGyro;
	
	// settings for the Analog Devices ADW22307 Gyro
	// 7 mV/deg/s, per the data sheet
	private static final double GYRO_SENSITIVITY = 0.007;
	
	public static void initialize()
	{
		if (!initialized) {
			
	        myGyro = new AnalogGyro(GYRO_CHANNEL);
	        myGyro.setSensitivity(GYRO_SENSITIVITY);
	        
	        myGyro.calibrate();

			initialized = true;
		}
	}
	
	public static void reset()
	{
		System.out.println("GyroSensor::reset called!");
		
		if (myGyro != null)
			myGyro.reset();
	}
	
	public static double getAngle()
	{
		double angle = 0;
		
		if (myGyro != null) {
			angle = myGyro.getAngle();
		
			// slow down reading of sensor
			Timer.delay(0.02);
		}
		
		return angle;
	}

}


package Systems;
import edu.wpi.first.wpilibj.AnalogGyro;

public class GyroSensor {
	private static boolean initialized = false;
	
	private static final int GYRO_CHANNEL = 0;
	private static AnalogGyro myGyro;
	
	public static void initialize()
	{
		if (!initialized) {
			
	        myGyro = new AnalogGyro(GYRO_CHANNEL);

			initialized = true;
		}
	}
	
	public static void reset()
	{
		if (myGyro != null)
			myGyro.reset();
	}
	
	public static double getAngle()
	{
		if (myGyro != null)
			return myGyro.getAngle();
		else
			return 0.0;
	}

}

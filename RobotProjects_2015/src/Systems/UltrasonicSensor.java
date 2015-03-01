package Systems;

import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor {

	private static boolean initialized = false;
	
	private static Ultrasonic ultra;
	private static final int PING_CHANNEL = 0;
	private static final int ECHO_CHANNEL = 1;
	
	private static final double MM_TO_SMOOT = 0.000588;
		
	public static void initialize()
	{
		if (!initialized)
		{
			ultra = new Ultrasonic(PING_CHANNEL, ECHO_CHANNEL);
			//ultra.setEnabled(true);
			ultra.setAutomaticMode(true);
			initialized = true;
		}
	}
	
	public static double getRangeMM()
	{
		//ultra.ping();
		return ultra.getRangeMM();
	}
	
	public static double getRangeInch()
	{
		//ultra.ping();
		return ultra.getRangeInches();
	}
	
	public static double getRangeSmoot()
	{
		//ultra.ping();
		return (ultra.getRangeMM() * MM_TO_SMOOT);
	}
}

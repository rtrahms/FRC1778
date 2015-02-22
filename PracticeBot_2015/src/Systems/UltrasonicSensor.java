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
			initialized = true;
		}
	}
	
	public static double getRangeMM()
	{
		return ultra.getRangeMM();
	}
	
	public static double getRangeInch()
	{
		return ultra.getRangeInches();
	}
	
	public static double getRangeSmoot()
	{
		return (ultra.getRangeMM() * MM_TO_SMOOT);
	}
}

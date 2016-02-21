package Systems;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Utility;

public class UltrasonicSensor {

	private static boolean initialized = false;
	
	private static Ultrasonic ultra;
	private static final int PING_CHANNEL = 3;
	private static final int ECHO_CHANNEL = 4;
	
	private static final double BALL_PRESENT_THRESHOLD_INCH = 4.0;
	private static final double ULTRASONIC_MSG_CYCLE = 250000;
	
	private static final double MM_TO_SMOOT = 0.000588;
		
	private static double rangeInches;
	private static boolean ballIsPresent;
	
	private static double initTimer;
	
	public static void initialize()
	{
		if (!initialized)
		{
			ultra = new Ultrasonic(PING_CHANNEL, ECHO_CHANNEL);
			//ultra.setEnabled(true);
			ultra.setAutomaticMode(true);
			rangeInches = 100000;
			ballIsPresent = false;
			initialized = true;
		}
	}
	
	public static void teleopInit()  {
		initTimer= Utility.getFPGATime();
	}
	
	public static void teleopPeriodic() {
		
		double currentTime = Utility.getFPGATime();

		if ((currentTime - initTimer) < ULTRASONIC_MSG_CYCLE)
			return;

		rangeInches = ultra.getRangeInches();
		//System.out.println("Ultrasonic value (in) = " + rangeInches);
		
		if (!ballIsPresent && (rangeInches < BALL_PRESENT_THRESHOLD_INCH)) {
			//System.out.println("BALL PRESENT!");
			ballIsPresent = true;
			RioDuinoAssembly.SendString("ballYes");
			
			// reset msg timer
			initTimer = Utility.getFPGATime();
		}
		else if (ballIsPresent && (rangeInches > BALL_PRESENT_THRESHOLD_INCH)) {
			//System.out.println("BALL GONE!");
			ballIsPresent = false;
			RioDuinoAssembly.SendString("ballNo");
			
			// reset msg timer
			initTimer = Utility.getFPGATime();
		}
		
	}
	
	public static boolean isBallPresent() {
		return ballIsPresent;
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

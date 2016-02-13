package Systems;

import edu.wpi.first.wpilibj.I2C;

public class RioDuinoAssembly {
	
	public static enum Color { Black, Red, Blue, Yellow, Orange, Green, Purple, Grey};
	
	private static boolean initialized = false;
	private static Color teamColor;

	private static I2C i2cBus;
	
	public static void initialize() {
		i2cBus = new I2C(I2C.Port.kMXP, 4);				
	}
	
	public static void setTeamColor(Color col) {
		teamColor = col;
		sendTeamColor(teamColor);
	}
	
	public static void setTeamColor() {
		sendColor(teamColor);
	}
		
	public static void autonomousInit() {
		SendString("colorGreen");
		SendString("autoInit");
	}
	
	public static void teleopInit() {
		SendString("colorGreen");
		SendString("teleopInit");
	}
	
	public static void testInit() {
		SendString("colorOrange");		
		SendString("testInit");
	}
	
	public static void disabledInit() {
		sendTeamColor(teamColor);
		SendString("disabledInit");
	}
		
	public static void sendColor(Color col)
	{
		switch (col) {
		case Red:
			SendString("colorRed");
			break;
		case Blue:
			SendString("colorBlue");
			break;
		case Yellow:
			SendString("colorYellow");
			break;
		case Green:
			SendString("colorGreen");
			break;
		case Purple:
			SendString("colorPurple");
			break;
		case Grey:
			SendString("colorGrey");
			break;
		case Black:
		default:
			SendString("Robot Black");
			break;
		}
	}
	
	private static void sendTeamColor(Color col)
	{
		switch (col) {
		case Red:
			SendString("teamRed");
			break;
		case Blue:
		default:
			SendString("teamBlue");
			break;
		}
	}
	
	public static void SendStateChange(char state)
	{
		i2cBus.write(0x02, state);
	}
	
	public static void SendString(String writeStr)
	{
		char[] CharArray = writeStr.toCharArray();
		byte[] WriteData = new byte[CharArray.length];
		for (int i = 0; i < CharArray.length; i++) {
			WriteData[i] = (byte) CharArray[i];
		}
		i2cBus.transaction(WriteData, WriteData.length, null, 0);

	}
}

package Systems;

import edu.wpi.first.wpilibj.I2C;

public class RioDuinoAssembly {
	
	public static enum Color { Black, Red, Blue, Yellow, Green, Purple};
	
	private static boolean initialized = false;
	private static Color teamColor;

	private static I2C i2cBus;
	
	public static void initialize() {
		i2cBus = new I2C(I2C.Port.kMXP, 4);		
		
		// turn on camera LED ring
		SendString("Camera Ring On");
	}
	
	public static void setTeamColor(Color col) {
		teamColor = col;
		sendColor(teamColor);
	}
	
	public static void setTeamColor() {
		sendColor(teamColor);
	}
		
	public static void autonomousInit() {
	}
	
	public static void teleopInit() {
	}
	
	public static void teleopPeriodic() {		
	}
	
	public static void sendColor(Color col)
	{
		switch (col) {
		case Red:
			SendString("Robot Red");
			break;
		case Blue:
			SendString("Robot Blue");
			break;
		case Yellow:
			SendString("Robot Yellow");
			break;
		case Green:
			SendString("Robot Green");
			break;
		case Purple:
			SendString("Robot Purple");
			break;
		case Black:
		default:
			SendString("Robot Black");
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

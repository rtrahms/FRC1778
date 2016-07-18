package Systems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

public class RioDuinoAssembly {
	
	public static enum Color { Black, Red, Blue, Yellow, Orange, Green, Purple, Grey};
	
	// particulars about the team number and color
	private static DriverStation.Alliance dsTeamColor;
	private static int dsTeamLocation;

	private static boolean initialized = false;
	private static Color teamColor;

	private static I2C i2cBus;
	
	public static void initialize() {
		if (!initialized)
		{
			i2cBus = new I2C(I2C.Port.kMXP, 4);	
			initialized = true;
			
			setTeamColor();
		}
	}
	
	public static void setTeamColor(Color col) {
		teamColor = col;
		sendColor(teamColor);
	}
	
	public static void setTeamColor() {
		dsTeamColor = DriverStation.getInstance().getAlliance();
		dsTeamLocation = DriverStation.getInstance().getLocation();

		 if (dsTeamColor == DriverStation.Alliance.Red)
			 teamColor = RioDuinoAssembly.Color.Red; 
		 else
			 teamColor = RioDuinoAssembly.Color.Blue;

		 sendTeamColor(teamColor);
	}
		
	public static void autonomousInit() {
	
		setTeamColor();
		SendString("colorGreen");
		SendString("autoInit");
	}
	
	public static void teleopInit() {
		
		setTeamColor();
		SendString("colorGreen");
		SendString("teleopInit");
	}
	
	public static void testInit() {

		setTeamColor();		
		SendString("colorOrange");		
		SendString("testInit");
	}
	
	public static void disabledInit() {
		if (!initialized)
			initialize();
		
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
			SendString("colorBlack");
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
		if (!initialized)
			return;
		
		i2cBus.write(0x02, state);
	}
	
	public static void SendString(String writeStr)
	{
		
		if (!initialized)
			return;
		
		char[] CharArray = writeStr.toCharArray();
		byte[] WriteData = new byte[CharArray.length];
		for (int i = 0; i < CharArray.length; i++) {
			WriteData[i] = (byte) CharArray[i];
		}
		i2cBus.transaction(WriteData, WriteData.length, null, 0);

	}
}

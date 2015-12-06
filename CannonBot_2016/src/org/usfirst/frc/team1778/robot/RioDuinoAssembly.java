package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.I2C;

public class RioDuinoAssembly {

	private I2C i2cBus;
	
	public RioDuinoAssembly()
	{
		i2cBus = new I2C(I2C.Port.kMXP, 4);
	}
	
	public void SendStateChange(char state)
	{
		i2cBus.write(0x02, state);
	}
	
	public void SendString(String writeStr)
	{
		char[] CharArray = writeStr.toCharArray();
		byte[] WriteData = new byte[CharArray.length];
		for (int i = 0; i < CharArray.length; i++) {
			WriteData[i] = (byte) CharArray[i];
		}
		i2cBus.transaction(WriteData, WriteData.length, null, 0);

	}
}

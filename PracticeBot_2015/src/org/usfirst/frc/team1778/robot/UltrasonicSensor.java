package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor {

	private Ultrasonic ultra;
	private final int PING_CHANNEL = 0;
	private final int ECHO_CHANNEL = 1;
	
	private final double MM_TO_SMOOT = 0.000588;
	
	public UltrasonicSensor() {
		ultra = new Ultrasonic(PING_CHANNEL, ECHO_CHANNEL);
	}
	
	public double getRangeMM()
	{
		return ultra.getRangeMM();
	}
	
	public double getRangeInch()
	{
		return ultra.getRangeInches();
	}
	
	public double getRangeSmoot()
	{
		return (ultra.getRangeMM() * MM_TO_SMOOT);
	}
}

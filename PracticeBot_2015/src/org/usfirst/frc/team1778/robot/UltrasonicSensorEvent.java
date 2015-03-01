package org.usfirst.frc.team1778.robot;

public class UltrasonicSensorEvent extends Event {
	
	private UltrasonicSensor ultra1778;
	private double distanceMM;
	
	public UltrasonicSensorEvent()
	{
		ultra1778 = new UltrasonicSensor();
		this.distanceMM = 0.0;
	}
	
	public UltrasonicSensorEvent(double distanceMM)
	{
		ultra1778 = new UltrasonicSensor();
		this.distanceMM = distanceMM;		
	}
	
	// overloaded initialize method
	public void initialize()
	{
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		if (ultra1778.getRangeMM() <= distanceMM)
			return true;
		
		return false;
	}
}

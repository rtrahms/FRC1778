package pwmStateMachine;

import Systems.GyroSensor;

// event triggered when gyro gets to a certain predetermined angle
public class GyroAngleEvent extends Event {
	
	private double angleToTurn = 0.0;
	private double accuracyDeg = 2.0;
	
	public GyroAngleEvent(double angleToTurn)
	{
		this.angleToTurn = angleToTurn;
		GyroSensor.initialize();
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("GyroAngleEvent initialized!");
		GyroSensor.reset();
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		//System.out.println("angle = " + myGyro.getAngle() + " angleToTurn = " + angleToTurn);
		if (Math.abs(GyroSensor.getAngle() - angleToTurn) < accuracyDeg)
		{
			System.out.println("GyroAngleEvent triggered!");
			return true;
		}
		
		return false;
	}

}

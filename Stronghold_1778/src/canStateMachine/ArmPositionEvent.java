package canStateMachine;

import Systems.FrontArmAssembly;
import Systems.GyroSensor;

// event triggered when gyro gets to a certain predetermined angle
public class ArmPositionEvent extends Event {
	
	private double targetPos;
	private final double accuracyTicks = 4096;
	
	public ArmPositionEvent(double targetPos)
	{
		this.targetPos = targetPos;
		FrontArmAssembly.initialize();
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("GyroAngleEvent initialized!");
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		double currPos = FrontArmAssembly.getPosition();
		
		//System.out.println("currPos = " + currPos + " targetPos = " + targetPos);
		if (Math.abs(currPos - targetPos) < accuracyTicks)
		{
			System.out.println("ArmPositionEvent triggered!");
			return true;
		}
		
		return false;
	}

}

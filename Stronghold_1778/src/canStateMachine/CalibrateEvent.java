package canStateMachine;

import Systems.AutoShooterAssembly;

public class CalibrateEvent extends Event {
	private boolean calibratedCondition;
	
	public CalibrateEvent(boolean calibrateCondition)
	{
		this.calibratedCondition = calibrateCondition;
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("CalibrateEvent initialized!");
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{				
		boolean calState = AutoShooterAssembly.isCalibrated();
		
		if (calibratedCondition == calState)
			return true;
		
		return false;
	}

}

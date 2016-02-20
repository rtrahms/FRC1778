package canStateMachine;

import Systems.CatapultAssembly;

public class CatapultEvent extends Event {
	
	private boolean firedCondition;
	
	public CatapultEvent(boolean firedCondition)
	{
		this.firedCondition = firedCondition;
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("CatapultEvent initialized!");
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{		
		boolean catapultState = CatapultAssembly.isFired();
		
		if (firedCondition == catapultState)
			return true;
		
		return false;
	}
}

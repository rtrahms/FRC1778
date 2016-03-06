package canStateMachine;

import Systems.CatapultAssembly;

public class ShootAndResetCatapultState extends AutoState {
	
	public ShootAndResetCatapultState() {
		this.name = "<Shoot and Reset Catapult State>";		
		
		CatapultAssembly.initialize();
	}
	
	public ShootAndResetCatapultState(String name)
	{
		this.name = name;
		
		CatapultAssembly.initialize();
	}

	// state entry
	public void enter() {
				
		// do some shooty and resety stuff (call once only)
		CatapultAssembly.shootAndReset();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// no periodic call needed
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		
		// cleanup base class
		super.exit();
	}

}

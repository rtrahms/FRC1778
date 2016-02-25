package canStateMachine;

import Systems.CatapultAssembly;

public class ResetCatapultState extends AutoState {
	public ResetCatapultState() {
		this.name = "<Reset Catapult State>";		
		
		// do some initialization
		CatapultAssembly.initialize();
	}
	
	public ResetCatapultState(String name)
	{
		this.name = name;
		
		// do some initialization
		CatapultAssembly.initialize();
	}

	// state entry
	public void enter() {
				
		// reset the catapult if necessary
		if (CatapultAssembly.isFired())
			CatapultAssembly.reset();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// no periodic needed
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		
		// cleanup base class
		super.exit();
	}

}

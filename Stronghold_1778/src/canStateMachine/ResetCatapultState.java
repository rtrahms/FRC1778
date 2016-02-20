package canStateMachine;

import Systems.CatapultAssembly;

public class ResetCatapultState extends AutoState {
	public ResetCatapultState() {
		this.name = "<Reset Catapult State>";		
		
		// do some elevator initialization
		CatapultAssembly.initialize();
	}
	
	public ResetCatapultState(String name)
	{
		this.name = name;
		
		// do some elevator initialization
		CatapultAssembly.initialize();
	}

	// state entry
	public void enter() {
				
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// reset the catapult
		CatapultAssembly.reset();
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		// do some elevator cleanup
		CatapultAssembly.autoStop();
		
		// cleanup base class
		super.exit();
	}

}

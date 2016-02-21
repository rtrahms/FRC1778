package canStateMachine;

import Systems.CatapultAssembly;

public class ShootCatapultState extends AutoState {
	
	public ShootCatapultState() {
		this.name = "<Shoot Catapult State>";		
		
		CatapultAssembly.initialize();
	}
	
	public ShootCatapultState(String name)
	{
		this.name = name;
		
		CatapultAssembly.initialize();
	}

	// state entry
	public void enter() {
				
		// do some shooty stuff (call once only)
		CatapultAssembly.shoot();
		
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

package canStateMachine;

import Systems.CatapultAssembly;

public class ShootCatapultState extends AutoState {
	private boolean autoTargeting = false;
	
	public ShootCatapultState() {
		this.name = "<Shoot Catapult State>";		
		
		CatapultAssembly.initialize();
	}
	
	public ShootCatapultState(String name, boolean autoTargeting)
	{
		this.name = name;
		this.autoTargeting = autoTargeting;
		
		CatapultAssembly.initialize();
	}

	// state entry
	public void enter() {
		// do some elevator initialization
		
		CatapultAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some shooty stuff
		CatapultAssembly.shoot();
		
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

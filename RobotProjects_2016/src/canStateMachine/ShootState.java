package canStateMachine;

import Systems.CatapultAssembly;

public class ShootState extends AutoState {
	private boolean autoTargeting = false;
	
	public ShootState() {
		this.name = "<Shoot State>";		
		
		CatapultAssembly.initialize();
	}
	
	public ShootState(String name, boolean autoTargeting)
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
		
		// do some elevator stuff
		CatapultAssembly.autoPeriodic(autoTargeting);
		
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

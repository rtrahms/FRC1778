package canStateMachine;

import Systems.FrontArmAssembly;

public class ConveyerStartState extends AutoState {
	
	private boolean inDirection = true;
	
	public ConveyerStartState() {
		this.name = "<Conveyer Start State>";		
		
		// do some initialization
		FrontArmAssembly.initialize();
	}
	
	public ConveyerStartState(String name, boolean inDirection)
	{
		this.name = name;
		this.inDirection = inDirection;
		
		// do some initialization
		FrontArmAssembly.initialize();
	}

	// state entry
	public void enter() {
				
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// run the conveyer
		FrontArmAssembly.startConveyer(inDirection);
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		
		// stop the conveyer
		FrontArmAssembly.stopConveyer();
		
		// cleanup base class
		super.exit();
	}

}

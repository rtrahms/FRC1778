package canStateMachine;

import Systems.FrontArmAssembly;

public class ArmMoveState extends AutoState {
	
	private double speed = 0.0;
	
	public ArmMoveState() {
		this.name = "<Arm Move State>";		
		
		// do some initialization
		FrontArmAssembly.initialize();
	}
	
	public ArmMoveState(String name, double speed)
	{
		this.name = name;
		this.speed = speed;
		
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
		FrontArmAssembly.startArm(speed);
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		
		// stop the conveyer
		FrontArmAssembly.stopArm();
		
		// cleanup base class
		super.exit();
	}

}

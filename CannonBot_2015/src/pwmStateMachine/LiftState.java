package pwmStateMachine;

import Systems.CANDriveAssembly;
import Systems.ElevatorAssembly;
import Systems.PWMDriveAssembly;

public class LiftState extends AutoState {
	
	private boolean liftUp = false;
	
	public LiftState(boolean liftUp) {
		this.name = "<Lift State>";		
		this.liftUp = liftUp;
		
		ElevatorAssembly.initialize();
	}
	
	public LiftState(String name, boolean liftUp)
	{
		this.name = name;
		this.liftUp = liftUp;
		
		ElevatorAssembly.initialize();
	}

	// state entry
	public void enter() {
		// do some elevator initialization
		
		ElevatorAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some elevator stuff
		ElevatorAssembly.autoPeriodic(liftUp);
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		// do some elevator cleanup
		ElevatorAssembly.autoStop();
		
		// cleanup base class
		super.exit();
	}
	
}

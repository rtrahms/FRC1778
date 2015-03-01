package StateMachine;

import Systems.PWMDriveAssembly;

public class TurnState extends AutoState {
	
	private double angleToTurn = 0.0;
	
	public TurnState(double angleToTurn)
	{
		this.name = "<Turn State>";
		this.angleToTurn = angleToTurn;
		
		PWMDriveAssembly.initialize();
	}
	
	public TurnState(String name, double angleToTurn)
	{
		this.name =  name;
		this.angleToTurn = angleToTurn;
		
		PWMDriveAssembly.initialize();
	}
	
	// state entry
	public void enter() {
		// do some drivey initialization
		
		PWMDriveAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some drivey stuff
		
		PWMDriveAssembly.autoPeriodicTurn();
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		// do some drivey cleanup
		PWMDriveAssembly.autoStop();
		
		// cleanup base class
		super.exit();
	}

}

package canStateMachine;

import Systems.CANDriveAssembly;

public class TurnState extends AutoState {
	
	private double angleToTurn = 0.0;
	private double speedToTurn = 0.3;
	private boolean isPwm = false;
		
	public TurnState(double angleToTurn, double speed, boolean isPwm)
	{
		this.name = "<Turn State>";
		this.angleToTurn = angleToTurn;
		this.speedToTurn = speed;
		this.isPwm = isPwm;
		
		// PWMDriveAssembly not supported
		
		CANDriveAssembly.initialize();
	}
	
	public TurnState(String name, double angleToTurn, double speed, boolean isPwm)
	{
		this.name =  name;
		this.angleToTurn = angleToTurn;
		this.speedToTurn = speed;
		this.isPwm = isPwm;
		
		// PWMDriveAssembly not supported

		CANDriveAssembly.initialize();
	}
	
	// state entry
	public void enter() {
		// do some drivey initialization
		
		// PWMDriveAssembly not supported

		CANDriveAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some drivey stuff
		
		// PWMDriveAssembly not supported
		
		if (angleToTurn > 0.0)
			CANDriveAssembly.rotateRight(speedToTurn);
		else
			CANDriveAssembly.rotateLeft(speedToTurn);
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		// do some drivey cleanup
			
		// PWMDriveAssembly not supported
		
		CANDriveAssembly.autoStop();
		
		// cleanup base class
		super.exit();
	}

}

package StateMachine;

import Systems.CANDriveAssembly;

public class DriveForwardAction extends Action {
	
	private double speed = 0.0;
	
	public DriveForwardAction(double speed)
	{
		this.speed = speed;
		this.name = "< Drive Forward Action>";
		CANDriveAssembly.initialize();
	}
	
	public DriveForwardAction(String name, double speed)
	{
		this.name =  name;
		this.speed = speed;
				
		CANDriveAssembly.initialize();
	}
	
	// action entry
	public void initialize() {
		// do some drivey initialization
		
		CANDriveAssembly.autoInit();
		
		super.initialize();
	}
	
	// called periodically
	public void process()  {
		
		// do some drivey stuff
				
		CANDriveAssembly.autoPeriodicStraight(speed);
		
		super.process();
	}
	
	// state cleanup and exit
	public void cleanup() {
		// do some drivey cleanup
					
		CANDriveAssembly.autoStop();
		
		// cleanup base class
		super.cleanup();
	}
}

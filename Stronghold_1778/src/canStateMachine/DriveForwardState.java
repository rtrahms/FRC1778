package canStateMachine;

import Systems.CANDriveAssembly;

public class DriveForwardState extends AutoState {
	
	private boolean isPWM = false;
	private double speed = 0.0;
	
	public DriveForwardState(boolean isPWM, double speed)
	{
		this.speed = speed;
		this.isPWM = isPWM;
		if (isPWM)
		{
			this.name = "<PWM Drive Forward State - NOT SUPPORTED>";
			//PWMDriveAssembly.initialize();
		}
		else
		{
			this.name = "<CAN Drive Forward State>";
			CANDriveAssembly.initialize();
		}
	}
	
	public DriveForwardState(String name, boolean isPWM, double speed)
	{
		this.name =  name;
		this.speed = speed;
		this.isPWM = isPWM;
		
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
		
		CANDriveAssembly.autoPeriodicStraight(speed);
		
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

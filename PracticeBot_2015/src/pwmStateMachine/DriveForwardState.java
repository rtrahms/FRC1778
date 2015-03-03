package pwmStateMachine;

import Systems.CANDriveAssembly;
import Systems.PWMDriveAssembly;

public class DriveForwardState extends AutoState {
	
	private boolean isPWM = false;
	private double speed = 0.0;
	
	public DriveForwardState(boolean isPWM, double speed)
	{
		this.speed = speed;
		this.isPWM = isPWM;
		if (isPWM)
		{
			this.name = "<PWM Drive Forward State>";
			PWMDriveAssembly.initialize();
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
		
		if (isPWM)
			PWMDriveAssembly.initialize();
		else
			CANDriveAssembly.initialize();
	}
	
	// state entry
	public void enter() {
		// do some drivey initialization
		
		if (isPWM)
			PWMDriveAssembly.autoInit();
		else
			CANDriveAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some drivey stuff
		if (isPWM)
			PWMDriveAssembly.autoPeriodicStraight(speed);
		else
			CANDriveAssembly.autoPeriodicStraight(speed);
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		// do some drivey cleanup
		if (isPWM)
			PWMDriveAssembly.autoStop();
		else
			CANDriveAssembly.autoStop();
		
		// cleanup base class
		super.exit();
	}
}

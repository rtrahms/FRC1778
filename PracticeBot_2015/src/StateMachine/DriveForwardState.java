package StateMachine;

import Systems.PWMDriveAssembly;

public class DriveForwardState extends AutoState {

	// PWMDriveAssembly is now a static class (no instantiation needed)
	//DriveAssembly driveAssembly;
	
	public DriveForwardState()
	{
		this.name = "<Drive Forward State>";
		
		PWMDriveAssembly.initialize();
		//driveAssembly = new DriveAssembly();
	}
	
	public DriveForwardState(String name)
	{
		this.name =  name;
		
		PWMDriveAssembly.initialize();
		//driveAssembly = new DriveAssembly();
	}
	
	// state entry
	public void enter() {
		// do some drivey initialization
		
		PWMDriveAssembly.autoInit();
		//driveAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some drivey stuff
		
		PWMDriveAssembly.autoPeriodic();
		//driveAssembly.autoPeriodic();
		
		return super.process();
	}
	
	// state cleanup and exit
	public void exit() {
		// do some drivey cleanup
		//driveAssembly.autoStop();
		
		// cleanup base class
		super.exit();
	}
}

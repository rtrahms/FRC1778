package StateMachine;

import Systems.DriveAssembly;

public class DriveForwardState extends AutoState {

	//DriveAssembly driveAssembly;
	
	public DriveForwardState()
	{
		this.name = "<Drive Forward State>";
		
		DriveAssembly.initialize();
		//driveAssembly = new DriveAssembly();
	}
	
	public DriveForwardState(String name)
	{
		this.name =  name;
		
		DriveAssembly.initialize();
		//driveAssembly = new DriveAssembly();
	}
	
	// state entry
	public void enter() {
		// do some drivey initialization
		
		DriveAssembly.autoInit();
		//driveAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some drivey stuff
		
		DriveAssembly.autoPeriodic();
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

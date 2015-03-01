package org.usfirst.frc.team1778.robot;

public class DriveForwardState extends AutoState {

	//DriveAssembly driveAssembly;
	
	public DriveForwardState()
	{
		this.name = "<Drive Forward State>";
		
		//driveAssembly = new DriveAssembly();
	}
	
	public DriveForwardState(String name)
	{
		this.name =  name;
		
		//driveAssembly = new DriveAssembly();
	}
	
	// state entry
	public void enter() {
		// do some drivey initialization
		//driveAssembly.autoInit();
		
		super.enter();
	}
	
	// called periodically
	public AutoState process()  {
		
		// do some drivey stuff
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

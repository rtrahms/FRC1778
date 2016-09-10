package StateMachine;

import java.util.prefs.Preferences;

import Systems.CANDriveAssembly;

public class TurnAction extends Action {
	
	private double angleToTurn = 0.0;
	private double speedToTurn = 0.3;
		
	public TurnAction(double angleToTurn, double speed)
	{
		this.name = "<Turn Action>";
		this.angleToTurn = angleToTurn;
		this.speedToTurn = speed;
				
		CANDriveAssembly.initialize();
	}
	
	public TurnAction(String name, double angleToTurn, double speed, boolean isPwm)
	{
		this.name =  name;
		this.angleToTurn = angleToTurn;
		this.speedToTurn = speed;
		
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
				
		if (angleToTurn > 0.0)
			CANDriveAssembly.rotateRight(speedToTurn);
		else
			CANDriveAssembly.rotateLeft(speedToTurn);
		
		super.process();
	}
	
	// action cleanup and exit
	public void cleanup() {
		// do some drivey cleanup
			
		// PWMDriveAssembly not supported
		
		CANDriveAssembly.autoStop();
		
		// cleanup base class
		super.cleanup();
	}
	
	public void persistWrite(int counter, Preferences prefs) {

		// create node for action
		Preferences actionPrefs = prefs.node(counter + "_" + this.name);
	
		// store action details
		actionPrefs.put("class",this.getClass().toString());
		actionPrefs.putDouble("angleToTurn",this.angleToTurn);
		actionPrefs.putDouble("speedToTurn",this.speedToTurn);
	}

}

package StateMachine;

import Systems.CANDriveAssembly;
import NetworkComm.RPIComm;

public class CalibrateTargetAction extends Action {
	
	public CalibrateTargetAction() {
		this.name = "<Calibrate Target Action>";		
	}
	
	public CalibrateTargetAction(String name)
	{
		this.name = name;
	}
	
	// action entry
	public void initialize() {
		// do some calibrate initialization
		RPIComm.initialize();
		//RPIComm.setMovementModes(true, false);  // forward movement only
		RPIComm.setMovementModes(false, true);  // lateral movement only
		//RPIComm.setMovementModes(true, true);  // forward and lateral movement
				
		super.initialize();
	}
	
	// called periodically
	public void process()  {
		
		// do some calibrate stuff
		
		if (RPIComm.hasTarget()) {
			double leftVal = RPIComm.getLeftDriveValue();
			double rightVal = RPIComm.getRightDriveValue();
			
			CANDriveAssembly.drive(leftVal, rightVal, 0);
		}
		else {
			// no target - stop motors
			// TODO:  spin & search mode?
			CANDriveAssembly.drive(0, 0, 0);
		}
		
		super.process();
	}
	
	// state cleanup and exit
	public void cleanup() {
		// do some calibrate cleanup
		CANDriveAssembly.drive(0, 0, 0);
					
		// cleanup base class
		super.cleanup();
	}

}

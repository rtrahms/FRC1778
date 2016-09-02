package StateMachine;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {
	public static enum AutoMode {
		DO_NOTHING, TARGET_FOLLOW, DRIVE_FORWARD_SLOW, TEST_NETWORK;
	}
	
	AutoMode mode;
	private final SendableChooser chooser;
	
	public AutoChooser() {
		chooser = new SendableChooser();
		
		chooser.addDefault("DO_NOTHING", AutoMode.DO_NOTHING);
		chooser.addObject("TARGET_FOLLOW", AutoMode.TARGET_FOLLOW);
		chooser.addObject("DRIVE_FORWARD_SLOW", AutoMode.DRIVE_FORWARD_SLOW);
		chooser.addObject("TEST_NETWORK", AutoMode.TEST_NETWORK);
	}
	
	public void putChoosersOnDash(){
		SmartDashboard.putData("Auto_Mode_Chooser ", chooser);
	}

	public AutoMode getAutoChoice() {
		return (AutoMode) chooser.getSelected();
	}

}

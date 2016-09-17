package StateMachine;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {
	public static final int DO_NOTHING = 0;
	public static final int TARGET_FOLLOW = 1;
	public static final int DRIVE_FORWARD_SLOW = 2;
	public static final int DRIVE_FORWARD_FOREVER = 3;
	public static final int TEST_NETWORK = 4;
	
	int mode;
	private SendableChooser chooser;
	
	public AutoChooser() {
		chooser = new SendableChooser();
		
		chooser.addDefault("DO_NOTHING", DO_NOTHING);
		chooser.addObject("TARGET_FOLLOW", TARGET_FOLLOW);
		chooser.addObject("DRIVE_FORWARD_SLOW", DRIVE_FORWARD_SLOW);
		chooser.addObject("DRIVE_FORWARD_FOREVER", DRIVE_FORWARD_FOREVER);
		chooser.addObject("TEST_NETWORK", TEST_NETWORK);
		SmartDashboard.putData("Auto_Mode_Chooser", chooser);
	}
	
	public int getAutoChoice() {
		return (int) chooser.getSelected();
	}

}

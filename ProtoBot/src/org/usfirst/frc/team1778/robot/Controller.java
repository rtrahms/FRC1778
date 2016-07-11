package org.usfirst.frc.team1778.robot;

/** 
    Use this class to map the buttons and tumbsticks of the Logitech controllers to named uses.
	For reference, all the button IDs are listed in the LogitechF310.java file.
*/
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;import standard.LogitechF310;

public class Controller {

    public static final int PORT_DRIVER_CONTROLLER = 0;
    public static final int PORT_COPILOT_CONTROLLER = 1;

	public Joystick controller;
	public Button QUICKTURN_Button;

	// Ports
	ControllerDriver = new Joystick(ControlMap.PORT_STICK_RIGHT);
	ControllerCoPilot = new Joystick(ControlMap.PORT_CONTROLLER);


	public class ControllerDriver {
	public static final int QUICKTURN_Button = LogitechF310.RB;
	public static final int THROTTLE = LogitechF310.Axis.LEFT_Y;
	public static final int STEERING = LogitechF310.Axis.RIGHT_X;
    }

	public class ControllerCopilot{

	}
}
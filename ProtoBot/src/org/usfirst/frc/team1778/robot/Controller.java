package org.usfirst.frc.team1778.robot;

/** 
    Use this class to map the buttons and tumbsticks of the Logitech controllers to named uses.
	For reference, all the button IDs are listed in the LogitechF310.java file.
*/
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import Utility.LogitechF310;


public class Controller {

    public int PORT_DRIVER_CONTROLLER=0;
    public int PORT_COPILOT_CONTROLLER=1;
	public Joystick Driver;
	public Joystick CoPilot;
	public Button quickTurn;
	
    public Controller (){
    	Driver = new Joystick(PORT_DRIVER_CONTROLLER);
    	CoPilot = new Joystick(PORT_COPILOT_CONTROLLER);
    	quickTurn = new JoystickButton(Driver, LogitechF310.RB);
    }
}
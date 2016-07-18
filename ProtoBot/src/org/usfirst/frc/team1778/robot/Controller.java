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

    public static final int PORT_DRIVER_CONTROLLER=0;
    public static final int PORT_COPILOT_CONTROLLER=1;
	public static Joystick Driver;
	public static Joystick CoPilot;
	public static Button quickTurn;
	
    public Controller (){
    	Driver = new Joystick(PORT_DRIVER_CONTROLLER);
    	CoPilot = new Joystick(PORT_COPILOT_CONTROLLER);
    	quickTurn = new JoystickButton(Driver, LogitechF310.RB);
    }
    
    public double Driver_Throttle (){
    	return Driver.getRawAxis(LogitechF310.Axis.RIGHT_X);
    }

    public double Driver_Steering (){
    	return Driver.getRawAxis(LogitechF310.Axis.LEFT_X);
    }
    
    public boolean Driver_isQuickTurn(){
    	return Driver.getRawButton(LogitechF310.RB);
    }
}
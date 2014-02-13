
package frc1778;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc1778.commands.TankDriveWithJoysticks;

/**
 * glue that binds controls on driver station to commands and command groups
 * that allow control of the robot.
 */
public class OI {
    Joystick leftJoy;
    Joystick rightJoy;
    Joystick gamepad;
    JoystickButton lButton1;
    JoystickButton lButton2;
    JoystickButton lButton3;
    JoystickButton lButton4;
    JoystickButton lButton5;
    JoystickButton lButton6;
    JoystickButton lButton7;
    JoystickButton lButton8;
    JoystickButton lButton9;
    JoystickButton lButton10;
    JoystickButton lButton11;
    JoystickButton rButton1;
    JoystickButton rButton2;
    JoystickButton rButton3;
    JoystickButton rButton4;
    JoystickButton rButton5;
    JoystickButton rButton6;
    JoystickButton rButton7;
    JoystickButton rButton8;
    JoystickButton rButton9;
    JoystickButton rButton10;
    JoystickButton rButton11;
    
    public OI () 
    {
	leftJoy  = new Joystick (1);
	rightJoy = new Joystick (2);
        gamepad = new Joystick(3);	
        rButton1.whenReleased(new TankDriveWithJoysticks());	
        
        // TODO: tie gamepad inputs to new gate operation commands
        // TODO: Evan should do this
    }
    

    // Tank drive & Arcade drive input methods (left and right joysticks)
    
    public boolean getLeftButton6(){
        return leftJoy.getRawButton(6);
    }

    public boolean getLeftButton7(){
        return leftJoy.getRawButton(7);
    }

    public double getLeftSpeed () {
        return  -leftJoy.getY ();
    }

    public double getLeftTurn () {
        return   leftJoy.getX ();
    }

    public double getRightSpeed () {
        return -rightJoy.getY ();
    }

    public double getRightTurn () {
        return  rightJoy.getX ();
    }

    public double getRightThrottle () {
        return -rightJoy.getZ ();
    }

    public double getLeftThrottle () {
        return -leftJoy.getZ ();
    }

    // Gate control input methods (gamepad)
    // TODO: Define input methods for the gamepad here for gate operation
    // TODO: Evan should be defining these
    
    public double getGamepadRightStick() {
        return gamepad.getRawAxis(4);
    }
    
    public double getGamepadLeftStick() {
        return gamepad.getRawAxis(2);
    }
    


}

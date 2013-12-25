
package frc1778;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc1778.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
     // left joystick and buttons
    Joystick leftJoy = new Joystick(1);
    Button leftButton1 = new JoystickButton(leftJoy,1);
    Button leftButton2 = new JoystickButton(leftJoy,2);
    Button leftButton3 = new JoystickButton(leftJoy,3);
    Button leftButton4 = new JoystickButton(leftJoy,4);
    Button leftButton5 = new JoystickButton(leftJoy,5);
    Button leftButton6 = new JoystickButton(leftJoy,6);
    Button leftButton7 = new JoystickButton(leftJoy,7);
    Button leftButton8 = new JoystickButton(leftJoy,8);
    
    Joystick rightJoy = new Joystick(2);
    Button rightButton1 = new JoystickButton(rightJoy,1);
    Button rightButton2 = new JoystickButton(rightJoy,2);
    Button rightButton3 = new JoystickButton(rightJoy,3);
    Button rightButton4 = new JoystickButton(rightJoy,4);
    Button rightButton5 = new JoystickButton(rightJoy,5);
    Button rightButton6 = new JoystickButton(rightJoy,6);
    Button rightButton7 = new JoystickButton(rightJoy,7);
    Button rightButton8 = new JoystickButton(rightJoy,8);

    // constructor
    public OI() {
        //leftButton1.whenPressed(new LowerScoop());
        //leftButton1.whenReleased(new RaiseScoop());
        
    }

}


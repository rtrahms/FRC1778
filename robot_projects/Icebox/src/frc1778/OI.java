
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
    Joystick jimJoy;
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
    JoystickButton jimButton1;
    JoystickButton jimButton2;
    JoystickButton jimButton3;
    JoystickButton jimButton4;
    JoystickButton jimButton5;
    JoystickButton jimButton6;
    JoystickButton jimButton7;
    JoystickButton jimButton8;
    JoystickButton jimButton9;
    JoystickButton jimButton10;
    JoystickButton jimButton11;
    
    public OI () 
    {
	leftJoy  = new Joystick (1);
	rightJoy = new Joystick (2);
        
        // TODO: Joystick on port 3 needs to be instantiated for the gamepad
        // TODO: Evan should do this
//      jimJoy   = new Joystick (3);  /* , 4, 10 */
        
    /*  extra logitech joystick with 4 axies and 10 buttons */

//	lButton1   = new JoystickButton (leftJoy, 1);
//	lButton2   = new JoystickButton (leftJoy, 2);
//	lButton3   = new JoystickButton (leftJoy, 3);
//	lButton4   = new JoystickButton (leftJoy, 4);
//	lButton6   = new JoystickButton (leftJoy, 6);
//	lButton8   = new JoystickButton (leftJoy, 8);
//	lButton9   = new JoystickButton (leftJoy, 9);
//	lButton10  = new JoystickButton (leftJoy, 10);
	lButton11  = new JoystickButton (leftJoy, 11);
	
	rButton1   = new JoystickButton (rightJoy, 1);
//	rButton2   = new JoystickButton (rightJoy, 2);
//	rButton6   = new JoystickButton (rightJoy, 6);
//	rButton7   = new JoystickButton (rightJoy, 7);
//	rButton8   = new JoystickButton (rightJoy, 8);
//	rButton9   = new JoystickButton (rightJoy, 9);
//	rButton10  = new JoystickButton (rightJoy, 10);
//	rButton11  = new JoystickButton (rightJoy, 11);
	
//	jimButton1 = new JoystickButton(jimJoy, 1);
//	jimButton2 = new JoystickButton(jimJoy, 2);
//	jimButton3 = new JoystickButton(jimJoy, 3);
//	jimButton4 = new JoystickButton(jimJoy, 4);
//	jimButton5 = new JoystickButton(jimJoy, 5);
//	jimButton6 = new JoystickButton(jimJoy, 6);
//	jimButton7 = new JoystickButton(jimJoy, 7);
//	jimButton8 = new JoystickButton(jimJoy, 8);
//	jimButton9 = new JoystickButton(jimJoy, 9);
//	jimButton10 = new JoystickButton(jimJoy, 10);	

//	Left Controller
	
/*	lButton6->WhenPressed(new DriveStraight());
	lButton9->WhenPressed(new SetShooterLimits());
	lButton10->WhenPressed(new GyroReset());
*/
	
    //  Right Controller	
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
    
    public double getJimLeftX () {
        return  jimJoy.getRawAxis (1);
    }

    public double getJimLeftY () {
        return -jimJoy.getRawAxis (2);
    }
    
    public double getJimRightX () {
        return jimJoy.getRawAxis (3);
    }

    public double getJimRightY() {
        return -jimJoy.getRawAxis (4);
    }
}

package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.*;

public class UserInput {
	
	public UserInput(){
		
	}
	
	Joystick gamepad = new Joystick(0);
	
	// returns the value on the vertical axis of the left stick
	public double getLeftStickVert(){
		return gamepad.getY();
	}
	
	// returns value for the horizontal axis of the right stick
	public double getRightStickHoriz(){
		return -gamepad.getZ();
	}
	
	public boolean getLeftButton(){
		return gamepad.getRawButton(5);
	}
	
	
}
package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class Driver {
	Joystick gamepad;
    CANTalon motorL;
    CANTalon motorR;
    RobotDrive drive;
    
	public Driver(){
		motorL = new CANTalon(1);
    	motorR = new CANTalon(2);
    	motorL.setInverted(true);
    	motorR.setInverted(true);

    	gamepad = new Joystick(0);
    	drive = new RobotDrive(motorL, motorR);
	}
	
	public void driveNow(){
		drive.tankDrive(gamepad.getY(), gamepad.getThrottle());
        Timer.delay(0.005);	
	}
}

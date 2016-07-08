package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically it 
 * contains the code necessary to operate a robot with tank drive.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	//  Joystick leftStick;  // set to ID 1 in DriverStation
	//  Joystick rightStick; // set to ID 2 in DriverStation
	//Joystick gamepad;
    //CANTalon motorL;
    //CANTalon motorR;
    //RobotDrive drive;
	Driver driver;
    
    
    public Robot() {
    	//motorL = new CANTalon(1);
    	//motorR = new CANTalon(2);
    	//motorL.setInverted(true);
    	//motorR.setInverted(true);
    	//	leftStick = new Joystick(0);
    	//	rightStick = new Joystick(1);
    	//gamepad = new Joystick(0);
    	//drive = new RobotDrive(motorL, motorR);
    	driver = new Driver();
    }
    
    /**
     * Runs the motors with tank steering.
     */
    public void operatorControl() {
        //myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	//if(Math.abs(leftStick.getY()) > 0.1)
        	//	drive.tankDrive(leftStick.getY(), rightStick.getY());
        	//drive.tankDrive(gamepad.getY(), gamepad.getThrottle());
            //Timer.delay(0.005);		// wait for a motor update time
        }
    }
}

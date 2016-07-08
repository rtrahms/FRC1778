package org.usfirst.frc.team1778.robot;


import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a short sample program demonstrating how to use the basic throttle
 * mode of the new CAN Talon.
 */
public class Robot extends SampleRobot {

	Driver driver;
  public Robot() {
	  driver = new Driver();
  }

  /**
    * Runs the motor.
    */
  public void operatorControl() {
    while (isOperatorControl() && isEnabled()) {
    	driver.driveNow();
    }
  }
}

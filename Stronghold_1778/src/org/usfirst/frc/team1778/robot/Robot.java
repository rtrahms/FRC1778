package org.usfirst.frc.team1778.robot;

import Systems.AutoShooterAssembly;
import Systems.CANDriveAssembly;
import Systems.CatapultAssembly;
import Systems.FrontArmAssembly;
import Systems.GyroSensor;
import Systems.NetworkCommAssembly;
import Systems.RioDuinoAssembly;
import Systems.UltrasonicSensor;
import canStateMachine.AutoStateMachine;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/*
 * For Stronghold, Team 1778 robot has four major systems: CANDrive, AutoShooter, FrontArm & Catapult
 * Supporting systems: Gyro Sensor, Ultrasonic Sensor, RioDuino (for LEDs) and Network Comm (for targeting)
 * Autonomous modes run by AutoStateMachine object 
 */

public class Robot extends IterativeRobot {
	
	PowerDistributionPanel pdp;

	// autonomous state machine object
	AutoStateMachine autoSM;
	
	public void robotInit() {

		autoSM = new AutoStateMachine();

		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();

		GyroSensor.initialize();
		UltrasonicSensor.initialize();
		
		NetworkCommAssembly.initialize();
		
		CANDriveAssembly.initialize();
		AutoShooterAssembly.initialize();
		FrontArmAssembly.initialize();
		CatapultAssembly.initialize();

		RioDuinoAssembly.initialize();
		RioDuinoAssembly.SendString("robotInit");
	}

	// called one time on entry into autonomous
	public void autonomousInit() {
		
		// reset sensors and network table
		GyroSensor.reset();
		UltrasonicSensor.reset();
		NetworkCommAssembly.reset();
		
		RioDuinoAssembly.autonomousInit();
		CatapultAssembly.autoInit();
		
		// start the autonomous state machine
		autoSM.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		
		// update values used for targeting
    	NetworkCommAssembly.updateValues(); 	
		
		// state machine runs things in autonomous
		autoSM.process();
	}

	// called one time on entry into teleop
	public void teleopInit() {
		
		RioDuinoAssembly.teleopInit();
		
		// reset sensors and network table
		GyroSensor.reset();
		UltrasonicSensor.reset();
		//NetworkCommAssembly.reset();
		
		// teleop init for all systems
		CANDriveAssembly.teleopInit();
		AutoShooterAssembly.teleopInit();
		CatapultAssembly.teleopInit();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
				
		// update values used for targeting
    	//NetworkCommAssembly.updateValues();
    	
		// check status of the ball (if we have one)   	
		UltrasonicSensor.teleopPeriodic();
    	
    	CANDriveAssembly.teleopPeriodic();
    	AutoShooterAssembly.teleopPeriodic();
    	FrontArmAssembly.teleopPeriodic();
    	CatapultAssembly.teleopPeriodic(); 	
 	}
	
	public void disabledInit() {
		
		RioDuinoAssembly.disabledInit();
	}
	
	public void testInit() {
		RioDuinoAssembly.testInit();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() 
	{
	}

}

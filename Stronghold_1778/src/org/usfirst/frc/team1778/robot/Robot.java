package org.usfirst.frc.team1778.robot;

import NetworkComm.GRIPDataComm;
import NetworkComm.InputOutputComm;
import Systems.AutoShooterAssembly;
import Systems.CANDriveAssembly;
import Systems.CatapultAssembly;
import Systems.FrontArmAssembly;
import Systems.GyroSensor;
import Systems.RioDuinoAssembly;
import Systems.UltrasonicSensor;
import canStateMachine.AutoStateMachine;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
		
		GRIPDataComm.initialize();
		InputOutputComm.initialize();
		
		AutoShooterAssembly.initialize();	
		CANDriveAssembly.initialize();
		FrontArmAssembly.initialize();
		CatapultAssembly.initialize();

		RioDuinoAssembly.initialize();
		RioDuinoAssembly.SendString("robotInit");
		
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainStatus", "Robot initialization complete!");
	}

	// called one time on entry into autonomous
	public void autonomousInit() {
		
		// reset sensors and network table
		GyroSensor.reset();
		UltrasonicSensor.reset();
		
		GRIPDataComm.autoInit();
		AutoShooterAssembly.autoInit();
		
		RioDuinoAssembly.autonomousInit();
		CANDriveAssembly.autoInit();
		CatapultAssembly.autoInit();
		
		// start the autonomous state machine
		autoSM.start();
		
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainStatus", "Autonomous initialization complete!");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
				
		// update targeting data from network tables
		GRIPDataComm.updateValues();
		
		// state machine runs things in autonomous
		autoSM.process();
	}

	// called one time on entry into teleop
	public void teleopInit() {
		
		RioDuinoAssembly.teleopInit();
		
		// reset sensors and network table
		GyroSensor.reset();
		UltrasonicSensor.reset();
		
		// teleop init for all systems
		CANDriveAssembly.teleopInit();
		FrontArmAssembly.teleopInit();
		CatapultAssembly.teleopInit();
		
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainStatus", "Teleop initialization complete!");
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
				    	
		// check status of the ball (if we have one)   	
		UltrasonicSensor.teleopPeriodic();
    	
    	CANDriveAssembly.teleopPeriodic();
    	FrontArmAssembly.teleopPeriodic();
    	CatapultAssembly.teleopPeriodic(); 	
    	
		
		// send output data for test & debug
    	/*
    	double gyroAngle = GyroSensor.getAngle();
		String gyroAngleStr = String.format("%.2f", gyroAngle);
		String myString = new String("gyroAngle = " + gyroAngleStr);
		System.out.println(myString);
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"Teleop/gyro_test", myString);
		*/

 	}
	
	public void disabledInit() {
		
		
		RioDuinoAssembly.disabledInit();
		
		InputOutputComm.initialize();
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainStatus", "Robot disabled!");
		
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

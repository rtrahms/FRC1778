package org.usfirst.frc.team1778.robot;

import Systems.AutoShooterAssembly;
import Systems.CANDriveAssembly;
import Systems.CatapultAssembly;
import Systems.FrontArmAssembly;
import Systems.GyroSensor;
import Systems.NetworkCommAssembly;
import Systems.RioDuinoAssembly;
import canStateMachine.AutoStateMachine;
import edu.wpi.first.wpilibj.DriverStation;
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
 * Team 1778 robot has three major subclasses - Drivetrain, alignment and
 * elevator
 */

public class Robot extends IterativeRobot {
	
	PowerDistributionPanel pdp;

	// autonomous state machine object
	AutoStateMachine autoSM;

	// particulars about the team number and color
	DriverStation.Alliance teamColor;
	int teamLocation;

	public void robotInit() {

		autoSM = new AutoStateMachine();

		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();

		GyroSensor.initialize();
		NetworkCommAssembly.initialize();
		AutoShooterAssembly.initialize();
		CANDriveAssembly.initialize();
		FrontArmAssembly.initialize();
		CatapultAssembly.initialize();
		RioDuinoAssembly.initialize();
		//HookLiftAssembly.initialize();

		RioDuinoAssembly.SendString("robotInit");

	}

	// called one time on entry into autonomous
	public void autonomousInit() {

		teamColor = DriverStation.getInstance().getAlliance();
		teamLocation = DriverStation.getInstance().getLocation();

		System.out.println("in autonomousInit(), station #" + teamLocation);

		// start up the robot team color
		 if (teamColor == DriverStation.Alliance.Blue)
			 RioDuinoAssembly.setTeamColor(RioDuinoAssembly.Color.Blue); 
		 else
			 RioDuinoAssembly.setTeamColor(RioDuinoAssembly.Color.Red);

		RioDuinoAssembly.autonomousInit();
		
		// start the autonomous state machine
		//autoSM.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		//autoSM.process();
	}

	// called one time on entry into teleop
	public void teleopInit() {

		GyroSensor.reset();
		
		CANDriveAssembly.teleopInit();
		AutoShooterAssembly.teleopInit();
		FrontArmAssembly.teleopInit();
		CatapultAssembly.teleopInit();
		RioDuinoAssembly.teleopInit();
		//HookLiftAssembly.teleopInit();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
				
    	NetworkCommAssembly.updateValues(); 	
    	CANDriveAssembly.teleopPeriodic();
    	AutoShooterAssembly.teleopPeriodic();
    	FrontArmAssembly.teleopPeriodic();
    	CatapultAssembly.teleopPeriodic();
    	//HookLiftAssembly.teleopPeriodic();
  	
    	//System.out.println("Gyro value = " + GyroSensor.getAngle());
 	}
	
	public void disabledInit() {
		RioDuinoAssembly.disabledInit();
    	CatapultAssembly.disabledInit();		
	}
	
	public void testInit() {
		RioDuinoAssembly.testInit();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}

package org.usfirst.frc.team1778.robot;

import Systems.AutoShooterAssembly;
import Systems.CANDriveAssembly;
import Systems.CatapultAssembly;
import Systems.FrontArmAssembly;
import Systems.GyroSensor;
import Systems.NetworkCommAssembly;
import Systems.RioDuinoAssembly;
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
 * Team 1778 robot has three major subclasses - Drivetrain, alignment and
 * elevator
 */

public class Robot extends IterativeRobot {
	
	PowerDistributionPanel pdp;

	// autonomous state machine object
	AutoStateMachine autoSM;

	
	boolean teleopMode = false;

	public void robotInit() {

		autoSM = new AutoStateMachine();

		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();

		GyroSensor.initialize();
		NetworkCommAssembly.initialize();
		
		CANDriveAssembly.initialize();
		AutoShooterAssembly.initialize();
		//FrontArmAssembly.initialize();
		CatapultAssembly.initialize();
		//HookLiftAssembly.initialize();

		RioDuinoAssembly.initialize();
		RioDuinoAssembly.SendString("robotInit");
		
		// reset the catapult (to load ball)
		//CatapultAssembly.reset();

	}

	// called one time on entry into autonomous
	public void autonomousInit() {

		teleopMode = false;
		
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

		teleopMode = true;
		
		GyroSensor.reset();
		
		CANDriveAssembly.teleopInit();
		AutoShooterAssembly.teleopInit();
		//FrontArmAssembly.teleopInit();
		CatapultAssembly.teleopInit();
		//HookLiftAssembly.teleopInit();

		RioDuinoAssembly.teleopInit();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
				
    	NetworkCommAssembly.updateValues(); 	
    	
    	CANDriveAssembly.teleopPeriodic();
    	AutoShooterAssembly.teleopPeriodic();
    	//FrontArmAssembly.teleopPeriodic();
    	CatapultAssembly.teleopPeriodic();
    	//HookLiftAssembly.teleopPeriodic();
  	
    	//System.out.println("Gyro value = " + GyroSensor.getAngle());
 	}
	
	public void disabledInit() {
    	AutoShooterAssembly.disabledInit();
    	//FrontArmAssembly.disabledInit();
    	CatapultAssembly.disabledInit();		
    	//HookLiftAssembly.disabledInit();
    	
		RioDuinoAssembly.disabledInit();
	}
	
	public void testInit() {
		teleopMode = false;
		
		RioDuinoAssembly.testInit();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() 
	{
	}

}


package org.usfirst.frc.team1778.robot;

import canStateMachine.AutoStateMachine;
import Systems.CANDriveAssembly;
import Systems.ElevatorAssembly;
import Systems.AlignmentAssembly;
import Systems.PneumaticsTester;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/* Team 1778 robot has three major subclasses - Drivetrain, alignment and elevator */

public class Robot extends IterativeRobot {

	//Camera camera;
	
	PowerDistributionPanel pdp;
	
	// autonomous state machine object
	AutoStateMachine autoSM;

	
    public void robotInit() {
    	
    	autoSM = new AutoStateMachine();

    	pdp = new PowerDistributionPanel();
    	pdp.clearStickyFaults();
    	
    	CANDriveAssembly.initialize();
    	ElevatorAssembly.initialize();
    	AlignmentAssembly.initialize();
    	//PneumaticsTester.initialize();
    	//camera = new Camera("169.254.26.13");
    }

    // called one tim on entry into autonomous
    public void autonomousInit() {    	
    	autoSM.start();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {    	
    	autoSM.process();
    }

    // called one time on entry into teleop
    public void teleopInit() {
    	
    	CANDriveAssembly.teleopInit();
    	ElevatorAssembly.teleopInit();
    	AlignmentAssembly.teleopInit();
    	//PneumaticsTester.teleopInit();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

		//System.out.println("Chill out teleopPeriodic call!");
    	
    	CANDriveAssembly.teleopPeriodic();
    	ElevatorAssembly.teleopPeriodic();
    	AlignmentAssembly.teleopPeriodic();
    	//PneumaticsTester.teleopPeriodic();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

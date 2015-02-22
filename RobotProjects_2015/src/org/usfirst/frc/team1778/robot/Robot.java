
package org.usfirst.frc.team1778.robot;

import org.usfirst.frc.team1778.robot.camera.Camera;

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

	
	// Chill Out 1778 major subassemblies
	DriveAssembly drivetrain;
	ElevatorAssembly elevator;
	AlignmentAssembly aligner;
	//Camera camera;
	PneumaticsTester pneumTest;
	
	PowerDistributionPanel pdp;
	
	
    public void robotInit() {
    	
    	pdp = new PowerDistributionPanel();
    	pdp.clearStickyFaults();
    	
    	drivetrain = new DriveAssembly();
    	elevator = new ElevatorAssembly();
    	aligner = new AlignmentAssembly();
    	//pneumTest = new PneumaticsTester();
    	
    	//camera = new Camera("169.254.26.13");
    }

    // called one tim on entry into autonomous
    public void autonomousInit() {
    	drivetrain.autoInit();
    	elevator.autoInit();
    	aligner.autoInit();
    	RobotAuto.autoInit(this);
    	//pneumTest.autoInit();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	//drivetrain.autoPeriodic();
    	RobotAuto.autoPeriodic(this);
    }

    // called one time on entry into teleop
    public void teleopInit() {
    	drivetrain.teleopInit();
    	elevator.teleopInit();
    	aligner.teleopInit();
    	//pneumTest.teleopInit();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

		//System.out.println("Chill out teleopPeriodic call!");

    	drivetrain.teleopPeriodic();
        elevator.teleopPeriodic();
        aligner.teleopPeriodic();
        //pneumTest.teleopPeriodic();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

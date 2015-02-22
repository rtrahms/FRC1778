
package org.usfirst.frc.team1778.robot;

import org.usfirst.frc.team1778.robot.camera.Camera;

import StateMachine.AutoStateMachine;
import Systems.DriveAssembly;
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

/* Team 1778 Practice Robot Code - no pneumatics, no elevator - only drivetrain */

public class Robot extends IterativeRobot {

	
	// Chill Out 1778 major subassemblies - now static classes
	//DriveAssembly drivetrain;
	//PneumaticsTester pneumTest;
	//Camera camera;
	
	// autonomous state machine object
	AutoStateMachine autoSM;
	
	PowerDistributionPanel pdp;
	
	
    public void robotInit() {
    	
    	autoSM = new AutoStateMachine();
    	
    	pdp = new PowerDistributionPanel();
    	pdp.clearStickyFaults();
    	
    	DriveAssembly.initialize();
    	//drivetrain = new DriveAssembly();
    	//pneumTest = new PneumaticsTester();
    	
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

    	DriveAssembly.teleopInit();
    	//drivetrain.teleopInit();
    	//pneumTest.teleopInit();

    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

    	DriveAssembly.teleopPeriodic();
    	//drivetrain.teleopPeriodic();
        //pneumTest.teleopPeriodic();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

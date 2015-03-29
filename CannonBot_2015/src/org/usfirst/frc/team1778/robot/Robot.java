
package org.usfirst.frc.team1778.robot;

import pwmStateMachine.AutoStateMachine;
import Systems.PWMDriveAssembly;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/* Team 1778 Practice Robot Code - no pneumatics, no elevator - only drivetrain */

public class Robot extends IterativeRobot {
	
    // Pneumatics control module ID
    private static final int PCM_NODE_ID = 2;

    //Camera camera;
	
    // safety lights
    Relay      lightOne;
    Relay      lightTwo;
    DigitalInput safetySwitch;
    boolean safetyDisabled = false;
        	
	// autonomous state machine object
	AutoStateMachine autoSM;
	
	PowerDistributionPanel pdp;	
	
    public void robotInit() {
    	
    	autoSM = new AutoStateMachine();
    	
    	pdp = new PowerDistributionPanel();
    	pdp.clearStickyFaults();
    	
    	PWMDriveAssembly.initialize();
 
        // Light relays on digital sidecar
        lightOne = new Relay(0);
        lightTwo = new Relay(1);
        //safetySwitch = new DigitalInput(2);

    	//camera = new Camera("169.254.26.13");
    }

    // called one tim on entry into autonomous
    public void autonomousInit() {    	
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {    	
    }

    // called one time on entry into teleop
    public void teleopInit() {
    	    	
    	//System.out.println("initializing");
    	PWMDriveAssembly.teleopInit();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        // check state of safety switch
        //safetyDisabled = safetySwitch.get();
    	
    	// temporarily disabled safety (until switch is installed)
    	safetyDisabled = true;
        
        if (safetyDisabled) {

        	// turn on warning lights
        	//System.out.println("driving");
        	lightOne.set(Relay.Value.kForward);
        	lightTwo.set(Relay.Value.kForward);

        	PWMDriveAssembly.teleopPeriodic();    	
        } else {
	        // turn off warning lights
	        lightOne.set(Relay.Value.kOff);
	        lightTwo.set(Relay.Value.kOff);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

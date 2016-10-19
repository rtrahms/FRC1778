
package org.usfirst.frc.team1778.robot;

import NetworkComm.InputOutputComm;
import NetworkComm.RPIComm;
import StateMachine.AutoStateMachine;
import Systems.CANDriveAssembly;
import Systems.NavXSensor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	AutoStateMachine autoSM;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		RPIComm.initialize();
		InputOutputComm.initialize();
		CANDriveAssembly.initialize();
		
		autoSM = new AutoStateMachine();
		
    	InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainLog","robot initialized...");
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
		
    	InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainLog","autonomous mode...");
    	RPIComm.autoInit();
    	
    	autoSM.start();
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	RPIComm.updateValues();

		double gyroAngle = NavXSensor.getYaw();
				
		// send output data for test & debug
		String gyroAngleStr = String.format("%.2f", gyroAngle);
		String myString = new String("gyroAngle = " + gyroAngleStr);
		System.out.println(myString);
    	InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"Auto/autonomousPeriodic", myString);
   	
    	
    	autoSM.process();
    }

    public void teleopInit() {
    	InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainLog","teleop mode...");

    	RPIComm.teleopInit();
		CANDriveAssembly.teleopInit();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	RPIComm.updateValues();        
		CANDriveAssembly.teleopPeriodic();
   }

    public void disabledInit() {
    	autoSM.stop();
    	
    	InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"MainLog","robot disabled...");
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

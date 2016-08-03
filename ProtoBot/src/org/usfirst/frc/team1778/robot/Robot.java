
package org.usfirst.frc.team1778.robot;

import Systems.DriveTrain;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import Utility.LogitechF310;
import org.usfirst.frc.team1778.robot.Controller;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	protected Controller Controller;

	//Autonoumous Mode Choices
	final String doNothing = "Do Nothing";
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    Controller controller = new Controller();
    UserInput userInput = new UserInput();
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	//This is the driver control class.  All buttons an joysticks are here.
    	Controller Controller = new Controller();
    	
        DriveTrain DriveTrain = new DriveTrain(8,3);

    	
    	//The Autonomous mode chooser in smart dashboard. It will return a 
        chooser = new SendableChooser();
        chooser.addDefault("Do Nothing", doNothing);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. 
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
		//TODO: The Encoder need to be zeroed her.
    	//drive.zeroEncoders();

    	autoSelected = ((Command) chooser.getSelected());
		if (autoSelected != null) {
			autoSelected.start();
		}
		System.out.println("Auto selected: " + autoSelected);
    }

    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case "doNothing":
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    
    public void teleopInit(){
    	
    }
    
    public void teleopPeriodic() {
    	DriveControl drive = new DriveControl();
    	drive.calculateDrive(userInput.getLeftStickVert(),userInput.getRightStickHoriz(),userInput.getLeftButton(),false);
    }
    
    public void testPeriodic() {
    
    }
    
}

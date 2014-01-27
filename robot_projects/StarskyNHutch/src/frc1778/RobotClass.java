// RobotClass.java - top level robot code (aka main())
// VM automatically runs this class

package frc1778;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc1778.commands.CommandBase;
import frc1778.commands.DriveTest;

public class RobotClass extends IterativeRobot {
    Command autonomousCommand;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    //  instantiate the command used for the autonomous period
    
    //  Initialize all subsystems
        CommandBase.init();
        
        autonomousCommand = new DriveTest();
        
        // TODO: The user should be able to select more than one autonomous action
        // TODO: Selectable via the selectable chooser (smart dashboard)
        // TODO: Initialize the choices here - David should do this
    }

    public void autonomousInit() {
    //  schedule the autonomous command (example)
        autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Watchdog.getInstance().feed();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}

// RobotClass.java - top level robot code (aka main())
// VM automatically runs this class

package frc1778;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc1778.commands.AutoDriveAndShoot;
import frc1778.commands.CommandBase;

public class RobotClass extends IterativeRobot {
    public Command autonomousCommand;
    
    public void robotInit() {  
    //  Initialize all subsystems
        CommandBase.init();
        
        autonomousCommand = new AutoDriveAndShoot();
        SmartDashboard.putData(autonomousCommand);
        
        // TODO: The user should be able to select more than one autonomous action
        // TODO: Selectable via the selectable chooser (smart dashboard)
        // TODO: Initialize the choices here - David should do this
    }

    public void autonomousInit() {
        autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        autonomousCommand.cancel();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    public void testPeriodic() {
        LiveWindow.run();
    }
}

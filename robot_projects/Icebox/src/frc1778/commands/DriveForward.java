/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc1778.commands;

import edu.wpi.first.wpilibj.Timer;
import frc1778.RobotClass;

/**
 *
 * @author hudsodav000
 */
public class DriveForward extends CommandBase {
    
    private double startTime;
    private double driveTime = 5;
    private double endTime;
    
    RobotClass robot = new RobotClass();
    
    public DriveForward() {
        super("Drive Forward");
        requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("drive forward init");
        drive.percentMode();
        drive.brakeMode();
        drive.enable();
        startTime = Timer.getFPGATimestamp();
        endTime = startTime + driveTime;
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Timer.getFPGATimestamp() >= endTime) {
            return true;
        } else {
            return false;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        drive.setLeftRight(.5, .5);
    }
    
    // Called once after isFinished returns true
    protected void end() {
        System.out.println("It's gone!");
        drive.setLeftRight(0, 0);
        drive.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("DON'T INTERRUPT MY CODE!");
        end();
    }
}

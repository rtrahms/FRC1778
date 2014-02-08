/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc1778.commands;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author hudsodav000
 */
public class DriveBackward extends CommandBase {
    
    private double startTime;
    private static final double driveTime = 5;
    boolean timeUp;
    
    public DriveBackward() {
        super("Drive Backward");
        requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("drive backward init");
        drive.percentMode();
        drive.brakeMode();
        drive.enable();
        startTime = Timer.getFPGATimestamp();
        
        if(Timer.getFPGATimestamp() >= startTime + driveTime) {
            timeUp = true;
        } else {
            timeUp = false;
        }
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(timeUp = true) {
            return true;
        } else {
            return false;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        drive.setLeftRight(-.5, -.5);
    }
    
    // Called once after isFinished returns true
    protected void end() {
        System.out.println("I have to stop telling lies.");
        drive.setLeftRight(0, 0);
        drive.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("I WAS RUNNING!");
    }
}

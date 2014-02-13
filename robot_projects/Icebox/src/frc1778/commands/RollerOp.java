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
 * @author veilljai000
 */
public class RollerOp extends CommandBase {
    
    private double startTime;
    private double rollTime = 0.5;
    private double endTime;
    
    RobotClass robot = new RobotClass();
    
    public RollerOp() {
        super("RollerOP");
        requires(roller);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("RollerOp Init");
        roller.setSafety(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        roller.setRollerSpeed(oi.getGamepadRightStick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        roller.setRollerSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("Roller Teleop Interrupted");
        end();
    }
}
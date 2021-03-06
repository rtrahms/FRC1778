/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc1778.commands;

import frc1778.RobotClass;
/**
 *
 * @author veilljai000
 */
public class AutoDriveAndShoot extends CommandBase {
    RobotClass robot = new RobotClass();
    
    public AutoDriveAndShoot() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super ("AutoDriveAndShoot");
        requires(drive);
        // requires(camera);
        requires(gate);
        requires(roller);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    System.out.println("Auto D'n'S Drive init"); //drive setup
    drive.percentMode();
    drive.brakeMode();
    drive.enable();
    System.out.println("Auto D'n'S Roller init"); //roller setup
    roller.setRollerSpeed(.25);
    roller.setSafety(true);
    System.out.println("Auto D'n'S Gate init"); //gate setup
    gate.percentMode();
    gate.brakeMode();
    gate.enable();
    System.out.println("Auto D'n'S Camera init");//camera setup
        
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() { 
        //read camera
        //target value is (+)left, (-)right
        //<deadzone check>
        //if (+) execute drive left method
        //if (-) execute drive right method
        //if no hot target found after 7 then go to cold target and shoot
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc1778;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class IterativeRobot1778 extends IterativeRobot {
   
    //private Drive mDrive = null;
    //private Climber mClimber = null;
    //private Shooter mShooter = null;
    //private XboxController driveXbox = new XboxController(1);
    //private XboxController operatorXbox = new XboxController(2);
    
    private Compressor compressor = null;
    private Scheduler scheduler;
    private SendableChooser autonChooser = new SendableChooser();
   
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        scheduler = Scheduler.getInstance();
             
        System.out.println("entering robotInit");
       
        /*
        mDrive = Drive.getInstance();

        mShooter = Shooter.getInstance();

        mClimber = Climber.getInstance();

        compressor = new Compressor(RobotMap.PRESSURE_SWITCH, RobotMap.COMPRESSOR_RELAY);
        compressor.start();
        
        autonChooser.addDefault("Shoot", new ShootAuton(this));
        autonChooser.addObject("Troll", new TrollAuton());
        autonChooser.addObject("Do nothing", new FakeCommand());
        */
        SmartDashboard.putData("Autonomous Chooser", autonChooser);

       
    }
    
     /**
     * This function is called once during start of autonomous
     */
    public void autonomousInit() {
 
        System.out.println("entering autonomousInit");

        /*
        mDrive.resetGyro();
        mDrive.enablePid();
        mDrive.resetEncoders();
        mDrive.driveStraight(0);

        mClimber.unlock();
        mShooter.tiltDown();
        mClimber.retractTipper();
        mClimber.disableClimbingMode();
        mDrive.stop();
        mDrive.disengagePto();
        */

        ((Command) autonChooser.getSelected()).start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        scheduler.run();
    }
    
    /**
     * This function is called once during the start of operator control
     */
    public void teleopInit() {
        
        System.out.println("entering teleopInit");


        /*
        mDrive.resetGyro();
        mDrive.disablePid();

        mClimber.unlock();
        mShooter.tiltUp();
        mClimber.retractTipper();
        mClimber.disableClimbingMode();
        mDrive.highGear();
        
        new JoystickButton(operatorXbox, XboxController.BUTTON_LEFT_BUMPER).whenPressed(new ExtendRetractLoader());
        */
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        scheduler.run();        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

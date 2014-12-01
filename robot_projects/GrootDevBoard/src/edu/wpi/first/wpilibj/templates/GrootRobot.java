/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class GrootRobot extends SimpleRobot {

    // drive motors
    Victor mFrontLeft = new Victor(1,1);
    Victor mBackLeft = new Victor(1,2);
    Victor mFrontRight = new Victor(1,3);
    Victor mBackRight = new Victor(1,4);
    
    //RobotDrive drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
    RobotDrive drive = new RobotDrive(mFrontLeft, mFrontRight);
        
    // drive control
    Joystick leftStick = new Joystick(2);
    Joystick rightStick = new Joystick(1);
    //private Gyro gyro;
    
    // gamepad control
    Joystick gamepad = new Joystick(3);
     
    public GrootRobot() {
        
        // sensors
        //camera = new Camera1778();
        //ultrasonic = new Ultrasonic1778();
        
        // read switch and set robot position
        //positionSwitch = new DigitalInput(POSITION_SWITCH_SLOT);
       
        // drive system
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
       
        //gyro = new Gyro(1);       
    } 
    
    public void autonomous() {
        
        getWatchdog().setEnabled(false);
        
        while(isAutonomous()) {
            // No autonomous actions at this time
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {   
        
        getWatchdog().setEnabled(false);

        while(isEnabled() && isOperatorControl()) {
            
            // feed joystick values directly to drive system
             drive.tankDrive(leftStick, rightStick);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}

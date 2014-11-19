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
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
   // drive motors
    private Victor mFrontLeft;
    private Victor mFrontRight;
    private Victor mBackLeft;
    private Victor mBackRight;
    private RobotDrive drive;
        
    // drive control
    private Joystick leftStick;
    private Joystick rightStick;
    //private Gyro gyro;
    
    // gate and roller control
    private Joystick gamepad;
    
    public void autonomous() {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        // sensors
        //camera = new Camera1778();
        //ultrasonic = new Ultrasonic1778();
        
        // read switch and set robot position
        //positionSwitch = new DigitalInput(POSITION_SWITCH_SLOT);
       
        // drive system
        
        getWatchdog().setEnabled(false);
        mFrontLeft = new Victor(1,1);
        mBackLeft = new Victor(1,2);
        mFrontRight = new Victor(1,3);
        mBackRight = new Victor(1,4);

        
        //gyro = new Gyro(1);
        
        // drive control
        leftStick = new Joystick(2);
        rightStick = new Joystick(1);

        // gate & roller control
        gamepad = new Joystick(3);
        
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);

    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}

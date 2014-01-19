/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc1778;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.Watchdog;

public class TankArcade extends SimpleRobot {

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
    CANJaguar mFrontLeft;
    CANJaguar mFrontRight;
    CANJaguar mBackLeft;
    CANJaguar mBackRight;
    RobotDrive drive;
    Joystick leftStick;
    Joystick rightStick;
    //Gyro gyro;
    DriverStationLCD display;
    AnalogChannel ping;
    
    public TankArcade() throws CANTimeoutException {
        mFrontLeft = new CANJaguar(6);
        mBackLeft = new CANJaguar(2);
        mFrontRight = new CANJaguar(4);
        mBackRight = new CANJaguar(5);
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        
        ping = new AnalogChannel(2);
        //gyro = new Gyro(1);
        
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    }
    
    public void debug(String print) {
        display.println(DriverStationLCD.Line.kUser1,1,print);
        display.updateLCD();
    }
    
    public void autonomous() {
        getWatchdog().setEnabled(false);
        display.clear();
        while(isAutonomous()) {
            double distance = ping.getVoltage()/0.05;
            debug(Double.toString(distance));
        }
    }

    public void operatorControl() {
        getWatchdog().setEnabled(false);
        int mode = 0;
        while(isEnabled() && isOperatorControl()) {
            if(leftStick.getButton(Joystick.ButtonType.kTrigger) == true) {
                mode = 1-mode;
                while(leftStick.getButton(Joystick.ButtonType.kTrigger) == true) {
                }
            }
            if(mode == 0) {
                drive.tankDrive(leftStick, rightStick);
            } else {
                drive.arcadeDrive(leftStick);
            }
            //display.println(DriverStationLCD.Line.kUser1,1,Double.toString(angle));
            //display.updateLCD();
        }
    }
    
    public void test() {
        
    }
}
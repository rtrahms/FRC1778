// Andrew is love, Andrew is life.

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

//Import robotics libraries
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

// This is the RobotTemplate class. I guess.
public class RobotTemplate extends SimpleRobot {
    
    // Declare Objects
    CANJaguar  mFrontLeft;
    CANJaguar  mFrontRight;
    CANJaguar  mBackLeft;
    CANJaguar  mBackRight;
    RobotDrive drive;
    Joystick   leftStick;
    Joystick   rightStick;
    Relay[]    cannonTrigger;
    Relay      lightOne;
    Relay      lightTwo;
    
    // Initialize constant barrelOpenSec (how long the barrel will be open)
    final double barrelOpenSec = 3.0; 
    
    // Declare Variables
    boolean barrelOpen; 
    double  startTime;    
    double  currentTime; 
    int     barrelCount;  
    
    //Gyro gyro;
    //DriverStationLCD display;
    
    public RobotTemplate() throws CANTimeoutException {
        // Initialize Objects
        mFrontLeft    = new CANJaguar(5);
        mBackLeft     = new CANJaguar(6);
        mFrontRight   = new CANJaguar(7);
        mBackRight    = new CANJaguar(8);
        leftStick     = new Joystick(1);
        rightStick    = new Joystick(2);
        cannonTrigger = new Relay[3];
        lightOne = new Relay(1, 1);
        lightTwo = new Relay(1, 2);
        cannonTrigger[0] = new Relay(2, 1, Relay.Direction.kForward);
        cannonTrigger[1] = new Relay(2, 2, Relay.Direction.kForward);
        cannonTrigger[2] = new Relay(2, 3, Relay.Direction.kForward);
        barrelCount = 0;
        
        // Initialize RobotDrive class as drive
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    }
    
    // There was an autonomous code here. It's gone now.

    // Operator controlled period
    public void operatorControl() {
        getWatchdog().setEnabled(false);
        //display.clear();
        //gyro.reset();
        while(isEnabled() && isOperatorControl()) {
            //double angle = gyro.getAngle();
            drive.tankDrive(leftStick, rightStick);
            //display.println(DriverStationLCD.Line.kUser1,1,Double.toString(angle));
            //display.updateLCD();
            
            // If both triggers are pressed and the barrel isn't open
            if(leftStick.getButton(Joystick.ButtonType.kTrigger) && rightStick.getButton(Joystick.ButtonType.kTrigger) && !barrelOpen){ //verify that both triggers are pressed
                startTime = Timer.getFPGATimestamp();
                barrelOpen = true;
                cannonTrigger[barrelCount].set(Relay.Value.kForward);
            }
            
            // Ensures the Barrel is open for three seconds
            if(Timer.getFPGATimestamp() - startTime >= barrelOpenSec){
                barrelOpen = false;
                cannonTrigger[barrelCount].set(Relay.Value.kOff);
                barrelCount = (barrelCount+1) % 3;
            }
        }
    }
    
    /*public void cannonShoot() {       
        if(!barrelOpen){
            cannonTrigger.set(Relay.Value.kOff);
        }
    }*/
}

/*    public void autonomous() {
        System.out.println("Begin Autonomous");
        //drive.setSafetyEnabled(false); //removes 100ms timeout
        //System.out.println("Safety Disabled");
        getWatchdog().setEnabled(false);
        System.out.println("Watchdog Disabled");//protects motors
        for(int n = 0; n < 5000; n++) {
            System.out.println("Calling Drive n="+n);
            drive.tankDrive(0.5, -0.5); //turn
        }
        System.out.println("Loop finished");
        drive.tankDrive(0, 0);
        System.out.println("Exit Autonomous");
        
    }*/
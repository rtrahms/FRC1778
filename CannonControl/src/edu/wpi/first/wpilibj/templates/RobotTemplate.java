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
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

// This is the RobotTemplate class. I guess.
public class RobotTemplate extends SimpleRobot {
    
    // drive system
    CANJaguar  mFrontLeft;
    CANJaguar  mFrontRight;
    CANJaguar  mBackLeft;
    CANJaguar  mBackRight;
    RobotDrive drive;

    // controllers
    Joystick   leftStick;
    Joystick   rightStick;
    
    // firing system
    Relay[]    cannonTrigger;
    
    // safety lights
    Relay      lightOne;
    Relay      lightTwo;
    
    DigitalInput safetySwitch;
    boolean safetyDisabled = false;
    
    // Initialize constant barrelOpenSec (how long the barrel will be open)
    final double barrelOpenSec = 0.25; 
    
    // Declare Variables
    boolean barrelOpen; 
    double  startTime;    
    double  currentTime; 
    int     barrelCount;  
    
    //Gyro gyro;
    //DriverStationLCD display;
    
    public RobotTemplate() throws CANTimeoutException {
        // Initialize Objects
        
        mFrontLeft    = new CANJaguar(4);
        mBackRight     = new CANJaguar(5);
        mFrontRight   = new CANJaguar(6);
        mBackLeft    = new CANJaguar(8);
        
        leftStick     = new Joystick(1);
        rightStick    = new Joystick(2);
        
        // Firing System Relay controls on Digital sidecar
        // Digital Sidecar is MODULE 1
        cannonTrigger = new Relay[3];
        cannonTrigger[0] = new Relay(1, 1);
        cannonTrigger[1] = new Relay(1, 2);
        cannonTrigger[2] = new Relay(1, 3);
        barrelCount = 0;
        
        // Light relays on digital sidecar
        lightOne = new Relay(1, 4);
        lightTwo = new Relay(1, 5);
        
        safetySwitch = new DigitalInput(1,1);
        
        // Initialize RobotDrive class as drive
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        //drive = new RobotDrive(mBackLeft,mBackRight);
    }
    
    // There was an autonomous code here. It's gone now.

    // Operator controlled period
    public void operatorControl() {
        getWatchdog().setEnabled(false);
                 
        while(isEnabled() && isOperatorControl()) {

            // check state of safety switch
            safetyDisabled = safetySwitch.get();
            
            if (safetyDisabled) {
                
                // allow tank to drive    
                drive.tankDrive(leftStick, rightStick);

                // turn on warning lights
                lightOne.set(Relay.Value.kForward);
                lightTwo.set(Relay.Value.kForward);
             
                // If both triggers are pressed and the barrel isn't open
                if(leftStick.getButton(Joystick.ButtonType.kTrigger) && 
                   rightStick.getButton(Joystick.ButtonType.kTrigger) && 
                   !barrelOpen)
                { 
                    //verify that both triggers are pressed
                    startTime = Timer.getFPGATimestamp();
                    barrelOpen = true;
                    System.out.println("triggered - barrelCount = " + barrelCount);
                    cannonTrigger[barrelCount].set(Relay.Value.kForward);
                }

                // Ensures the Barrel is open for barrelOpenSec before closing          
                if ((Timer.getFPGATimestamp() - startTime > barrelOpenSec) && barrelOpen) {
                    barrelOpen = false;
                    cannonTrigger[barrelCount].set(Relay.Value.kOff);
                    barrelCount = (barrelCount+1) % 3;
                }
            } else {
                // turn off warning lights
                lightOne.set(Relay.Value.kOff);
                lightTwo.set(Relay.Value.kOff);
            }
        }     
    }
    
    public void test() {
        while(isTest() && isEnabled()) {
            LiveWindow.run();
            Timer.delay(0.1);
        }
    }
    
    /*public void cannonShoot() {       
        if(!barrelOpen){
            cannonTrigger.set(Relay.Value.kOff);
        }
    }*/
}

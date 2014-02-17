/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc1778;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class SimpleFullControl extends SimpleRobot {
    
    // PID coefficients for gate jaguar
    final double PID_GATE[] = { 0.5, 0.45, 0.6 };
    
    // potentiometer values for gate position
    final double GATE_CLOSED = 0.8;
    final double GATE_OPEN = 0.57;
    
    // drive motors
    CANJaguar mFrontLeft;
    CANJaguar mFrontRight;
    CANJaguar mBackLeft;
    CANJaguar mBackRight;
    
    // gate and roller motors
    CANJaguar gate;
    CANJaguar rollers;
    
    RobotDrive drive;
    
    // drive control
    Joystick leftStick;
    Joystick rightStick;
    Gyro gyro;
    
    // gate and roller control
    Joystick gamepad;
    
    public SimpleFullControl() throws CANTimeoutException {  
        
        // drive system
        mFrontLeft = new CANJaguar(2);
        mBackLeft = new CANJaguar(1);
        mFrontRight = new CANJaguar(8);
        mBackRight = new CANJaguar(5);
        gyro = new Gyro(1);

        // set up gate motor (PID-based)
        gate = new CANJaguar(6);
        getWatchdog().setEnabled(false);
        gate.changeControlMode(CANJaguar.ControlMode.kPosition);
        //gate.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
        //gate.setPID(PID_GATE[0], PID_GATE[1], PID_GATE[2]);

        // set up roller motor
        rollers = new CANJaguar(4);
        rollers.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        
        getWatchdog().setEnabled(false);

        // drive control
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);

        // gate & roller control
        gamepad = new Joystick(3);
        
        //gyro = new Gyro(1);
        
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        double Kp = 0.03;
        getWatchdog().setEnabled(false);
        gyro.reset();
        double angle = 0;
        while(isAutonomous()) {
            angle = gyro.getAngle();
            drive.drive(-.2, -angle*Kp);
            System.out.println("angle is: " + angle);
            
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        getWatchdog().setEnabled(false);
        int mode = 0;
        
        // enable control for gate & rollers
        double lock_pos = 0.275;
        double step = 0.7;
        double increment = 0.0;
        double pot_pos = 0;
        try {    
            gate.enableControl();
            rollers.enableControl();
        } 
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        while(isEnabled() && isOperatorControl()) {
            //************* drive control section
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
            
            //*********** gate and roller control section
            increment = gamepad.getRawAxis(2)*step;
            lock_pos += increment;
            lock_pos = Math.max(Math.min(lock_pos, 0.4),0.1);
            
            System.out.println("Current: " + pot_pos + "  :  Goto: " + lock_pos);
            // gate motor operation
            try {
                pot_pos = gate.getPosition();
                //System.out.println("Pot pos = "+pot_pos);
                gate.setX(increment);
                rollers.setX(gamepad.getRawAxis(4));
            } catch(CANTimeoutException e) {
                e.printStackTrace();
            }
         }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}

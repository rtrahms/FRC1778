package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class RobotTemplate extends SimpleRobot {
    CANJaguar rollers;
    CANJaguar mFrontLeft;
    CANJaguar mFrontRight;
    CANJaguar mBackLeft;
    CANJaguar mBackRight;
    CANJaguar gate;
    //CANJaguar rollers;
    RobotDrive drive;
    Joystick leftStick;
    Joystick rightStick;
    Joystick gamepad;
    
    // PID coefficients for gate jaguar
    final double pid_gate[] = { 0.5, 0.45, 0.6 };
    
    public RobotTemplate() throws CANTimeoutException {
        mFrontLeft = new CANJaguar(2);
        mBackLeft = new CANJaguar(1);
        mFrontRight = new CANJaguar(8);
        mBackRight = new CANJaguar(5);
        
        // set up gate motor (PID-based)
        gate = new CANJaguar(4);
        gate.changeControlMode(CANJaguar.ControlMode.kPosition);
        gate.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
        gate.setPID(pid_gate[0],pid_gate[1], pid_gate[2]);

        // set up roller motor
        rollers = new CANJaguar(6);
        rollers.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        
        getWatchdog().setEnabled(false);
        
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        gamepad = new Joystick(3);
        
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    }
    
    public void autonomous() {
        getWatchdog().setEnabled(false);
        while(isAutonomous()) {
            
        }
    }

    public void operatorControl() {
        getWatchdog().setEnabled(false);
        double lock_pos = 0;
        double step = 0.005;
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
            
            // read gamepad joystick (Y-axis) and update target for gate motor
            increment = gamepad.getRawAxis(2)*step;
            lock_pos += increment;
            lock_pos = Math.max(Math.min(lock_pos, 1),0);
            System.out.println("" + increment + "  :  " + lock_pos);
            // gate motor operation
            try {
                pot_pos = gate.getPosition();
                //System.out.println("Pot pos = "+pot_pos);
                gate.setX(lock_pos);
                rollers.setX(gamepad.getRawAxis(4));
            } catch(CANTimeoutException e) {
                e.printStackTrace();
            }
            /*
            // roller motor operation
            try {
                
            } catch (CANTimeoutException e) {
                e.printStackTrace();
            }
            */
        }
    }
    
    public void test() {
        
    }
}
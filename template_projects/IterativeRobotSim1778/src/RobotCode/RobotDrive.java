/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RobotCode;


/**
 *
 * @author aubrey
 */
public class RobotDrive {
    Jaguar jag1;
    Jaguar jag2;
    Jaguar jag3;
    Jaguar jag4;
    protected double m_sensitivity;
    double leftMotorSpeed;
    double rightMotorSpeed;
    
    public RobotDrive(int LeftMotorChannel, int RightMotorChannel) {
        jag1 = new Jaguar(LeftMotorChannel);
        jag2 = new Jaguar(RightMotorChannel);
        
    }
    
    public RobotDrive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor) {
        jag1 = new Jaguar(frontLeftMotor);
        jag2 = new Jaguar(rearLeftMotor);
        jag3 = new Jaguar(frontRightMotor);
        jag4 = new Jaguar(rearRightMotor);
        
    }
    
    public void arcadeDrive(double moveValue, double rotateValue) {
        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);
        
        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        jag1.set(leftMotorSpeed);
        jag2.set(rightMotorSpeed);
    }
    public void arcadeDrive(Joystick joystick1) {
        arcadeDrive(joystick1.getRawAxis(1), joystick1.getRawAxis(2));
    }
    
    public void tankDrive(double leftValue, double rightValue) {
        leftValue = limit(leftValue);
        rightValue = limit(rightValue);
        if (leftValue >= 0.0) {
            leftValue = (leftValue * leftValue);
        } else {
            leftValue = -(leftValue * leftValue);
        }
        if (rightValue >= 0.0) {
            rightValue = (rightValue * rightValue);
        } else {
            rightValue = -(rightValue * rightValue);
        }
        
        jag1.set(leftValue);
        jag2.set(rightValue);
    }
    
    public void tankDrive(Joystick joystick1, Joystick joystick2) {
        tankDrive(joystick1.getRawAxis(1), joystick2.getRawAxis(1));
    }   
    
    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle) {
        
    }
    
    public void mecanumDrive_Polar(double magnitude, double direction, double rotation) {
        
    }
    
    public void drive(double outputMagnitude, double curve) {
          double leftOutput, rightOutput;

        if (curve < 0) {
            double value = Math.log(-curve);
            double ratio = (value - m_sensitivity) / (value + m_sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }
            leftOutput = outputMagnitude / ratio;
            rightOutput = outputMagnitude;
        } else if (curve > 0) {
            double value = Math.log(curve);
            double ratio = (value - m_sensitivity) / (value + m_sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }
            leftOutput = outputMagnitude;
            rightOutput = outputMagnitude / ratio;
        } else {
            leftOutput = outputMagnitude;
            rightOutput = outputMagnitude;
        }
     
    }
    
    protected static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }
}
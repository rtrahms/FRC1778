package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Utility;


//Chill Out 1778 class for controlling the drivetrain

public class DriveAssembly {
	
	// Speed controller IDs
	private final int LEFT_FRONT_TALON_ID = 3;
	private final int LEFT_REAR_TALON_ID = 4;
	private final int RIGHT_FRONT_TALON_ID = 8;
	private final int RIGHT_REAR_TALON_ID = 7;
	private final int LATERAL_TALON_ID1 = 5;
	private final int LATERAL_TALON_ID2 = 6;
	private final int LEFT_GRABBER_TALON_ID = 9;
	private final int RIGHT_GRABBER_TALON_ID = 10;
	
	// joystick axis ids
	private final int JOY_X_AXIS = 0;
	private final int JOY_Y_AXIS = 1;
	private final int JOY_Z_AXIS = 2;
	private final int JOY_SLIDER_AXIS = 3;
	
	// joystick device ids
	private final int LEFT_JOYSTICK_ID = 0;
	private final int RIGHT_JOYSTICK_ID = 1;
	
	//threshold constants
	private final double LATERAL_DEADZONE = 0.3;
	private final double DRIVE_DEADZONE = 0.2;
	private final double EQUALIZATION_THRESHOLD = 0.1;
	
	//other constants
	private final boolean USE_SQUARED_INPUTS = true;
	private final double GYRO_CORRECT_COEFF = 0.125;
	
	// speed controllers and drive class
	private CANTalon mFrontLeft, mBackLeft, mFrontRight, mBackRight;
	private CANTalon mLateral_1, mLateral_2;
    private RobotDrive drive;
    
    // drive control
    private Joystick leftStick, rightStick;
	
    // timers
    private long startTimeUs;
    
    // sensors and feels
    private Gyro gyro;
    
	// constructor - tank drive
	public DriveAssembly()
	{
        mFrontLeft = new CANTalon(LEFT_FRONT_TALON_ID);
        mBackLeft = new CANTalon(LEFT_REAR_TALON_ID);
        mFrontRight = new CANTalon(RIGHT_FRONT_TALON_ID);
        mBackRight = new CANTalon(RIGHT_REAR_TALON_ID);
        
        mLateral_1 = new CANTalon(LATERAL_TALON_ID1);
        mLateral_1.enableBrakeMode(true);
        mLateral_2 = new CANTalon(LATERAL_TALON_ID2);
        mLateral_2.enableBrakeMode(true);
        
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        
        leftStick = new Joystick(LEFT_JOYSTICK_ID);
        rightStick = new Joystick(RIGHT_JOYSTICK_ID);
        
        gyro = new Gyro(0);
	}


	public void autoInit()
	{
		// initialize drive gyro
        gyro.reset();	
        
        // initialize auto drive timer
		startTimeUs = Utility.getFPGATime();
	}
	
	public void autoPeriodic() {
		// todo - autonomous operation of drive
		/*
		double currentPeriodSec = (Utility.getFPGATime() - startTimeUs)/1000000.0;
		
		// if we are still in the drive time period
		if (currentPeriodSec < AUTO_DRIVE_TIME_SEC) {
			double driveAngle = 0;

			System.out.println("Time (sec) = " + String.format("%.1f",currentPeriodSec) + " Angle =" + String.format("%.2f",driveAngle));
			
			driveDirection(driveAngle, AUTO_DRIVE_SPEED);
		} else {
			// beyond time, stop robot
			drive.drive(0.0, 0.0);
		}
		*/
	}
		
	public void teleopInit() {
		gyro.reset();
	}
	
	public void teleopPeriodic() {
		
		// Left Stick Throttle Control
		double throttleVal = 1.0 - ((leftStick.getRawAxis(JOY_Z_AXIS))+1.0)/2.0;
		
		double leftValue = throttleVal*leftStick.getY();
		double rightValue = throttleVal*rightStick.getY();
		double strafeValue = throttleVal*leftStick.getX();
		
		// Deadzones
		if(Math.abs(leftValue) <= DRIVE_DEADZONE) {
			leftValue = 0;
		}
		if(Math.abs(rightValue) <= DRIVE_DEADZONE) {
			rightValue = 0;
		}
		if(Math.abs(strafeValue) <= LATERAL_DEADZONE) {
			strafeValue = 0;
		}
		
		// Throttle Equalization Compensation
		if(Math.abs(leftValue-rightValue) <= EQUALIZATION_THRESHOLD) {
			double avgValue = (leftValue+rightValue)/2;
			//System.out.println("EQUALIZING: "+avgValue);
			leftValue = avgValue;
			rightValue = avgValue;
		}
		
		// Slowmode
		if(leftStick.getRawButton(1)) {
			leftValue /= 1.5;
			rightValue /= 1.5;
			strafeValue /= 1.5;
		}
		
		//Set the drive train 
		drive(leftValue, rightValue, strafeValue);
		
	}
	
	public void drive(double left, double right, double strafe) {
		drive.tankDrive(left, right, USE_SQUARED_INPUTS);
		mLateral_1.set(strafe);
		mLateral_2.set(strafe);
	}
	
	public void driveDirection(double angle, double speed) {
		double gyroAngle = getAngle();
		double driveAngle = (angle-gyroAngle)*GYRO_CORRECT_COEFF;
		drive(driveAngle+speed, -driveAngle+speed, 0);
	}
	
	public void turnToDirection(double angle, double power) {
		double gyroAngle = getAngle();
		double driveAngle = (angle-gyroAngle)*(1/360)*power;
		drive(driveAngle, -driveAngle, 0);
	}
	
	public double getAngle() {
		double angle = gyro.getAngle();
		return angle;
	}
	
}

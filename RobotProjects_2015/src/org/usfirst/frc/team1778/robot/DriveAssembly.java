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
	
	//dead zone constant
	private final double DEADZONE = .5;
	
	// autonomous constants
	// drive time 3 seconds
	private final long AUTO_DRIVE_TIME_SEC = 4;
	private final double AUTO_DRIVE_SPEED = -0.5;
	private final double AUTO_DRIVE_CORRECT_COEFF = 0.125;
	
    // drive throttle (how fast the drivetrain moves, and direction)
    //private final double DRIVE_STEP_MAGNITUDE_DEFAULT = 1.0;
    //private final double DRIVE_STEP_POLARITY_DEFAULT = 1.0;
    // minimum motor increment (for joystick dead zone)
    //private final double MIN_INCREMENT = 0.1;
		
	// speed controllers and drive class
	private CANTalon mFrontLeft, mBackLeft, mFrontRight, mBackRight;
	private CANTalon mLateral_1, mLateral_2;
    private RobotDrive drive;
    
    // drive control
    private Joystick leftStick, rightStick;
    private Joystick arcadeStick;
	
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
		
		double currentPeriodSec = (Utility.getFPGATime() - startTimeUs)/1000000.0;
		
		// if we are still in the drive time period
		if (currentPeriodSec < AUTO_DRIVE_TIME_SEC)
		{
			double gyroAngle = gyro.getAngle();
			double driveAngle = -gyroAngle * AUTO_DRIVE_CORRECT_COEFF;

			System.out.println("Time (sec) = " + String.format("%.1f",currentPeriodSec) + " Angle =" + String.format("%.2f",driveAngle));
			//drive.drive(AUTO_DRIVE_SPEED,driveAngle);
			drive.tankDrive(driveAngle*AUTO_DRIVE_CORRECT_COEFF+AUTO_DRIVE_SPEED, -driveAngle*AUTO_DRIVE_CORRECT_COEFF+AUTO_DRIVE_SPEED);
		}
		else
		{
			// beyond time, stop robot
			drive.drive(0.0, 0.0);
		}
	}
		
	public void teleopPeriodic() {
		
		// left stick z-axis will serve as throttle control
		// normalized (0.0-1.0)
		//double throttleVal = 1.0f;
		double throttleVal = 1.0 - ((leftStick.getRawAxis(JOY_Z_AXIS))+1.0)/2.0;
		//double throttleVal = (arcadeStick.getRawAxis(JOY_SLIDER_AXIS) + 1.0)/2.0;
		
		boolean useSquaredInputs = true;
		
		//*****************  TANK DRIVE SECTION (uses two y axes) **********/
		// control robot forward and turn movement with y-axis and twist-axis
		
		double leftValue = throttleVal*leftStick.getY();
		double rightValue = throttleVal*rightStick.getY();
		if(leftStick.getRawButton(1)) {
			leftValue /= 1.5;
			rightValue /= 1.5;
		}
		//drive.tankDrive(leftStick, rightStick);
		drive.tankDrive(leftValue, rightValue, useSquaredInputs);
		
		//*****************  TANK DRIVE SECTION ****************************/
		
		//**************  ARCADE DRIVE SECTION (uses one Y and one Z) **********/
		/*
		double moveValue = throttleVal*arcadeStick.getRawAxis(JOY_Y_AXIS);
		double rotateValue = throttleVal*arcadeStick.getRawAxis(JOY_Z_AXIS);
		drive.arcadeDrive(moveValue, rotateValue);
		*/
		//**************  ARCADE DRIVE SECTION ****************************/
		
		//************** STRAFE DRIVE SECTION  (uses one X axis) *********/
		// control strafe speed controller with x-axis (use left joystick)		
		double strafeValue = throttleVal*leftStick.getX();
		//double strafeValue = throttleVal*arcadeStick.getRawAxis(JOY_X_AXIS);
		if(Math.abs(strafeValue) <= DEADZONE){
			strafeValue = 0;
		}
		mLateral_1.set(strafeValue);
		mLateral_2.set(strafeValue);
		/*************** STRAFE DRIVE SECTION ****************************/

	}
		
}

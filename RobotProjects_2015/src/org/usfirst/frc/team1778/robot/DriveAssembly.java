package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;

//Chill Out 1778 class for controlling the drivetrain

public class DriveAssembly {

	// Speed controller IDs
	private final int LEFT_FRONT_TALON_ID = 2;
	private final int LEFT_REAR_TALON_ID = 3;
	private final int RIGHT_FRONT_TALON_ID = 4;
	private final int RIGHT_REAR_TALON_ID = 5;
	private final int LATERAL_TALON_ID = 6;
	
	// joystick ids
	private final int JOYSTICK_ID = 0;
	
    // drive throttle (how fast the drivetrain moves, and direction)
    private final double DRIVE_STEP_MAGNITUDE_DEFAULT = 1.0;
    private final double DRIVE_STEP_POLARITY_DEFAULT = 1.0;
    // minimum motor increment (for joystick dead zone)
    private final double MIN_INCREMENT = 0.1;
	
	private boolean hDriveEnabled;
	
	// speed controllers and drive class
	private TalonSRX mFrontLeft, mBackLeft, mFrontRight, mBackRight;
	private TalonSRX mLateral;
    private RobotDrive drive;
    
    // drive control
    private Joystick joyStick;
	
	// constructor - tank drive
	public DriveAssembly()
	{
        mFrontLeft = new TalonSRX(LEFT_FRONT_TALON_ID);
        mBackLeft = new TalonSRX(LEFT_REAR_TALON_ID);
        mFrontRight = new TalonSRX(RIGHT_FRONT_TALON_ID);
        mBackRight = new TalonSRX(RIGHT_REAR_TALON_ID);
        
        drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        
        joyStick = new Joystick(JOYSTICK_ID);
        
        hDriveEnabled = false;
	}

	// constructor - H drive
	public DriveAssembly(boolean hDriveEnabled)
	{
        mFrontLeft = new TalonSRX(LEFT_FRONT_TALON_ID);
        mBackLeft = new TalonSRX(LEFT_REAR_TALON_ID);
        mFrontRight = new TalonSRX(RIGHT_FRONT_TALON_ID);
        mBackRight = new TalonSRX(RIGHT_REAR_TALON_ID);
        
        this.hDriveEnabled = hDriveEnabled;
        
        if (hDriveEnabled)
        {
        	mLateral = new TalonSRX(LATERAL_TALON_ID);
        	drive = new RobotDrive(mFrontLeft, mBackLeft, mFrontRight, mBackRight);
        }

        joyStick = new Joystick(JOYSTICK_ID);
	}

	public void autoPeriodic()
	{
		// todo
	}
		
	public void teleopPeriodic()
	{
		if (!hDriveEnabled)
	        drive.arcadeDrive(joyStick);
		else
			holonomicDrive();
	}
	
	private void tankDrive()
	{
		/*
        double driveStep = DRIVE_STEP_POLARITY_DEFAULT * DRIVE_STEP_MAGNITUDE_DEFAULT;
        double leftDriveIncrement = leftStick.getRawAxis(2) * driveStep;
        if (Math.abs(leftDriveIncrement) < MIN_INCREMENT)
            leftDriveIncrement = 0.0;
        double rightDriveIncrement = rightStick.getRawAxis(2) * driveStep;
        if (Math.abs(rightDriveIncrement) < MIN_INCREMENT)
            rightDriveIncrement = 0.0;
 
        drive.tankDrive(leftStick, rightStick);
        */
	}
	
	private void holonomicDrive()
	{
		// todo - incorporate lateral drive motor
	}
	
}

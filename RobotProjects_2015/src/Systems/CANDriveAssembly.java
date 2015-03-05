package Systems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Utility;


//Chill Out 1778 class for controlling the drivetrain

public class CANDriveAssembly {
	
	private static boolean initialized = false;
	
	// Speed controller IDs
	private static final int LEFT_FRONT_TALON_ID = 3;
	private static final int LEFT_REAR_TALON_ID = 4;
	private static final int RIGHT_FRONT_TALON_ID = 8;
	private static final int RIGHT_REAR_TALON_ID = 7;
	private static final int LATERAL_TALON_ID1 = 5;
	private static final int LATERAL_TALON_ID2 = 6;
	private static final int LEFT_GRABBER_TALON_ID = 9;
	private static final int RIGHT_GRABBER_TALON_ID = 10;
	
	// joystick axis ids
	private static final int JOY_X_AXIS = 0;
	private static final int JOY_Y_AXIS = 1;
	private static final int JOY_Z_AXIS = 2;
	private static final int JOY_SLIDER_AXIS = 3;
	
	// joystick device ids
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	
	//threshold constants
	private static final double LATERAL_DEADZONE = 0.3;
	private static final double DRIVE_DEADZONE = 0.2;
	private static final double EQUALIZATION_THRESHOLD = 0.1;
	
	private static final long AUTO_DRIVE_TIME_SEC = 4;
	private static final double AUTO_DRIVE_SPEED = -0.5;
	private static final double AUTO_DRIVE_CORRECT_COEFF = 0.125;
	
	//other constants
	private static final boolean USE_SQUARED_INPUTS = true;
	private static final double GYRO_CORRECT_COEFF = 0.125;
	
	// speed controllers and drive class
	private static CANTalon mFrontLeft, mBackLeft, mFrontRight, mBackRight;
	private static CANTalon mLateral_1, mLateral_2;
    private static RobotDrive drive;
    
    // drive control
    private static Joystick leftStick, rightStick;
	
    // timers
    private static long startTimeUs;
        
	// static initializer
	public static void initialize()
	{
		if (!initialized) {
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
	        
	        GyroSensor.initialize();
	        
	        initialized = true;
		}
	}


	public static void autoInit()
	{
		// initialize drive gyro
		GyroSensor.reset();
        
        // initialize auto drive timer
		//startTimeUs = Utility.getFPGATime();
	}
	
	/*
	public static void autoPeriodicStraight() {
		// autonomous operation of drive straight
		
		double gyroAngle = gyro.getAngle();
		double driveAngle = -gyroAngle * AUTO_DRIVE_CORRECT_COEFF;
		//System.out.println("Time (sec) = " + String.format("%.1f",currentPeriodSec) + " Angle =" + String.format("%.2f",driveAngle));

		drive.tankDrive(driveAngle*AUTO_DRIVE_CORRECT_COEFF+AUTO_DRIVE_SPEED, 
						 -driveAngle*AUTO_DRIVE_CORRECT_COEFF+AUTO_DRIVE_SPEED);
	}
	*/

	public static void autoPeriodicStraight(double speed) {
		// autonomous operation of drive straight
		
		double gyroAngle = GyroSensor.getAngle();
		double driveAngle = -gyroAngle * AUTO_DRIVE_CORRECT_COEFF;
		//System.out.println("Time (sec) = " + String.format("%.1f",currentPeriodSec) + " Angle =" + String.format("%.2f",driveAngle));

		speed *= -1.0;
		drive.tankDrive(driveAngle*AUTO_DRIVE_CORRECT_COEFF+speed, 
						-driveAngle*AUTO_DRIVE_CORRECT_COEFF+speed);
	}

	public static void autoStop() {
		drive.drive(0.0, 0.0);
	}
		
	public static void teleopInit() {
		GyroSensor.reset();
	}
	
	public static void teleopPeriodic() {
		
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
	
	private static void drive(double left, double right, double strafe) {
		drive.tankDrive(left, right, USE_SQUARED_INPUTS);
		mLateral_1.set(strafe);
		mLateral_2.set(strafe);
	}
	
	public static void driveDirection(double angle, double speed) {
		double gyroAngle = getAngle();
		double driveAngle = (angle-gyroAngle)*GYRO_CORRECT_COEFF;
		drive(driveAngle+speed, -driveAngle+speed, 0);
	}
	
	public static void turnToDirection(double angle, double power) {
		double gyroAngle = getAngle();
		double driveAngle = (angle-gyroAngle)*(1/360)*power;
		drive(driveAngle, -driveAngle, 0);
	}
	
	public static void rotateLeft(double speed) {
		drive(-speed, speed, 0);
	}

	public static void rotateRight(double speed) {
		drive(speed, -speed, 0);
	}

	private static double getAngle() {
		
		double angle = GyroSensor.getAngle();
		return angle;
	}
	
}

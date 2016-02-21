package Systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class AutoShooterAssembly {

	private static boolean initialized = false;

	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	private static final int TARGETING_BUTTON_ID = 2;
	private static final double AUTO_RESET_THRESHOLD = 0.5;

	private static Joystick leftJoy, rightJoy;

	private static double angularVelocity, forwardVelocity;

	private static boolean targeting = false;
	private static boolean isCalibrated = false;
	private static int consecCalibrate = 0;

	public static void initialize() {
		if (!initialized) {
			leftJoy = new Joystick(LEFT_JOYSTICK_ID);
			rightJoy = new Joystick(RIGHT_JOYSTICK_ID);

			setCalibration(false);

			initialized = true;
		}
	}

	public static void teleopInit() {
		setCalibration(false);
	}

	public static void teleopPeriodic() {
		// one button to start auto trigger mode
		if ((leftJoy.getRawButton(TARGETING_BUTTON_ID) || rightJoy
				.getRawButton(TARGETING_BUTTON_ID)) && !targeting) {
			System.out.println("AutoShooterAssembly:  autotargeting mode ON");
			setCalibration(true);
		}

		// either joystick trigger pressed to stop auto targeting mode
		if ((Math.abs(leftJoy.getRawAxis(1)) > AUTO_RESET_THRESHOLD || Math.abs(rightJoy
				.getRawAxis(1)) > AUTO_RESET_THRESHOLD) && targeting) {
			System.out.println("AutoShooterAssembly:  autotargeting mode OFF");
			setCalibration(false);
		}

		// if we are in targeting mode, pass through calibration method
		if (targeting)
			calibrateShooter();
		
		// is it calibrated yet?
		if (isCalibrated) {
			
			// shoot the catapult if ready to shoot!
			if (CatapultAssembly.readyToShoot())
			{
				System.out.println("FIRING CATAPULT!");
				//CatapultAssembly.shoot();
			}
			
			// reset targeting flag and isCalibrated flags
			targeting = false;
			isCalibrated = false;
		}
	}

	public static void setCalibration(boolean switchOn)
	{
		targeting = switchOn;
		isCalibrated = false;
		consecCalibrate = 0;
	}
	
	
	// calibrates the shooter in x and y prior to shooting catapult
	public static void calibrateShooter()
	{
		// if no targets exist to calibrate against
		if (!NetworkCommAssembly.hasTarget())  {
			
			// turn calibration mode off
			System.out.println("AutoShooterAssembly: no targets, turning calibration off");
			setCalibration(false);
		}
		else {
			// first calibrate X position
			if (calibratePosX()) {
				// second calibrate Y position
				if (calibratePosY()) {
					// both are calibrated - shoot the ball!
					System.out.println("AutoTarget: SHOOTING!");
					consecCalibrate++;
					if (consecCalibrate > 10) {
						// set calibrated flag (ready to shoot!)
						isCalibrated = true;
					}
				} else {
					consecCalibrate = 0;
				}
			} else {
				consecCalibrate = 0;
			}
		}
	}
	
	public static boolean isCalibrated() {
		return isCalibrated;
	}
	
	public static void disabledInit() {
		if (!initialized)
			initialize();
	}

	private static boolean calibratePosX() {
		angularVelocity = NetworkCommAssembly.getAngularVelocity();

		// if in the margin is small enough
		if (angularVelocity == 0) {
			// no further x movement necessary - STOP
			CANDriveAssembly.driveForward(0);

			System.out.println("LATERAL X CALIBRATED!");
			return true;
		} else {
			System.out.println("angularVelocity = " + angularVelocity);

			// rotate robot left or right to get better actual X position
			CANDriveAssembly.rotate(angularVelocity);
		}

		return false;
	}

	private static boolean calibratePosY() {
		forwardVelocity = NetworkCommAssembly.getForwardVelocity();

		// if in the margin is small enough
		if (forwardVelocity == 0) {
			// no further x movement necessary - STOP
			CANDriveAssembly.driveForward(0);

			System.out.println("AXIAL Y CALIBRATED!");
			return true;
		} else {
			System.out.println("forwardVelocity = " + forwardVelocity);

			// rotate robot left or right to get better actual X position
			CANDriveAssembly.driveForward(forwardVelocity);
		}

		return false;
	}

	private static boolean calibratePosXY() {
		forwardVelocity = NetworkCommAssembly.getForwardVelocity();
		angularVelocity = NetworkCommAssembly.getAngularVelocity();

		// if in the margin is small enough
		if (forwardVelocity == 0) {
			// no further x movement necessary - STOP
			CANDriveAssembly.driveVelocity(forwardVelocity, angularVelocity);

			System.out.println("AXIAL X AND Y CALIBRATED!");
			return true;
		} else {
			System.out.println("forwardVelocity = " + forwardVelocity
					+ " angularVelocity = " + angularVelocity);

			// rotate robot left or right to get better actual X position
			CANDriveAssembly.driveForward(forwardVelocity);
		}

		return false;
	}

}

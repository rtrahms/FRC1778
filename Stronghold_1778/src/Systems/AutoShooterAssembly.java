package Systems;

public class AutoShooterAssembly {

	private static boolean initialized = false;

	private static boolean isCalibrated = false;
	private static double driveLeft, driveRight;
	

	public static void initialize() {
		if (!initialized) {

			reset();
			
			initialized = true;
		}
	}

	public static void autoInit() {
		reset();
	}	
	
	public static void reset() {
		isCalibrated = false;
		driveLeft = 0;
		driveRight = 0;		
	}
	
	// calibrates the shooter in x and y prior to shooting catapult
	public static void calibrateShooter()
	{
		// if no target data, return false
		if (!NetworkCommAssembly.hasTarget()) {
			System.out.println("AutoShooterAssembly: no targets, resetting calibration");
			reset();
		}
		else {
			// target data exists, and is centered
			if (NetworkCommAssembly.targetCentered()) {
				isCalibrated = true;
				driveLeft = 0;
				driveRight = 0;
			} 
			else {
			// target data exists, but is not centered
				isCalibrated = false;
				driveLeft = NetworkCommAssembly.getLeftDriveValue();
				driveRight = NetworkCommAssembly.getRightDriveValue();
			}
			
			//System.out.println("AutoShooterAssembly: calibrating... driveLeft: " + driveLeft + " driveRight: " + driveRight);
		}
			
		// drive the robot based on guidance from the calibration
		CANDriveAssembly.drive(driveLeft, driveRight, 0);
	}
	
	public static boolean isCalibrated() {
		return isCalibrated;
	}	

}

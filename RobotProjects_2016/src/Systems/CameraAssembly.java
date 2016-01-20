package Systems;

import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class CameraAssembly {
	private static boolean initialized = false;
	
	private static AxisCamera myCamera;
	
	private static final long TARGET_SCAN_PERIOD_USEC = 250000;
	private static final int TARGET_CENTER_TOLERANCE_PIXELS = 100;
	private static final int OFF_TARGET_VALUE = 1000;
	
	private static boolean scanningMode;
	private static long initTime, currentTime;
	
	private static int targetCenterX, targetCenterY;
	private static boolean targetSeen;
	
	private static HSLImage capturedImage, referenceTargetImage;

	public static void initialize() {
		if (!initialized)
		{
			myCamera = new AxisCamera("10.17.78.21");
			scanningMode = false;
			initTime = Utility.getFPGATime();
			
			targetCenterX = OFF_TARGET_VALUE;
			targetCenterY = OFF_TARGET_VALUE;
			targetSeen = false;
			
			initialized = true;
		}
	}
	
	public static void setScanMode(boolean mode)
	{
		scanningMode = mode;
	}
	
	public static void scanForTarget()
	{
		// if we're not actively scanning, return
		if (!scanningMode)
			return;
		
		// if we are actively scanning but enough time has not passed, return
		currentTime = Utility.getFPGATime();
		if ((currentTime - initTime) < TARGET_SCAN_PERIOD_USEC)
			return;
		
		// grab an image from the camera
		//capturedImage = myCamera.getImage();
		
		// check for target, get position relative to center of image
			
		// if we are lined up with target, flag green
		if (targetCenterY < TARGET_CENTER_TOLERANCE_PIXELS)
		    RioDuinoAssembly.sendColor(RioDuinoAssembly.Color.Green);
		else if (targetSeen)
			RioDuinoAssembly.sendColor(RioDuinoAssembly.Color.Yellow);
		else
			RioDuinoAssembly.setTeamColor();
		
		// reset timer
		initTime = Utility.getFPGATime();
		
	}
	
}

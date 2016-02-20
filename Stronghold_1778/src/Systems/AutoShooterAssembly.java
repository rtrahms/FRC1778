package Systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

public class AutoShooterAssembly {
	
	private static boolean initialized = false;
		
	private static final int LEFT_JOYSTICK_ID = 0;
	private static final int RIGHT_JOYSTICK_ID = 1;
	private static final int TARGETING_BUTTON_ID = 9;
	
	private static Joystick leftJoy, rightJoy;
	
	private static final double TARGET_X_POS_PIXELS = 200;
	private static final double TARGET_Y_POS_PIXELS = 50;
	private static final double TARGET_MARGIN_PIXELS = 50;
	private static double actualPosX = -1;
	private static double actualPosY = -1;
	private static boolean calibratedX, calibratedY;
	
	private static boolean targeting = false;
		
	public static void initialize()
	{
		if (!initialized)
		{
			leftJoy = new Joystick(LEFT_JOYSTICK_ID);
			rightJoy = new Joystick(RIGHT_JOYSTICK_ID);
			
			targeting = false;
			actualPosX = -1;
			actualPosY = -1;
			
			calibratedX = false;
			calibratedY = false;
			
			initialized = true;
		}
	}
	
	public static void teleopInit()
	{
		targeting = false;
		actualPosX = -1;
		actualPosY = -1;
		calibratedX = false;
		calibratedY = false;
	}
	
	public static void teleopPeriodic()
	{
		// one button to start auto trigger mode
		if (leftJoy.getRawButton(TARGETING_BUTTON_ID) && !targeting)
		{
			System.out.println("AutoShooterAssembly:  autotargeting mode ON");
			targeting = true;
			actualPosX = -1;
			actualPosY = -1;
			calibratedX = false;
			calibratedY = false;
		}
		
		// either joystick trigger pressed to stop auto targeting mode
		if ((leftJoy.getTrigger() || rightJoy.getTrigger()) && targeting)
		{
			System.out.println("AutoShooterAssembly:  autotargeting mode OFF");
			targeting = false;
			actualPosX = -1;
			actualPosY = -1;
			calibratedX = false;
			calibratedY = false;
		}
		
		if (targeting)
		{
			// if both x and y position are set...
			if (!calibratedX)
				// first calibrate X position
				calibratedX = calibratePosX();
			else if (!calibratedY)
				// second calibrate Y position
				calibratedY = calibratePosY();
			else
			{
				// both are calibrated - shoot the ball!
				System.out.println("AutoTarget: SHOOTING!");
				//CatapultAssembly.shoot();
			}
		}
	}
	
	public static void disabledInit()
	{
		if (!initialized)
			initialize();
	}
	
	private static boolean calibratePosX()
	{
		//actualPosX = NetworkCommAssembly.getTargetCenterX();
		
		if (actualPosX < 0)
			return false;
		
		double deltaX = TARGET_X_POS_PIXELS - actualPosX;
				
		// if in the margin is small enough
		if (deltaX < TARGET_MARGIN_PIXELS)
		{
			// no further x movement necessary - STOP
			CANDriveAssembly.driveDirection(0, 0);
			
			System.out.println("LATERAL X CALIBRATED!");
			return true;
		}
		else
		{
			System.out.println("actualPosX = " + actualPosX + " deltaX = " + deltaX);
			
			// rotate robot left or right to get better actual X position
			if (deltaX < 0)
				// actual to right of target
				CANDriveAssembly.rotateRight(0.1);
			else
				// actual to left of target
				CANDriveAssembly.rotateLeft(0.1);
		}
		
		return false;
	}
	
	private static boolean calibratePosY()
	{
		//actualPosY = NetworkCommAssembly.getTargetCenterY();
		
		if (actualPosY < 0)
			return false;
		
		double deltaY = TARGET_Y_POS_PIXELS - actualPosY;
				
		// if in the margin is small enough
		if (deltaY < TARGET_MARGIN_PIXELS)
		{
			// no further y movement necessary - STOP
			CANDriveAssembly.driveDirection(0, 0);
			
			System.out.println("AXIAL Y CALIBRATED!");
			return true;
		}
		else
		{
			System.out.println("actualPosY = " + actualPosY + " deltaY = " + deltaY);
			
			// move forward or backward to get better actual y position
			
			if (deltaY < 0)
				// actual below target, go forward
				CANDriveAssembly.driveDirection(0, 0.1);
			else
				// actual above target, go backward
				CANDriveAssembly.driveDirection(0,-0.1);
		}
		
		return false;
	}
		
}

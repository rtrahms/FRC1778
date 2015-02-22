package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Utility;

public class RobotAuto {
	public static int currentState;
	public static final int INIT_STATE = 1;
	public static final int STATE_GETBIN = 2;
	public static final int STATE_STOP = 0;
	
	// autonomous constants
	// drive time 3 seconds
	private static final long GETBIN_DRIVETIME = 4;
	private static final double AUTO_DRIVE_SPEED = -0.5;
	
	private static long startTime;
	private static long lastTime;
	
	public static void autoInit(Robot robot) {
		currentState = INIT_STATE;
		resetTime();
		startTime = Utility.getFPGATime();
	}
	
	public static void autoPeriodic(Robot robot) {
		switch(currentState) {
		case INIT_STATE:
			switchState(STATE_GETBIN);
		break;
		case STATE_GETBIN:
			if(getTime() < GETBIN_DRIVETIME) {
				robot.drivetrain.driveDirection(0,AUTO_DRIVE_SPEED);
			} else {
				switchState(STATE_STOP);
			}
		break;
		case STATE_STOP:
			//Do Nothing
		break;
		}
	}
	
	public static double getTime() {
		return (Utility.getFPGATime()-lastTime)/1000000.0;
	}
	
	public static double getTotalTime() {
		return (Utility.getFPGATime()-startTime)/1000000.0;
	}
	
	public static void resetTime() {
		lastTime = Utility.getFPGATime();
	}
	
	public static void switchState(int newState) {
		currentState = newState;
		resetTime();
	}
}

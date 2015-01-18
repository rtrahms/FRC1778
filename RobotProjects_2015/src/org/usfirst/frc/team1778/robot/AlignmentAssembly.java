package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

// Chill Out 1778 class for controlling the alignment mechanism

public class AlignmentAssembly {
	
    // left roller throttle (how fast the rollers move, and direction)
    private final double LEFT_STEP_MAGNITUDE_DEFAULT = 1.0;
    private final double LEFT_STEP_POLARITY_DEFAULT = 1.0;
    
    // right roller throttle (how fast the rollers move, and direction)
    private final double RIGHT_STEP_MAGNITUDE_DEFAULT = 1.0;
    private final double RIGHT_STEP_POLARITY_DEFAULT = 1.0;

    // Speed Controller IDs
	private final int LEFT_WHEEL_TALON_ID = 8;
	private final int RIGHT_WHEEL_TALON_ID = 9;
	
	// elevator controller gampad ID
	private final int GAMEPAD_ID = 2;
	
    // minimum motor increment (for joystick dead zone)
    private final double MIN_INCREMENT = 0.1;

	private TalonSRX mLeftWheel, mRightWheel;
	
	private Joystick gamepad;
	
	private double leftStep, rightStep;

	// constructor
	public AlignmentAssembly()
	{
		mLeftWheel = new TalonSRX(LEFT_WHEEL_TALON_ID);
		mRightWheel = new TalonSRX(RIGHT_WHEEL_TALON_ID);
		
        // wheel control
        gamepad = new Joystick(GAMEPAD_ID);
        
        // step amounts
        leftStep = LEFT_STEP_POLARITY_DEFAULT * LEFT_STEP_MAGNITUDE_DEFAULT;
        rightStep = RIGHT_STEP_POLARITY_DEFAULT * RIGHT_STEP_MAGNITUDE_DEFAULT;

	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopPeriodic()
	{
        // right and left wheel operation via right joystick
        double leftIncrement = gamepad.getRawAxis(2)*leftStep;   
        double rightIncrement = gamepad.getRawAxis(5)*rightStep;   
        
        if (Math.abs(leftIncrement) < MIN_INCREMENT)
        {
            leftIncrement = 0.0;
        }
        
        if (Math.abs(rightIncrement) < MIN_INCREMENT)
        {
            rightIncrement = 0.0;
        }
        
        // set wheel speed for left and right
        mLeftWheel.set(leftIncrement);
        mRightWheel.set(rightIncrement);
            
	}
}

package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

// Chill Out 1778 class for controlling the alignment mechanism

public class AlignmentAssembly {
	
    // left & right roller polarities (remember, rollers should operate opposite each other)
    private final double LEFT_POLARITY = -1.0;
    private final double RIGHT_POLARITY = 1.0;
    
    // Speed Controller IDs
	private final int LEFT_WHEEL_TALON_ID = 9;
	private final int RIGHT_WHEEL_TALON_ID = 10;
	
	// elevator controller gampad ID
	private final int GAMEPAD_ID = 2;
	
	private CANTalon mLeftWheel, mRightWheel;
	
	private Joystick gamepad;
	
	// constructor
	public AlignmentAssembly()
	{
		mLeftWheel = new CANTalon(LEFT_WHEEL_TALON_ID);
		mRightWheel = new CANTalon(RIGHT_WHEEL_TALON_ID);
		
        // wheel control
        gamepad = new Joystick(GAMEPAD_ID);
	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopPeriodic()
	{
        // left and right wheel operation via left and right gamepad joysticks
        double leftMove = LEFT_POLARITY * gamepad.getRawAxis(3);   
        double rightMove = RIGHT_POLARITY * gamepad.getRawAxis(1);           
        
        // set wheel speed for left and right
        mLeftWheel.set(leftMove);
        mRightWheel.set(rightMove);
            
	}
}

package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;

//Chill Out 1778 class for controlling the elevator & pusher pneumatic mechanisms

public class ElevatorAssembly {
	    
    // minimum increment (for joystick dead zone)
    private final double MIN_INCREMENT = 0.1;
    
    // Pneumatics control module ID
    private final int PCM_NODE_ID = 0;
    
    // the two elevator pneumatics
	private final int LEFT_RISER_FORWARD_ID = 0;
	private final int LEFT_RISER_REVERSE_ID = 1;
	private final int RIGHT_RISER_FORWARD_ID = 2;
	private final int RIGHT_RISER_REVERSE_ID = 3;
	
	// the two pneumatics used for pushing stacks out
	//private final int LEFT_PUSHER_FORWARD_ID = 4;
	//private final int LEFT_PUSHER_REVERSE_ID = 5;
	//private final int RIGHT_PUSHER_FORWARD_ID = 6;
	//private final int RIGHT_PUSHER_REVERSE_ID = 7;
	
	// elevator controller gampad
	private final int GAMEPAD_ID = 3;
	
    // elevator control
    private Joystick gamepad;
    
    private Compressor compressor;
    private DoubleSolenoid leftRiser, rightRiser;
    //private DoubleSolenoid leftPusher, rightPusher;
    
    private boolean toggleElevator;
    //private boolean togglePusher;

	// constructor
	public ElevatorAssembly()
	{
        // elevator control
        gamepad = new Joystick(GAMEPAD_ID);
        
        compressor = new Compressor(PCM_NODE_ID);
        compressor.setClosedLoopControl(true);     // automatically turn on & off compressor based on pressure switch value
        
        leftRiser = new DoubleSolenoid(LEFT_RISER_FORWARD_ID, LEFT_RISER_REVERSE_ID);
        rightRiser = new DoubleSolenoid(RIGHT_RISER_FORWARD_ID, RIGHT_RISER_REVERSE_ID);
        toggleElevator = false;

        //leftPusher = new DoubleSolenoid(LEFT_PUSHER_FORWARD_ID, LEFT_PUSHER_REVERSE_ID);
        //rightPusher = new DoubleSolenoid(RIGHT_PUSHER_FORWARD_ID, RIGHT_PUSHER_REVERSE_ID);   
        //togglePusher = false;
	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopPeriodic()
	{
		
        // elevator operation via gamepad triggers
		DoubleSolenoid.Value elevatorValue;	
        double elevatorIncrement = gamepad.getRawAxis(3);
        
        if (Math.abs(elevatorIncrement) < MIN_INCREMENT)
        {
            elevatorValue = DoubleSolenoid.Value.kOff;
        }
        else if (!toggleElevator)
        {
        	elevatorValue = DoubleSolenoid.Value.kForward;
        	toggleElevator = !toggleElevator;
        }
        else
        {
        	elevatorValue = DoubleSolenoid.Value.kReverse;
        	toggleElevator = !toggleElevator;
        }
            
		leftRiser.set(elevatorValue);
		rightRiser.set(elevatorValue);

        // pusher operation via RIGHT joystick
		/*
		DoubleSolenoid.Value pusherValue;
        double pusherIncrement = gamepad.getRawAxis(3);
        
        if (Math.abs(pusherIncrement) < MIN_INCREMENT)
        {
            pusherValue = DoubleSolenoid.Value.kOff;
        }
        else if (!togglePusher)
        {
        	pusherValue = DoubleSolenoid.Value.kForward;
        	togglePusher = !togglePusher;
        }
        else
        {
        	pusherValue = DoubleSolenoid.Value.kReverse;
        	togglePusher = !togglePusher;
        }

		leftPusher.set(pusherValue);
		rightPusher.set(pusherValue);
		*/
	}

}

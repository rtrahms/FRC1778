package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

//Chill Out 1778 class for controlling the elevator & pusher pneumatic mechanisms

public class ElevatorAssembly {
	    
    // minimum increment (for joystick dead zone)
    private final long CYCLE_USEC = 250000;
    
    // Pneumatics control module ID - must match CANbus ID of PCM!!
    private final int PCM_NODE_ID = 2;
    
    // elevator and pusher pneumatic solenoid IDs
	private final int ELEVATOR_FORWARD_ID = 0;
	private final int ELEVATOR_REVERSE_ID = 1;
	private final int PUSHER_FORWARD_ID = 2;
	private final int PUSHER_REVERSE_ID = 3;
		
	// elevator controller gampad - assumes drive joysticks are 0 and 1
	private final int GAMEPAD_ID = 2;
	
    // elevator control
    private Joystick gamepad;
    
    private DoubleSolenoid elevatorValve;
    private DoubleSolenoid pusherValve;
    
    // toggle (state) variables
    private boolean toggleElevator;
    private boolean togglePusher;
    
    private long initTime;

	// constructor
	public ElevatorAssembly()
	{
        // elevator & pusher control
        gamepad = new Joystick(GAMEPAD_ID);
                
        elevatorValve = new DoubleSolenoid(PCM_NODE_ID, ELEVATOR_FORWARD_ID, ELEVATOR_REVERSE_ID);
        elevatorValve.set(DoubleSolenoid.Value.kOff);
        toggleElevator = false;

        pusherValve = new DoubleSolenoid(PCM_NODE_ID, PUSHER_FORWARD_ID, PUSHER_REVERSE_ID);
        pusherValve.set(DoubleSolenoid.Value.kOff);
        togglePusher = false;
        
        initTime = Utility.getFPGATime();
	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopPeriodic()
	{
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initTime) < CYCLE_USEC)
			return;
		
        // elevator valve operation via gamepad button X
        if (gamepad.getRawButton(1))
        {
        	DoubleSolenoid.Value elevatorValue;
        	
	        if (!toggleElevator)
	        	elevatorValue = DoubleSolenoid.Value.kForward;
	        else
	        	elevatorValue = DoubleSolenoid.Value.kReverse;
	        
        	toggleElevator = !toggleElevator;
    		elevatorValve.set(elevatorValue);
       }

        // pusher valve operation via gamepad button Y
        if (gamepad.getRawButton(2))
        {
        	DoubleSolenoid.Value pusherValue;
        	
	        if (!toggleElevator)
	        	pusherValue = DoubleSolenoid.Value.kForward;
	        else
	        	pusherValue = DoubleSolenoid.Value.kReverse;
	        
	        togglePusher = !togglePusher;
    		pusherValve.set(pusherValue);
       }

		// set up for next cycle
		initTime = Utility.getFPGATime();

	}

}

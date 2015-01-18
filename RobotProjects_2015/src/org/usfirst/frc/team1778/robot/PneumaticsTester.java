package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

//Chill Out 1778 class for testing pneumatic mechanisms

public class PneumaticsTester {
	    
    // minimum increment (for joystick dead zone)
    private final long CYCLE_USEC = 250000;
    
    // Pneumatics control module ID - must match CANbus ID of PCM!!
    private final int PCM_NODE_ID = 2;
    	
	// pneumatics controller gampad ID - assumes no other controllers connected
	private final int GAMEPAD_ID = 0;
	
    // pneumatics control
    private Joystick gamepad;
    
    private Compressor compressor;
    private DoubleSolenoid doubleSol;
    
    private boolean toggleValve;
    
    private long initTime;
    
	// constructor
	public PneumaticsTester()
	{
        // pneumatics control
        gamepad = new Joystick(GAMEPAD_ID);
        
        // not needed for normal solenoid operation
        //compressor = new Compressor(PCM_NODE_ID);
        //compressor.setClosedLoopControl(true);     // automatically turn on & off compressor based on pressure switch value
        
        doubleSol = new DoubleSolenoid(PCM_NODE_ID, 0, 1);
        toggleValve = true;
        
        initTime = Utility.getFPGATime();
	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopPeriodic()
	{
						
		// if no button push, return
		if (!gamepad.getRawButton(1))
			return;
		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initTime) < CYCLE_USEC)
			return;
		
		//System.out.println("game pad X button pressed!");
		
		// otherwise, toggle the valve
		if (toggleValve)
		{
			System.out.println("enabling double solenoid!");
			doubleSol.set(DoubleSolenoid.Value.kForward);
		}
		else
		{
			System.out.println("reversing double solenoid!");
			doubleSol.set(DoubleSolenoid.Value.kReverse);
		}
		
		// set up for next cycle
		toggleValve = !toggleValve;
		initTime = Utility.getFPGATime();
				
	}

}

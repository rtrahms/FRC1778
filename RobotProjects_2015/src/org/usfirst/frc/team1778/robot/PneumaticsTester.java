package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Utility;

//Chill Out 1778 class for testing pneumatic mechanisms

public class PneumaticsTester {
	    
    // minimum increment (for joystick dead zone)
    private final long CYCLE_MILLISEC = 2000;
    
    // Pneumatics control module ID
    private final int PCM_NODE_ID = 0;
    	
	// pneumatics controller gampad ID - assumes no other controllers connected
	private final int GAMEPAD_ID = 1;
	
    // pneumatics control
    private Joystick gamepad;
    
    private Compressor compressor;
    //private DoubleSolenoid doubleSol;
    private Solenoid singleSol;
    
    private boolean toggleValve;
    
    private long initTime;
    
	// constructor
	public PneumaticsTester()
	{
        // pneumatics control
        gamepad = new Joystick(GAMEPAD_ID);
        
        compressor = new Compressor(PCM_NODE_ID);
        compressor.setClosedLoopControl(true);     // automatically turn on & off compressor based on pressure switch value
        
        //doubleSol = new DoubleSolenoid(0, 1);
        singleSol = new Solenoid(0);
        toggleValve = true;
        
        initTime = Utility.getFPGATime();
	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopPeriodic()
	{
		
		// currently just cycles a valve on and off at a periodic interval
		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		//if ((currentTime - initTime) < CYCLE_MILLISEC)
		//	return;
		
		// if no button push, return
		if (!gamepad.getRawButton(1))
			return;
		
		// otherwise, toggle the valve
		if (toggleValve)
		{
			singleSol.set(true);
		}
		else
		{
			singleSol.set(false);
		}
		
		// set up for next cycle
		toggleValve = !toggleValve;
		initTime = Utility.getFPGATime();
				
	}

}

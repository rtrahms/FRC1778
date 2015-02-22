package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Utility;

//Chill Out 1778 class for testing pneumatic mechanisms

public class PneumaticsTester {
	    
    // minimum increment (for joystick dead zone)
    private final long CYCLE_USEC = 250000;
    
    // Pneumatics control module ID
    private final int PCM_NODE_ID = 2;
    	
	// pneumatics controller gampad ID - assumes no other controllers connected
	private final int GAMEPAD_ID = 0;
	
    // pneumatics control
    private Joystick gamepad;
    
    private Compressor compressor;
    private DoubleSolenoid doubleSol_1;
    private DoubleSolenoid doubleSol_2;
    //private Solenoid singleSol;
    
    private boolean toggleValve_1, toggleValve_2;
    
    private long initTime;
    
	// constructor
	public PneumaticsTester()
	{
        // pneumatics control
        gamepad = new Joystick(GAMEPAD_ID);
        
        //compressor = new Compressor(PCM_NODE_ID);
        //compressor.setClosedLoopControl(true);     // automatically turn on & off compressor based on pressure switch value
        
        doubleSol_1 = new DoubleSolenoid(PCM_NODE_ID, 0, 1);
        doubleSol_2 = new DoubleSolenoid(PCM_NODE_ID, 2, 3);
        //singleSol = new Solenoid(PCM_NODE_ID,0);
        toggleValve_1 = true;
        toggleValve_2 = true;
        
        initTime = Utility.getFPGATime();
	}
	
	public void autoInit() {
		
	}
	
	public void autoPeriodic()
	{
	}
		
	public void teleopInit()
	{
		
	}
	
	public void teleopPeriodic()
	{
		
		// currently just cycles a valve on and off at a periodic interval
		
		long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		if ((currentTime - initTime) < CYCLE_USEC)
			return;
		
		// if x button push, toggle valve 1
		if (gamepad.getRawButton(1))
		{
			// otherwise, toggle the valve
			setLift(toggleValve_1);
			
			// set up for next cycle
			initTime = Utility.getFPGATime();
			toggleValve_1 = !toggleValve_1;
		}
		
		// if y button push, toggle valve 2
		if (gamepad.getRawButton(2))
		{
			// otherwise, toggle the valve
			if (toggleValve_2)
			{
				System.out.println("enabling double solenoid!");
				//singleSol.set(true);
				doubleSol_2.set(DoubleSolenoid.Value.kForward);
			}
			else
			{
				System.out.println("reversing double solenoid!");
				//singleSol.set(false);
				doubleSol_2.set(DoubleSolenoid.Value.kReverse);
			}
			
			// set up for next cycle
			initTime = Utility.getFPGATime();	
			toggleValve_2 = !toggleValve_2;
		}
		
		//long currentTime = Utility.getFPGATime();
		
		// if not long enough, just return
		//if ((currentTime - initTime) < CYCLE_USEC)
		//	return;
		
		//System.out.println("game pad button pressed!");
				
				
	}
	
	public void setLift(boolean lift) {
		if (lift)
		{
			System.out.println("enabling double solenoid!");
			//singleSol.set(true);
			doubleSol_1.set(DoubleSolenoid.Value.kForward);
		}
		else
		{
			System.out.println("reversing double solenoid!");
			//singleSol.set(false);
			doubleSol_1.set(DoubleSolenoid.Value.kReverse);
		}
	}

}

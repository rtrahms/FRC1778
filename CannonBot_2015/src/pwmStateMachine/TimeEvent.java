package pwmStateMachine;

import edu.wpi.first.wpilibj.Utility;

// event triggered when timer gets to a certain predetermined angle
public class TimeEvent extends Event {
	
	private double durationSec;
	private long startTimeUs;
	
	public TimeEvent()
	{	
		this.durationSec = 0.0;
	}
	
	public TimeEvent(double durationSec)
	{
		this.durationSec = durationSec;
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("TimeEvent initialized!");
		startTimeUs = Utility.getFPGATime();
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		long currentTimeUs = Utility.getFPGATime();
		double delta = (currentTimeUs - startTimeUs)/1e6;
		//System.out.println("delta = " + delta + " duration = " + durationSec);
		
		if (delta > durationSec)
		{
			System.out.println("TimeEvent triggered!");
			return true;
		}
		
		return false;
	}
}

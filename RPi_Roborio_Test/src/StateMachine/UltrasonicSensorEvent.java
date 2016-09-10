package StateMachine;

import java.util.prefs.Preferences;

import Systems.UltrasonicSensor;

// event triggered when sensor gets within a predetermined distance
public class UltrasonicSensorEvent extends Event {
	
	//private UltrasonicSensor ultra1778;
	private double distanceMM;
	
	public UltrasonicSensorEvent()
	{
		UltrasonicSensor.initialize();
		this.distanceMM = 0.0;
	}
	
	public UltrasonicSensorEvent(double distanceMM)
	{
		UltrasonicSensor.initialize();
		this.distanceMM = distanceMM;		
	}
	
	// overloaded initialize method
	public void initialize()
	{
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		if (UltrasonicSensor.getRangeMM() <= distanceMM)
			return true;
		
		return false;
	}
	
	public void persistWrite(int counter, Preferences prefs) {

		// create node for event
		Preferences eventPrefs = prefs.node(counter + "_" + this.name);
	
		// store event details
		eventPrefs.put("class",this.getClass().toString());
		eventPrefs.putDouble("distanceM",this.distanceMM);		
	}

}

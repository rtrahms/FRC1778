package StateMachine;

import java.util.prefs.Preferences;


public class Event {

	public String name;
	protected boolean triggered = false;
	
	public Event()
	{
		name = "<Generic Event>";
	}

	public void initialize()
	{
		
	}
	
	public boolean isTriggered()
	{
		return triggered;
	}
	
	public void persistWrite(int counter, Preferences prefs) {

		// create node for event
		Preferences eventPrefs = prefs.node(counter + "_" + this.name);
	
		// store event class
		eventPrefs.put("class",this.getClass().toString());
	}
	
}

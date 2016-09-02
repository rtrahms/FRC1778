package StateMachine;

import java.util.prefs.Preferences;


public class Event {

	public String name;
	protected boolean triggered = false;
	
	public Event()
	{
	}

	public void initialize()
	{
		
	}
	
	public boolean isTriggered()
	{
		return triggered;
	}
	
	public void persistWrite(Preferences prefs, String eventKeyStr) {

		// create node for event
		Preferences eventPrefs = prefs.node(eventKeyStr);
	
		// store event name
		eventPrefs.put("name",eventKeyStr);
	}
	
}

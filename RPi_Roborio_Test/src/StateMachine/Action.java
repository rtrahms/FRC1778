package StateMachine;

import java.util.prefs.Preferences;

public class Action {

	public String name;
	
	public Action() {
		
	}
	
	public void initialize() {
		
	}
	
	public void process() {
		
	}
	
	public void cleanup() {
		
	}
	
	public void persistWrite(Preferences prefs, String actionKeyStr) {

		// create node for action
		Preferences actionPrefs = prefs.node(actionKeyStr);
	
		// store action name
		actionPrefs.put("name",actionKeyStr);
	}
}

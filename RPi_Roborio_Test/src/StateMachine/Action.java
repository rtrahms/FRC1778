package StateMachine;

import java.util.prefs.Preferences;

public class Action {

	public String name;
	
	public Action() {
		name = "<Generic Action>";
	}
	
	public void initialize() {
		
	}
	
	public void process() {
		
	}
	
	public void cleanup() {
		
	}
	
	public void persistWrite(int counter, Preferences prefs) {

		// create node for action
		Preferences actionPrefs = prefs.node(counter + "_" + this.name);
	
		// store action class
		actionPrefs.put("class",this.getClass().toString());
	}
}

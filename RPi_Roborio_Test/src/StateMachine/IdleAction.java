package StateMachine;

import java.util.prefs.Preferences;


public class IdleAction extends Action {
	
	public IdleAction() {
		this.name = "<Idle Action>";		
	}
	
	public IdleAction(String name)
	{
		this.name = name;
	}
	
	// no need for enter, process, exit overloaded methods
	// WE DON'T DO ANYTHING IN IDLE!
	// used for persisting the network in a Java Preferences class object
	
	public void persistWrite(int counter, Preferences prefs) {

		// create node for action
		Preferences actionPrefs = prefs.node(counter + "_" + this.name);
	
		// store action class
		actionPrefs.put("class",this.getClass().toString());
	}
	
}

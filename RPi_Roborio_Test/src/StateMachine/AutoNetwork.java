package StateMachine;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import NetworkComm.InputOutputComm;

public class AutoNetwork {
	public String name;
	protected ArrayList<AutoState> states;
	
	private AutoState currentState;
	
	public AutoNetwork() {
		name = "<Generic Auto Network>";
		states = new ArrayList<AutoState>();
		currentState = null;
	}
	
	public AutoNetwork(String name) {
		this.name = name;
		states = new ArrayList<AutoState>();
		currentState = null;
	}

	
	public void addState(AutoState state) {
		states.add(state);
	}
	
	public AutoState getState(String name) {
		for (AutoState state : states) {
			if (state.name == name)
				return state;
		}
		
		return null;
	}
	
	public AutoState getCurrentState() {
		return currentState;
	}
	
	public void enter() {
		// grab the first state in the selected network and enter it!
		if (!states.isEmpty())
		{
			currentState = states.get(0);
			currentState.enter();
		}	

	}
	
	public void process() {
		if (currentState != null)
		{
			String myString = new String("State = " + currentState.name);
			//System.out.println(myString);
			InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"Auto/AutoSM_currentState", myString);

			AutoState nextState = currentState.process();

			// if the returned next state is non-null and not the current state, time to change state
			if ((nextState != null) && (nextState != currentState))
			{	
				// clean up current state
				currentState.exit();
				
				//System.out.println("State machine switching to " + nextState.name);		
				
				// switch to next state
				currentState = nextState;
				currentState.enter();
			}

		}
	}
	
	public void exit() {
		if (currentState != null) 
		{			
			currentState.exit();
		}
	}
	
	// used for persisting the network in a Java Preferences class object
	public void persistWrite(int counter, Preferences prefs) {

		// create node for autoNetwork
		Preferences networkPrefs = prefs.node(counter + "_" + this.name);
		
		// store network name
		networkPrefs.put("class",this.getClass().toString());
		
		// store all the states in the autoNetwork prefs
		int ctr = 0;
		for (AutoState a: states)
		{
			a.persistWrite(ctr++,networkPrefs);
		}
	}	
	
}

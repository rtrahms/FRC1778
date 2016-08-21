package StateMachine;

import java.util.ArrayList;

public class AutoState {
	
	protected ArrayList<Action> actionList;
	protected ArrayList<Event> eventList;
	public String name;
	protected AutoState nextState;
	
	public AutoState() {
		this.name = "AutoState";
		actionList = new ArrayList<Action>();
		eventList = new ArrayList<Event>();
		this.nextState = null;
	}
	
	public AutoState(String name) {
		this.name = name;
		actionList = new ArrayList<Action>();
		eventList = new ArrayList<Event>();
		this.nextState = null;
	}
	
	public void addAction(Action newAction) {
		actionList.add(newAction);
	}
	
	public void addEvent(Event newEvent) {
		eventList.add(newEvent);
	}
	
	public void associateNextState(AutoState nextState) {
		this.nextState = nextState;
	}
	
	// enter into state first time
	public void enter()
	{
		for (Action a: actionList) {
			// initialize each action
			a.initialize();
		}
		
		for (Event e: eventList) {
			// initialize each event for monitoring
			e.initialize();
		}
	}
	
	// called periodically
	public AutoState process()
	{
		// debug only - say who we are
		System.out.println("Current state is " + name);
		
		// for all the actions this state has, process each
		for (Action a: actionList) {
			a.process();
		}
		
		// for all the events this state has, check each
		for (Event e: eventList) {
			// if any event is triggered
			if (e.isTriggered()) {
				
				// query for its next state, and go there
				return this.nextState;
			}
		}
		
		// if we reach the end and nothing has triggered, return this state
		return this;
	}
	
	// exit state 
	public void exit()
	{
		// for all the actions this state has, cleanup each
		for (Action a: actionList) {
			a.cleanup();
		}		
	}
	
}

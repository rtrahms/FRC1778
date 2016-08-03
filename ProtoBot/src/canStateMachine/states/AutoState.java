package canStateMachine.states;

import java.util.ArrayList;

import canStateMachine.events.Event;

public abstract class AutoState {
	
	protected ArrayList<Event> eventList;
	public String name;
	
	public abstract void initialize (String args);

	
	public AutoState() {
		eventList = new ArrayList<Event>();
	}
	
	public void addEvent(Event newEvent) {
		eventList.add(newEvent);
	}
	
	// enter into state first time
	public void enter()
	{
		for (Event e: eventList) {
			// initialize each event for monitoring
			e.initialize("");
		}
	}
	
	// called periodically
	public AutoState process()
	{
		// debug only - say who we are
		System.out.println("Current state is " + name);
		
		// for all the events this state has, check each
		for (Event e: eventList) {
			// if an event is triggered
			if (e.isTriggered()) {
				
				// query for its next state, and go there
				return e.getNextState();
			}
		}
		
		return null;
	}
	
	// exit state 
	public void exit()
	{
		
	}
	
}

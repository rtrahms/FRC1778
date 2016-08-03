package canStateMachine.events;

import canStateMachine.states.AutoState;

public abstract class Event {

	protected boolean triggered = false;
	protected AutoState nextState;
	
	public Event()
	{
		nextState = null;
	}

	public abstract void initialize(String args);
	
	public abstract boolean isTriggered();
	
	public void associateNextState(AutoState nextState)
	{
		this.nextState = nextState;
	}
	
	public AutoState getNextState()
	{
		return nextState;
	}
}

package pwmStateMachine;


public class Event {

	protected boolean triggered = false;
	protected AutoState nextState;
	
	public Event()
	{
		nextState = null;
	}

	public void initialize()
	{
		
	}
	
	public boolean isTriggered()
	{
		return triggered;
	}
	
	public void associateNextState(AutoState nextState)
	{
		this.nextState = nextState;
	}
	
	public AutoState getNextState()
	{
		return nextState;
	}
}

package canStateMachine.states;


public class IdleState extends AutoState {
	
	public IdleState() {
		this.name = "<Idle State>";		
	}
	
	public IdleState(String name)
	{
		this.name = name;
	}

	@Override
	public void initialize(String args) {
		// TODO Auto-generated method stub
		
	}
	
	// no need for enter, process, exit overloaded methods
	// WE DON'T DO ANYTHING IN IDLE!
}

package StateMachine;


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
}

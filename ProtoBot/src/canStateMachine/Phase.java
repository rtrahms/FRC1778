package canStateMachine;

import java.util.ArrayList;

public class Phase {
	public ArrayList <Action> Actions = new ArrayList<Action>();
	private boolean _isFinished = false;
	

	public boolean IsFinished()
	{
		_isFinished = true;
		
		// need a better way to exit quickly
		Actions.forEach((action) ->{
		if (!action.IsFinished())
				_isFinished = false;
			});
		
		return _isFinished;			
	}
}

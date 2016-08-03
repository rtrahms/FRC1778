package canStateMachine;

import canStateMachine.events.Event;
import canStateMachine.states.AutoState;

public class Action {
	private boolean _isFinished = false;
	private Event _event;
	private AutoState _state;

	public Action (String line) throws ClassNotFoundException, InstantiationException, IllegalAccessException {		
		String eventClassName = "";
		String stateClassName = "";
		String args = "";
		String section = "";
		
		int stateEndPos = line.indexOf("|")+1;
		
		// Create the State information
		section = line.substring(0, stateEndPos);
		
		int argsStartPos = section.indexOf("(");
		int argsEndPos = section.indexOf(")");
		stateClassName = section.substring(0, argsStartPos).trim();
		
		// strip off the ()
		args = section.substring(argsStartPos+1, argsEndPos);
		
		// may need to prepend the package name to the class name
		// canStateMachine.states
		Class theClass = Class.forName(stateClassName);
		AutoState state = (AutoState)theClass.newInstance();
		state.initialize(args.trim());
		
		this.set_state(state);
		
		
		if (line.substring = section.contains("|")) {
			// Create the Event information	
			section = line.substring(stateEndPos+1, line.length());
			
			argsStartPos = section.indexOf("(");
			argsEndPos = section.indexOf(")");
			eventClassName = section.substring(0, argsStartPos).trim();
			
			// strip off the ()
			args = section.substring(argsStartPos+1, argsEndPos);
			
			// may need to prepend the package name to the class name
			// canStateMachine.events
			theClass = Class.forName(eventClassName);
			Event event = (Event)theClass.newInstance();
			event.initialize(args.trim());
			
			
			this.set_event(event);
		}
	}

	public boolean IsFinished(){
		// TODO write the guts
		return _isFinished;
	}

	public Event get_event() {
		return _event;
	}

	public void set_event(Event _event) {
		this._event = _event;
	}

	public AutoState get_state() {
		return _state;
	}

	public void set_state(AutoState _state) {
		this._state = _state;
	}
	

}


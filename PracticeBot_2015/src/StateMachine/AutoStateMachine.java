package StateMachine;

import java.util.ArrayList;

public class AutoStateMachine {
		
	private ArrayList<AutoState> autoStates0;
	private ArrayList<Event> autoEvents0;
	
	private AutoState currentState;
	
	public AutoStateMachine()
	{
		autoStates0 = new ArrayList<AutoState>();
		autoEvents0 = new ArrayList<Event>();
		
		createStateNetworks();
	}
	
	private void createStateNetworks()
	{	
		// drive straight network
		createStateNet0();
	}
	
	public AutoState getState() {
		return currentState;
	}
		
	public void start()
	{
		// if we have a state network
		if ((autoStates0 != null) && (autoStates0.size() > 0))
		{
			// grab the first state and enter it!
			currentState = autoStates0.get(0);			

			System.out.println("State machine starting with " + currentState.name);						
			currentState.enter();
		}
		
	}
	
	public void process()  {
		
		AutoState nextState = null;
		
		// process the current state
		if (currentState != null)
			nextState = currentState.process();
		
		// if the returned next state is non-null, event has triggered a new state!
		if (nextState != null)
		{	
			// clean up current state
			currentState.exit();
			
			System.out.println("State machine switching to " + nextState.name);		
			// switch to next state
			currentState = nextState;
			currentState.enter();
		}
	}
	
	private void createStateNet0() {
		// create states
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State>");
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events
		TimeEvent timer1 = new TimeEvent(1.0);  // 1s timer event
		TimeEvent timer2 = new TimeEvent(5.0);  // 5s timer event
		UltrasonicSensorEvent ultra1 = new UltrasonicSensorEvent(500);  // 0.5 M detection event
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward);
		timer2.associateNextState(deadEnd);
		ultra1.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward.addEvent(timer2);
		driveForward.addEvent(ultra1);
	
		// store everything
		autoStates0.add(startIdle);
		autoStates0.add(driveForward);
		autoStates0.add(deadEnd);
		
		autoEvents0.add(timer1);
		autoEvents0.add(timer2);
		autoEvents0.add(ultra1);

	}
}

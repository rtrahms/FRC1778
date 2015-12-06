package pwmStateMachine;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.DigitalInput;

public class AutoStateMachine {
		
	private DigitalInput autoNetworkSwitch1;
	private DigitalInput autoNetworkSwitch2;
	private DigitalInput autoNetworkSwitch3;

	private boolean autoNetworkEnable = false;
	
	private ArrayList<ArrayList<AutoState>> autoStates;
	private ArrayList<ArrayList<Event>> autoEvents;

	private AutoState currentState;
	
	public AutoStateMachine()
	{
		autoNetworkSwitch1 = new DigitalInput(0);
		autoNetworkSwitch2 = new DigitalInput(1);
		autoNetworkSwitch3 = new DigitalInput(2);

		// create list of lists for states and events
		autoStates = new ArrayList<ArrayList<AutoState>>();
		autoEvents = new ArrayList<ArrayList<Event>>();
			
		createStateNetworks();
	}
	
	private void createStateNetworks()
	{	
		
		// STATE MACHINE 0: add a do nothing state machine (auto disabled anyway at 0 index)
		createDoNothingSM(0);
		
		// STATE MACHINE 1: add a drive straight state machine (state & event list)
		createDriveForwardSM(1);
		
		// STATE MACHINE 2: add a simple turnaround state machine (state & event list)
		createTurnaroundSM(2);
				
		// STATE MACHINE 3: add a transport state machine (state & event list)
		createSquareTravelSM(3);
				
		// STATE MACHINE 4: add a stop-start-repeat state machine (state & event list)
		createStopStartRepeatSM(4);
		
		System.out.println("autoStates list size = " + autoStates.size() + ", autoEvents list size = " + autoEvents.size());
	}
	
	public AutoState getState() {
		return currentState;
	}
		
	public void start()
	{
		
		// determine if we are running auto or not
		int networkIndex = getNetworkIndex();
		System.out.println("autoNetworkEnable = " + autoNetworkEnable + ", networkIndex = " + networkIndex);
		
		if (autoNetworkEnable)
		{
			// if we have a state network
			ArrayList<AutoState> myNetwork = autoStates.get(networkIndex);
			
			if ((myNetwork != null) && (myNetwork.size() > 0))
			{
				// grab the first state in the selected network and enter it!
				currentState = myNetwork.get(0);
	
				System.out.println("State machine starting with " + currentState.name);						
				currentState.enter();
			}
		}
		
	}
	
	public void process()  {
		
		if (autoNetworkEnable)
		{
			AutoState nextState = null;
			
			// process the current state
			if (currentState != null)
			{
				System.out.println("State = " + currentState.name);
				nextState = currentState.process();
			}
			
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
	}
	
	// computes binary value from digital inputs.  
	// If all switches are false (zero), auto is disabled
	private int getNetworkIndex()
	{
		int value = 0;
		
		// first switch is binary 1
		if (autoNetworkSwitch1.get())
			value += 1;
		
		// second switch is binary 2
		if (autoNetworkSwitch2.get())
			value += 2;
		
		// third switch is binary 4
		if (autoNetworkSwitch3.get())
			value += 4;
		
		if (value == 0)
		{
			// all switches off means no auto modes selected - auto state machine operation disabled
			autoNetworkEnable = false;
		}
		else
		{
			// Non-zero network - auto mode selected!
			autoNetworkEnable = true;
		}
		
		// return index value for network
		return value;
		
	}
	
	// **** DO NOTHING STATE MACHINE ***** 
	// First (zero index) state machine - does nothing
	private void createDoNothingSM(int index) {
		
		IdleState deadEnd = new IdleState("<Dead End State>");
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event

		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();

		myStates.add(deadEnd);
		myEvents.add(timer1);
		
		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);
	}

	// **** MOVE FORWARD STATE MACHINE ***** 
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) go back to idle and stay there 
	private void createDriveForwardSM(int index) {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State>", isPwm, 0.5);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (betwen the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(8.0);  // 5s timer event
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward);
		timer2.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward.addEvent(timer2);
	
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();
				
		myStates.add(startIdle);
		myStates.add(driveForward);
		myStates.add(deadEnd);
		
		myEvents.add(timer1);
		myEvents.add(timer2);
		
		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);

	}
	
	// **** TURNAROUND STATE MACHINE ***** 
	// 1) be idle for a number of sec
	// 2) turn around (180 deg turn)
	// 3) go back to idle and stay there 
	private void createTurnaroundSM(int index) {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		TurnState turn180degState = new TurnState("<Turn Around State>",180.0, 0.5, isPwm);
		IdleState interimIdle = new IdleState("<Interim Idle State>");
		TurnState turnBackState = new TurnState("<Turn Back State>",-180.0, 0.5, isPwm);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (betwen the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // timer 1 event
		GyroAngleEvent gyro1 = new GyroAngleEvent(180.0);
		TimeEvent timer2 = new TimeEvent(0.5);  // timer 1 event
		GyroAngleEvent gyro2 = new GyroAngleEvent(-180.0);
		
		// connect each event with a state to move to
		timer1.associateNextState(turn180degState);
		gyro1.associateNextState(interimIdle);
		timer2.associateNextState(turnBackState);
		gyro2.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		turn180degState.addEvent(gyro1);
		interimIdle.addEvent(timer2);
		turnBackState.addEvent(gyro2);
	
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();

		myStates.add(startIdle);
		myStates.add(turn180degState);
		myStates.add(interimIdle);
		myStates.add(turnBackState);
		myStates.add(deadEnd);
		
		myEvents.add(timer1);
		myEvents.add(timer2);
		myEvents.add(gyro1);
		myEvents.add(gyro2);
		
		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);		
	}
	
	// **** SQUARE TRAVEL STATE MACHINE ***** 
	// 1) be idle for a number of sec
	// 2) turn in a square 
	// 3) go back to idle and stay there 
	private void createSquareTravelSM(int index) {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward1 = new DriveForwardState("<Drive Forward State 1>", isPwm, 0.5);
		TurnState turn1 = new TurnState("<Turn State 1>",90.0, 0.5, isPwm);
		DriveForwardState driveForward2 = new DriveForwardState("<Drive Forward State 2>", isPwm, 0.5);
		TurnState turn2 = new TurnState("<Turn State 2>",90.0, 0.5, isPwm);
		DriveForwardState driveForward3 = new DriveForwardState("<Drive Forward State 3>", isPwm, 0.5);
		TurnState turn3 = new TurnState("<Turn State 3>",90.0, 0.5, isPwm);
		DriveForwardState driveForward4 = new DriveForwardState("<Drive Forward State 4>", isPwm, 0.5);
		TurnState turn4 = new TurnState("<Turn State 4>",90.0, 0.5, isPwm);
		DriveForwardState driveForward5 = new DriveForwardState("<Drive Forward State 5>", isPwm, 0.5);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (betwen the states)
		TimeEvent timer_idle = new TimeEvent(0.5);
		TimeEvent timer_driveForward1 = new TimeEvent(1.0);
		GyroAngleEvent gyro_turn1 = new GyroAngleEvent(90.0);
		TimeEvent timer_driveForward2 = new TimeEvent(1.0);
		GyroAngleEvent gyro_turn2 = new GyroAngleEvent(90.0);
		TimeEvent timer_driveForward3 = new TimeEvent(1.0);
		GyroAngleEvent gyro_turn3 = new GyroAngleEvent(90.0);
		TimeEvent timer_driveForward4 = new TimeEvent(1.0);
		GyroAngleEvent gyro_turn4 = new GyroAngleEvent(90.0);
		TimeEvent timer_driveForward5 = new TimeEvent(1.0);
		
		// connect each event with a state to move to
		timer_idle.associateNextState(driveForward1);
		timer_driveForward1.associateNextState(turn1);
		gyro_turn1.associateNextState(driveForward2);
		timer_driveForward2.associateNextState(turn2);
		gyro_turn2.associateNextState(driveForward3);
		timer_driveForward3.associateNextState(turn3);
		gyro_turn3.associateNextState(driveForward4);
		timer_driveForward4.associateNextState(turn4);
		gyro_turn4.associateNextState(driveForward5);
		timer_driveForward5.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer_idle);
		driveForward1.addEvent(timer_driveForward1);
		turn1.addEvent(gyro_turn1);
		driveForward2.addEvent(timer_driveForward2);
		turn2.addEvent(gyro_turn2);
		driveForward3.addEvent(timer_driveForward3);
		turn3.addEvent(gyro_turn3);
		driveForward4.addEvent(timer_driveForward4);
		turn4.addEvent(gyro_turn4);
		driveForward5.addEvent(timer_driveForward5);
	
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();

		myStates.add(startIdle);
		myStates.add(driveForward1);
		myStates.add(driveForward2);
		myStates.add(driveForward3);
		myStates.add(driveForward4);
		myStates.add(driveForward5);
		myStates.add(turn1);
		myStates.add(turn2);
		myStates.add(turn3);
		myStates.add(turn4);
		myStates.add(deadEnd);
		
		myEvents.add(timer_driveForward1);
		myEvents.add(timer_driveForward2);
		myEvents.add(timer_driveForward3);
		myEvents.add(timer_driveForward4);
		myEvents.add(timer_driveForward5);
		myEvents.add(gyro_turn1);
		myEvents.add(gyro_turn2);
		myEvents.add(gyro_turn3);
		myEvents.add(gyro_turn4);
		
		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);		
	}

	
	// **** SIMPLE LIFT STATE MACHINE ***** 
	// 1) be idle for a number of sec
	// 2) lift down for a number of sec
	// 3) lift up for a number of sec
	// 4) lift down for a number of sec
	// 5) lift up for a number of sec
	// 6) lift down for a number of sec
	// 7) lift up for a number of sec
	// 8) go back to idle and stay there 
	private void createSimpleLiftSM(int index) {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		LiftState liftDown1 = new LiftState("<Lift Down 1>", false);
		LiftState liftUp1 = new LiftState("<Lift Up 1>", true);
		LiftState liftDown2 = new LiftState("<Lift Down 2>", false);
		LiftState liftUp2 = new LiftState("<Lift Up 2>", true);
		LiftState liftDown3 = new LiftState("<Lift Down 3>", false);
		LiftState liftUp3 = new LiftState("<Lift Up 3>", true);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (betwen the states)
		TimeEvent timer_idle = new TimeEvent(0.5);  // timer 1 event
		TimeEvent timer_liftDown1 = new TimeEvent(1.0);  // timer event for lift down 1
		TimeEvent timer_liftUp1 = new TimeEvent(1.0);  // timer event for lift up 1
		TimeEvent timer_liftDown2 = new TimeEvent(1.0);  // timer event for lift down 2
		TimeEvent timer_liftUp2 = new TimeEvent(1.0);  // timer event for lift up 2
		TimeEvent timer_liftDown3 = new TimeEvent(1.0);  // timer event for lift down 3
		TimeEvent timer_liftUp3 = new TimeEvent(1.0);  // timer event for lift up 3
		
		// connect each event with a state to move to
		timer_idle.associateNextState(liftDown1);
		timer_liftDown1.associateNextState(liftUp1);
		timer_liftUp1.associateNextState(liftDown2);
		timer_liftDown2.associateNextState(liftUp2);
		timer_liftUp2.associateNextState(liftDown3);
		timer_liftDown3.associateNextState(liftUp3);
		timer_liftUp3.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer_idle);
		liftDown1.addEvent(timer_liftDown1);
		liftUp1.addEvent(timer_liftUp1);
		liftDown2.addEvent(timer_liftDown2);
		liftUp2.addEvent(timer_liftUp2);
		liftDown3.addEvent(timer_liftDown3);
		liftUp3.addEvent(timer_liftUp3);
	
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();
		
		myStates.add(startIdle);
		myStates.add(liftDown1);
		myStates.add(liftDown2);
		myStates.add(liftDown3);
		myStates.add(liftUp1);
		myStates.add(liftUp2);
		myStates.add(liftUp3);
		myStates.add(deadEnd);
		
		myEvents.add(timer_idle);
		myEvents.add(timer_liftDown1);
		myEvents.add(timer_liftDown2);
		myEvents.add(timer_liftDown3);
		myEvents.add(timer_liftUp1);
		myEvents.add(timer_liftUp2);
		myEvents.add(timer_liftUp3);

		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);		
	}

	
	// ***** TRANSPORT STATE MACHINE *****
	// 1) be idle
	// 2) picks up recycle can
	// 2) picks up tote
	// 3) turns to left
	// 4) moves into auto zone
	// 5) lowers stack
	// 6) backs up a bit
	private void createTransportSM(int index) {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		LiftState liftDown1 = new LiftState("<Lift Down 1>", false);
		DriveForwardState driveForward1 = new DriveForwardState("<Drive Forward State 1>", isPwm, 0.5);
		LiftState liftUp1 = new LiftState("<Lift Up 1>", true);
		DriveForwardState driveForward2 = new DriveForwardState("<Drive Forward State 2>", isPwm, 0.5);
		LiftState liftDown2 = new LiftState("<Lift Down 2>", false);
		LiftState liftUp2 = new LiftState("<Lift Up 2>", true);
		TurnState turn1 = new TurnState("<Turn State 1>", -90.0, 0.5, isPwm);
		DriveForwardState driveForward3 = new DriveForwardState("<Drive Forward State 3>", isPwm, 0.5);
		LiftState liftDown3 = new LiftState("<Lift Down 3>", false);
		DriveForwardState driveBackward1 = new DriveForwardState("<Drive Backward State 1>", isPwm, -0.5);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer_idle = new TimeEvent(0.5);  // timer event for start
		TimeEvent timer_liftDown1 = new TimeEvent(2.0);  // timer event for lift down 1
		TimeEvent timer_driveForward1 = new TimeEvent(1.0);  // timer event for drive forward 1
		TimeEvent timer_liftUp1 = new TimeEvent(2.0);  // timer event for lift up 1
		TimeEvent timer_driveForward2 = new TimeEvent(1.0);  // timer event for drive forward 2
		TimeEvent timer_liftDown2 = new TimeEvent(1.0);  // timer event for lift down 2
		TimeEvent timer_liftUp2 = new TimeEvent(2.0);  // timer event for lift up 2
		GyroAngleEvent gyro_turn1 = new GyroAngleEvent(-90.0);  // gyro event -90 deg angle turn
		TimeEvent timer_driveForward3 = new TimeEvent(8.0);  // timer event for drive forward 3
		TimeEvent timer_liftDown3 = new TimeEvent(1.0);  // timer event for lift down 3
		TimeEvent timer_driveBackward1 = new TimeEvent(1.0);  // timer event for drive backward 1
		
		// connect each event with a state to move to
		timer_idle.associateNextState(liftDown1);
		timer_liftDown1.associateNextState(driveForward1);
		timer_driveForward1.associateNextState(liftUp1);
		timer_liftUp1.associateNextState(driveForward2);
		timer_driveForward2.associateNextState(liftDown2);
		timer_liftDown2.associateNextState(liftUp2);
		timer_liftUp2.associateNextState(turn1);
		gyro_turn1.associateNextState(driveForward3);
		timer_driveForward3.associateNextState(liftDown3);
		timer_liftDown3.associateNextState(driveBackward1);
		timer_driveBackward1.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer_idle);
		liftDown1.addEvent(timer_liftDown1);
		driveForward1.addEvent(timer_driveForward1);
		liftUp1.addEvent(timer_liftUp1);
		driveForward2.addEvent(timer_driveForward2);
		liftDown2.addEvent(timer_liftDown2);
		liftUp2.addEvent(timer_liftUp2);
		turn1.addEvent(gyro_turn1);
		driveForward3.addEvent(timer_driveForward3);
		liftDown3.addEvent(timer_liftDown3);
		driveBackward1.addEvent(timer_driveBackward1);

		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();
		
		myStates.add(startIdle);
		myStates.add(driveForward1);
		myStates.add(driveForward2);
		myStates.add(driveForward3);
		myStates.add(driveBackward1);
		myStates.add(liftUp1);
		myStates.add(liftUp2);
		myStates.add(liftDown1);
		myStates.add(liftDown2);
		myStates.add(liftDown3);
		myStates.add(turn1);
		myStates.add(deadEnd);
		
		myEvents.add(timer_idle);
		myEvents.add(timer_driveForward1);
		myEvents.add(timer_driveForward2);
		myEvents.add(timer_driveForward3);
		myEvents.add(timer_driveBackward1);
		myEvents.add(timer_liftUp1);
		myEvents.add(timer_liftUp2);
		myEvents.add(timer_liftDown1);
		myEvents.add(timer_liftDown2);
		myEvents.add(timer_liftDown3);
		myEvents.add(gyro_turn1);

		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);		

	}

	// **** STOP-START-REPEAT STATE MACHINE ****
	// Note: not currently used
	private void createStopStartRepeatSM(int index) {

		// create states
		boolean isPwm = true;
		
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward1 = new DriveForwardState("<Drive Forward State 1>", isPwm, 0.5);
		IdleState interimIdle1 = new IdleState("<Interim Idle State 1>");
		DriveForwardState driveForward2 = new DriveForwardState("<Drive Forward State 2>", isPwm, 0.5);
		IdleState interimIdle2 = new IdleState("<Interim Idle State 2>");
		DriveForwardState driveForward3 = new DriveForwardState("<Drive Forward State 3>", isPwm, 0.5);
		IdleState interimIdle3 = new IdleState("<Interim Idle State 3>");
		DriveForwardState driveForward4 = new DriveForwardState("<Drive Forward State 4>", isPwm, 0.5);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer1 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer3 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer4 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer5 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer6 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer7 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer8 = new TimeEvent(1.0);  // 0.5s timer event
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward1);
		timer2.associateNextState(interimIdle1);
		timer3.associateNextState(driveForward2);
		timer4.associateNextState(interimIdle2);
		timer5.associateNextState(driveForward3);
		timer6.associateNextState(interimIdle3);
		timer7.associateNextState(driveForward4);
		timer8.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward1.addEvent(timer2);
		interimIdle1.addEvent(timer3);
		driveForward2.addEvent(timer4);
		interimIdle2.addEvent(timer5);
		driveForward3.addEvent(timer6);
		interimIdle3.addEvent(timer7);
		driveForward4.addEvent(timer8);
	
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();
		
		myStates.add(startIdle);
		myStates.add(driveForward1);
		myStates.add(driveForward2);
		myStates.add(driveForward3);
		myStates.add(driveForward4);
		myStates.add(interimIdle1);
		myStates.add(interimIdle2);
		myStates.add(interimIdle3);
		myStates.add(deadEnd);
		
		myEvents.add(timer1);
		myEvents.add(timer2);
		myEvents.add(timer3);
		myEvents.add(timer4);
		myEvents.add(timer5);
		myEvents.add(timer6);
		myEvents.add(timer7);
		myEvents.add(timer8);

		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);		

	}


}

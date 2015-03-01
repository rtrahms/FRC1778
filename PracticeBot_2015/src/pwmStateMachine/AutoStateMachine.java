package pwmStateMachine;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DigitalInput;

public class AutoStateMachine {
		
	private DigitalInput autoNetworkSwitch;
	private boolean autoNetworkEnable = false;
	
	private ArrayList<AutoState> autoStates0;
	private ArrayList<Event> autoEvents0;

	private AutoState currentState;
	
	public AutoStateMachine()
	{
		autoNetworkSwitch = new DigitalInput(0);
		
		autoStates0 = new ArrayList<AutoState>();
		autoEvents0 = new ArrayList<Event>();
		
		createStateNetworks();
	}
	
	private void createStateNetworks()
	{	
		// drive straight network
		createStateNet0();
		
		// stop-start-repeat network
		//createStateNet1();
		
		// turning network
		//createStateNet2();
	}
	
	public AutoState getState() {
		return currentState;
	}
		
	public void start()
	{
		// determine if we are running auto or not
		autoNetworkEnable = autoNetworkSwitch.get();
		System.out.println("autoNetworkEnable = " + autoNetworkEnable);
		
		if (autoNetworkEnable)
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
		
	}
	
	public void process()  {
		
		if (autoNetworkEnable)
		{
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
	}
	
	// exmaple method to create a simple state network
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) go back to idle and stay there 
	private void createStateNet0() {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State>", isPwm);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(4.0);  // 5s timer event
		//UltrasonicSensorEvent ultra1 = new UltrasonicSensorEvent(500);  // 0.5 M detection event
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward);
		timer2.associateNextState(deadEnd);
		//ultra1.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward.addEvent(timer2);
		//driveForward.addEvent(ultra1);
	
		// store everything
		autoStates0.add(startIdle);
		autoStates0.add(driveForward);
		autoStates0.add(deadEnd);
		
		autoEvents0.add(timer1);
		autoEvents0.add(timer2);
		//autoEvents0.add(ultra1);

	}
	
	// another state network - doing more complex ops
	private void createStateNet1() {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward1 = new DriveForwardState("<Drive Forward State 1>", isPwm);
		IdleState interimIdle1 = new IdleState("<Interim Idle State 1>");
		DriveForwardState driveForward2 = new DriveForwardState("<Drive Forward State 2>", isPwm);
		IdleState interimIdle2 = new IdleState("<Interim Idle State 2>");
		DriveForwardState driveForward3 = new DriveForwardState("<Drive Forward State 3>", isPwm);
		IdleState interimIdle3 = new IdleState("<Interim Idle State 3>");
		DriveForwardState driveForward4 = new DriveForwardState("<Drive Forward State 4>", isPwm);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events
		TimeEvent timer1 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer3 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer4 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer5 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer6 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer7 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer8 = new TimeEvent(1.0);  // 0.5s timer event
		//UltrasonicSensorEvent ultra1 = new UltrasonicSensorEvent(500);  // 0.5 M detection event
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward1);
		timer2.associateNextState(interimIdle1);
		timer3.associateNextState(driveForward2);
		timer4.associateNextState(interimIdle2);
		timer5.associateNextState(driveForward3);
		timer6.associateNextState(interimIdle3);
		timer7.associateNextState(driveForward4);
		timer8.associateNextState(deadEnd);
		//ultra1.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward1.addEvent(timer2);
		interimIdle1.addEvent(timer3);
		driveForward2.addEvent(timer4);
		interimIdle2.addEvent(timer5);
		driveForward3.addEvent(timer6);
		interimIdle3.addEvent(timer7);
		driveForward4.addEvent(timer8);
		//driveForward.addEvent(ultra1);
	
		// store everything
		autoStates0.add(startIdle);
		autoStates0.add(driveForward1);
		autoStates0.add(driveForward2);
		autoStates0.add(driveForward3);
		autoStates0.add(driveForward4);
		autoStates0.add(interimIdle1);
		autoStates0.add(interimIdle2);
		autoStates0.add(interimIdle3);
		autoStates0.add(deadEnd);
		
		autoEvents0.add(timer1);
		autoEvents0.add(timer2);
		autoEvents0.add(timer3);
		autoEvents0.add(timer4);
		autoEvents0.add(timer5);
		autoEvents0.add(timer6);
		autoEvents0.add(timer7);
		autoEvents0.add(timer8);

		//autoEvents0.add(ultra1);

	}

	// another state network - doing some turning
	private void createStateNet2() {

		// create states
		boolean isPwm = true;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward1 = new DriveForwardState("<Drive Forward State 1>", isPwm);
		TurnState turn1 = new TurnState("<Turn State 1>", -90.0, 0.5);
		DriveForwardState driveForward2 = new DriveForwardState("<Drive Forward State 2>", isPwm);
		TurnState turn2 = new TurnState("<Turn State 2>", 90.0, 0.5);
		DriveForwardState driveForward3 = new DriveForwardState("<Drive Forward State 3>", isPwm);
		TurnState turn3 = new TurnState("<Turn State 3>", -180.0, 0.5);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events
		TimeEvent timer1 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer3 = new TimeEvent(1.0);  // 0.5s timer event
		TimeEvent timer4 = new TimeEvent(1.0);  // 0.5s timer event
		GyroAngleEvent gyroEvent1 = new GyroAngleEvent(-90.0);  // gyro event -90 deg angle turn
		GyroAngleEvent gyroEvent2 = new GyroAngleEvent(90.0);  // gyro event 90 deg angle turn
		GyroAngleEvent gyroEvent3 = new GyroAngleEvent(180.0);  // gyro event 180 deg angle turn
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward1);
		timer2.associateNextState(turn1);
		gyroEvent1.associateNextState(driveForward2);
		timer3.associateNextState(turn2);
		gyroEvent2.associateNextState(driveForward3);
		timer4.associateNextState(turn3);
		gyroEvent3.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward1.addEvent(timer2);
		turn1.addEvent(gyroEvent1);
		driveForward2.addEvent(timer3);
		turn2.addEvent(gyroEvent2);
		driveForward3.addEvent(timer4);
		turn3.addEvent(gyroEvent3);

		// store everything
		autoStates0.add(startIdle);
		autoStates0.add(driveForward1);
		autoStates0.add(driveForward2);
		autoStates0.add(driveForward3);
		autoStates0.add(turn1);
		autoStates0.add(turn2);
		autoStates0.add(turn3);
		autoStates0.add(deadEnd);
		
		autoEvents0.add(timer1);
		autoEvents0.add(timer2);
		autoEvents0.add(timer3);
		autoEvents0.add(timer4);
		autoEvents0.add(gyroEvent1);
		autoEvents0.add(gyroEvent2);
		autoEvents0.add(gyroEvent3);


	}

}

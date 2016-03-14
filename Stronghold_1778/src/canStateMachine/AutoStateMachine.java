package canStateMachine;

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
		// Eight possible state machine slots (three select switches)
		// Default for each slot is... do nothing
		
		//--- STATE MACHINE 0: add a do nothing state machine (auto disabled anyway at 0 index)
		createDoNothingSM(0);
		
		//*** STATE MACHINE 1: add a drive straight and stop (slow & steady) state machine
		createDriveForwardSM_Slow(1);
				
		//--- STATE MACHINE 2: add a drive straight (fast & short) state machine
		createDriveForwardSM_Fast(2);
		
		//--- STATE MACHINE 3: add a do nothing state machine
		createDoNothingSM(3);
		
		//--- STATE MACHINE 4: add a drive straight (super fast & super short) state machine
		createDriveForwardSM_SuperFast(4);
		
		//--- STATE MACHINE 5: add a do nothing state machine
		createDoNothingSM(5);

		//--- STATE MACHINE 6: add a do nothing state machine
		createDoNothingSM(6);

		//--- STATE MACHINE 7: add a do nothing state machine
		createDoNothingSM(7);

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
				
				//System.out.println("State machine switching to " + nextState.name);		
				
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
		
		// return index value for network selected
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

	// **** MOVE FORWARD STATE MACHINE - slow and steady ***** 
	// ** Good for moat, rough terrain, portcullis
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) go back to idle and stay there 
	private void createDriveForwardSM_Slow(int index) {

		// create states
		boolean isPwm = false;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State - Slow>", isPwm, 0.65);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(8.0);  // drive forward timer event
		
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
	
	// **** MOVE FORWARD STATE MACHINE - faster, shorter, more power ***** 
	// ** Good for <UNKNOWN> ramparts?  stone wall?
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) go back to idle and stay there 
	private void createDriveForwardSM_Fast(int index) {

		// create states
		boolean isPwm = false;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State - Fast>", isPwm, 0.85);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(6.0);  // drive forward timer event
		
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
	
	// **** MOVE FORWARD STATE MACHINE - superfast, super short, more power ***** 
	// ** Good for <UNKNOWN>
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) go back to idle and stay there 
	private void createDriveForwardSM_SuperFast(int index) {

		// create states
		boolean isPwm = false;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State - SuperFast>", isPwm, 0.95);
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(3.0);  // drive forward timer event
		
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

	// **** MOVE FORWARD & SHOOT STATE MACHINE ***** 
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) calibrate auto-shooter with target
	// 4) drop ball from conveyer into catapult
	// 5) shoot and reset catapult
	// 6) go back to idle and stay there 
	private void createDriveForwardAndShootSM(int index) {

		// create states
		boolean isPwm = false;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State>", isPwm, 0.65);
		ConveyerStartState conveyerIn = new ConveyerStartState("<Conveyer In State>",true);
		CalibrateShooterState calShooter = new CalibrateShooterState("<Cal Shooter State>");
		ShootAndResetCatapultState shootBallAndReset = new ShootAndResetCatapultState("<Shoot and Reset Catapult State>");
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // timer event for idle
		TimeEvent timer2 = new TimeEvent(7.0);  // timer event for drive forward
		TimeEvent timer3 = new TimeEvent(2.0);  // timer event for conveyer operation	
		CalibrateEvent calEvent1 = new CalibrateEvent(true);  // shooter calibrated event
		TimeEvent timer4 = new TimeEvent(5.0);  // timer event for catapult fire & reset
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward);
		timer2.associateNextState(conveyerIn);
		timer3.associateNextState(calShooter);
		calEvent1.associateNextState(shootBallAndReset);
		timer4.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward.addEvent(timer2);
		conveyerIn.addEvent(timer3);
		calShooter.addEvent(calEvent1);
		shootBallAndReset.addEvent(timer4);
	
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();
				
		myStates.add(startIdle);
		myStates.add(driveForward);
		myStates.add(conveyerIn);
		myStates.add(calShooter);
		myStates.add(shootBallAndReset);
		myStates.add(deadEnd);
		
		myEvents.add(timer1);
		myEvents.add(timer2);
		myEvents.add(timer3);
		myEvents.add(calEvent1);
		myEvents.add(timer4);
		
		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);

	}

	// **** DRIVE-TURN-DRIVE-SHOOT (Spybot) STATE MACHINE ***** 
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) turn a number of degrees
	// 4) drive forward for a number of sec
	// 5) drop ball from conveyer into catapult
	// 6) calibrate auto-shooter with target
	// 7) shoot and reset catapult
	// 8) go back to idle and stay there 
	private void createDriveTurnShootSM(int index) {

		// create states
		boolean isPwm = false;
		IdleState startIdle = new IdleState("<Start Idle State>");
		DriveForwardState driveForward = new DriveForwardState("<Drive Forward State>", isPwm, 0.65);
		TurnState turnLeft = new TurnState("<Turn Left State>",-135.0, 0.3, isPwm);
		DriveForwardState driveForward2 = new DriveForwardState("<Drive Forward State 2>", isPwm, 0.65);
		ConveyerStartState conveyerIn = new ConveyerStartState("<Conveyer In State>",true);
		CalibrateShooterState calShooter = new CalibrateShooterState("<Cal Shooter State>");
		ShootAndResetCatapultState shootBallAndReset = new ShootAndResetCatapultState("<Shoot and Reset Catapult State>");
		IdleState deadEnd = new IdleState("<Dead End State>");
		
		// create events (between the states)
		TimeEvent timer1 = new TimeEvent(0.5);  // 0.5s timer event
		TimeEvent timer2 = new TimeEvent(3.0);  // 0.5s timer event
		GyroAngleEvent gyro1 = new GyroAngleEvent(-135.0);  // -135 deg (left turn) event
		TimeEvent timer3 = new TimeEvent(3.0);  // timer event for second drive forward	
		TimeEvent timer4 = new TimeEvent(2.0);  // timer event for conveyer operation	
		CalibrateEvent calEvent1 = new CalibrateEvent(true);  // shooter calibrated event
		TimeEvent timer5 = new TimeEvent(5.0);  // timer for catapult being fired
		
		// connect each event with a state to move to
		timer1.associateNextState(driveForward);
		timer2.associateNextState(turnLeft);
		timer3.associateNextState(driveForward2);
		gyro1.associateNextState(conveyerIn);
		timer4.associateNextState(calShooter);
		calEvent1.associateNextState(shootBallAndReset);
		timer5.associateNextState(deadEnd);
		
		// add events to each state
		startIdle.addEvent(timer1);
		driveForward.addEvent(timer2);
		turnLeft.addEvent(gyro1);
		driveForward2.addEvent(timer3);
		conveyerIn.addEvent(timer4);
		calShooter.addEvent(calEvent1);
		shootBallAndReset.addEvent(timer5);
		
		// store everything
		ArrayList<AutoState> myStates = new ArrayList<AutoState>();
		ArrayList<Event> myEvents = new ArrayList<Event>();
				
		myStates.add(startIdle);
		myStates.add(driveForward);
		myStates.add(turnLeft);
		myStates.add(driveForward2);
		myStates.add(conveyerIn);
		myStates.add(calShooter);
		myStates.add(shootBallAndReset);
		myStates.add(deadEnd);
		
		myEvents.add(timer1);
		myEvents.add(timer2);
		myEvents.add(gyro1);
		myEvents.add(timer3);
		myEvents.add(timer4);
		myEvents.add(calEvent1);
		myEvents.add(timer5);
		
		// insert into the network arrays
		autoStates.add(index, myStates);
		autoEvents.add(index, myEvents);

	}
	

	// **** STOP-START-REPEAT STATE MACHINE ****
	// Note: not currently used
	private void createStopStartRepeatSM(int index) {

		// create states
		boolean isPwm = false;
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

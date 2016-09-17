package StateMachine;

import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class AutoNetworkBuilder {
		
	private final static String PREF_ROOT = "ChillOutAutonomousNetworks";
	private static Preferences prefRoot, prefs;
	
	private static ArrayList<AutoNetwork> autoNets;
	
	private static boolean initialized = false;
		
	public static void initialize() throws Exception {
		
		if (!initialized) {
			autoNets = null;
			prefRoot = Preferences.userRoot();
			prefs = prefRoot.node(PREF_ROOT);
			
			initialized = true;
		}
	}
	
	public static ArrayList<AutoNetwork> readInNetworks() {
		
		try {
			if (!initialized)
				initialize();
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		autoNets = new ArrayList<AutoNetwork>();
			
		/***** use only when storing the preferences first time *****/
		
		// clear current preferences keys from previous runs
		try {
			prefs.clear();
			Preferences node = prefs.node("<Do Nothing Network>");
			node.removeNode();
		}
		catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		// create networks
		autoNets.add(0, createDoNothingNetwork());	
		autoNets.add(1, createTargetFollowerNetwork());	
		autoNets.add(2, createDriveForwardNetwork_Slow());	
		autoNets.add(3, createDriveForwardForeverNetwork());	
		autoNets.add(4, createTestNetwork());
		
		// add the networks to the prefs object
		int counter = 0;
		for (AutoNetwork a: autoNets)
			a.persistWrite(counter++,prefs);		
				
		// store networks to file
	    try {
	        FileOutputStream fos = new FileOutputStream("/home/lvuser/chillOutAutoNets.xml");
	        prefs.exportSubtree(fos);
	        fos.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	   
		
		/**** TODO: normal operation - read in preferences file ***/
		
		return autoNets;
	}
	
	private void parseSingleNetwork() {
		
	}
		
	// **** DO NOTHING Network ***** 
	private static AutoNetwork createDoNothingNetwork() {
		
		AutoNetwork autoNet = new AutoNetwork("<Do Nothing Network>");
		
		AutoState idleState = new AutoState("<Idle State>");
		IdleAction deadEnd = new IdleAction("<Dead End Action>");
		idleState.addAction(deadEnd);

		autoNet.addState(idleState);	
		
		return autoNet;
	}

	
	// ****  [FOLLOW TARGET] Network - mainly for autotargeting testing - does not shoot ***** 
	// 1) be idle for a number of sec
	// 2) calibrate shooter continuously!  Never stop following target!  NEVER!
	private static AutoNetwork createTargetFollowerNetwork() {

		AutoNetwork autoNet = new AutoNetwork("<Target Follower Network>");

		// create states
		AutoState idleState = new AutoState("<Idle State 1>");
		IdleAction idleStart = new IdleAction("<Idle Action 1>");
		IdleAction doSomething2 = new IdleAction("<Placeholder Action 2>");
		IdleAction doSomething3 = new IdleAction("<Placeholder Action 3>");
		TimeEvent timer1 = new TimeEvent(0.5);  // timer event
		idleState.addAction(idleStart);
		idleState.addAction(doSomething2);
		idleState.addAction(doSomething3);
		idleState.addEvent(timer1);
		
		AutoState targetCalState = new AutoState("<Cal Target FOREVER State 1>");
		CalibrateTargetAction calTarget = new CalibrateTargetAction("<Cal Target Action 1>");
		IdleAction doSomething4 = new IdleAction("<Placeholder Action 4>");
		IdleAction doSomething5 = new IdleAction("<Placeholder Action 5>");
		targetCalState.addAction(calTarget);
		targetCalState.addAction(doSomething4);
		targetCalState.addAction(doSomething5);
		
		// connect each state with a state to move to
		idleState.associateNextState(targetCalState);
						
		autoNet.addState(idleState);
		autoNet.addState(targetCalState);
		
		return autoNet;
	}

	// **** MOVE FORWARD Network - slow and steady ***** 
	// 1) be idle for a number of sec
	// 2) drive forward for a number of sec
	// 3) go back to idle and stay there 
	private static AutoNetwork createDriveForwardNetwork_Slow() {
		
		AutoNetwork autoNet = new AutoNetwork("<Drive Forward Network - Slow>");
		
		AutoState idleState = new AutoState("<Idle State 1>");
		IdleAction startIdle = new IdleAction("<Start Idle Action 1>");
		IdleAction doSomething2 = new IdleAction("<Placeholder Action 2>");
		IdleAction doSomething3 = new IdleAction("<Placeholder Action 3>");
		TimeEvent timer1 = new TimeEvent(0.5);  // timer event
		idleState.addAction(startIdle);
		idleState.addAction(doSomething2);
		idleState.addAction(doSomething3);
		idleState.addEvent(timer1);

		AutoState driveState = new AutoState("<Drive State 1>");
		DriveForwardAction driveForward = new DriveForwardAction("<Drive Forward Action - Slow>", 0.25);
		IdleAction doSomething4 = new IdleAction("<Placeholder Action 4>");
		IdleAction doSomething5 = new IdleAction("<Placeholder Action 5>");
		TimeEvent timer2 = new TimeEvent(3.0);  // drive forward timer event
		driveState.addAction(driveForward);
		idleState.addAction(doSomething4);
		idleState.addAction(doSomething5);
		driveState.addEvent(timer2);
		
		AutoState idleState2 = new AutoState("<Idle State 2>");
		IdleAction deadEnd = new IdleAction("<Dead End Action>");
		idleState2.addAction(deadEnd);
				
		// connect each event with a state to move to
		idleState.associateNextState(driveState);
		driveState.associateNextState(idleState2);
						
		autoNet.addState(idleState);
		autoNet.addState(driveState);
		autoNet.addState(idleState2);
				
		return autoNet;
	}
	
	// **** MOVE FORWARD FOREVER Network - slow and steady ***** 
	// 1) be idle for a number of sec
	// 2) drive forward forever (never stop)
	private static AutoNetwork createDriveForwardForeverNetwork() {
		
		AutoNetwork autoNet = new AutoNetwork("<Drive Forward Network - Slow>");
		
		AutoState idleState = new AutoState("<Idle State 1>");
		IdleAction startIdle = new IdleAction("<Start Idle Action 1>");
		IdleAction doSomething2 = new IdleAction("<Placeholder Action 2>");
		IdleAction doSomething3 = new IdleAction("<Placeholder Action 3>");
		TimeEvent timer1 = new TimeEvent(0.5);  // timer event
		idleState.addAction(startIdle);
		idleState.addAction(doSomething2);
		idleState.addAction(doSomething3);
		idleState.addEvent(timer1);

		AutoState driveState = new AutoState("<Drive State 1>");
		DriveForwardAction driveForward = new DriveForwardAction("<Drive Forward Action - Slow>", 0.25);
		IdleAction doSomething4 = new IdleAction("<Placeholder Action 4>");
		IdleAction doSomething5 = new IdleAction("<Placeholder Action 5>");
		driveState.addAction(driveForward);
		idleState.addAction(doSomething4);
		idleState.addAction(doSomething5);
						
		// connect each event with a state to move to
		idleState.associateNextState(driveState);
						
		autoNet.addState(idleState);
		autoNet.addState(driveState);
				
		return autoNet;
	}
	
	// **** Test Network - does nothing except transitions states ***** 
	private static AutoNetwork createTestNetwork() {
		
		AutoNetwork autoNet = new AutoNetwork("<Test Network>");
		
		AutoState idleState = new AutoState("<Idle State 1>");
		IdleAction startIdle = new IdleAction("<Start Idle Action 1>");
		IdleAction doSomething2 = new IdleAction("<Placeholder Action 2>");
		IdleAction doSomething3 = new IdleAction("<Placeholder Action 3>");
		TimeEvent timer1 = new TimeEvent(10.0);  // timer event
		idleState.addAction(startIdle);
		idleState.addAction(doSomething2);
		idleState.addAction(doSomething3);
		idleState.addEvent(timer1);
		
		AutoState idleState2 = new AutoState("<Idle State 2>");
		IdleAction startIdle2 = new IdleAction("<Start Idle Action 2>");
		IdleAction doSomething4 = new IdleAction("<Placeholder Action 4>");
		IdleAction doSomething5 = new IdleAction("<Placeholder Action 5>");
		TimeEvent timer2 = new TimeEvent(10.0);  // timer event
		idleState2.addAction(startIdle2);
		idleState2.addAction(doSomething4);
		idleState2.addAction(doSomething5);
		idleState2.addEvent(timer2);
		
		AutoState idleState3 = new AutoState("<Idle State 3>");
		IdleAction startIdle3 = new IdleAction("<Start Idle Action 3>");
		IdleAction doSomething6 = new IdleAction("<Placeholder Action 6>");
		IdleAction doSomething7 = new IdleAction("<Placeholder Action 7>");
		TimeEvent timer3 = new TimeEvent(10.0);  // timer event
		idleState3.addAction(startIdle3);
		idleState3.addAction(doSomething6);
		idleState3.addAction(doSomething7);
		idleState3.addEvent(timer3);
		
		AutoState idleState4 = new AutoState("<Idle State 4>");
		IdleAction deadEnd = new IdleAction("<Dead End Action>");
		idleState4.addAction(deadEnd);
				
		// connect each event with a state to move to
		idleState.associateNextState(idleState2);
		idleState2.associateNextState(idleState3);
		idleState3.associateNextState(idleState4);
						
		autoNet.addState(idleState);
		autoNet.addState(idleState2);
		autoNet.addState(idleState3);
		autoNet.addState(idleState4);
				
		return autoNet;
	}

	
}

package StateMachine;

import java.util.ArrayList;

import NetworkComm.InputOutputComm;

public class AutoStateMachine {
		
	private boolean autoNetworkEnable = false;
		
	private ArrayList<AutoNetwork> autoNetworks;

	private AutoNetwork currentNetwork;
	
	public AutoStateMachine()
	{
		// create list of autonomous networks
		autoNetworks = AutonomousNetworkParser.readInNetworks();
		
	}
				
	public void start()
	{
		
		// determine if we are running auto or not
		int networkIndex = getNetworkIndex();
		
		String myString = new String("autoNetworkEnable = " + autoNetworkEnable + ", networkIndex = " + networkIndex);
		System.out.println(myString);
		InputOutputComm.putString(InputOutputComm.LogTable.kMainLog,"Auto/AutoSM_network", myString);
		
		if (autoNetworkEnable)
		{
			// if we have a state network
			currentNetwork = autoNetworks.get(networkIndex);
			
			if (currentNetwork != null)
			{	
				//System.out.println("State machine starting with " + currentState.name);						
				currentNetwork.enter();
			}
		}
		
	}
	
	public void process()  {
		
		if (autoNetworkEnable)
		{
			// process the current network
			if (currentNetwork != null)
			{
				currentNetwork.process();
			}	
		}
	}
	
	public void stop()  {
		if (currentNetwork != null)
		{
			currentNetwork.exit();
		}	
		
	}

	// computes binary value from digital inputs.  
	// If all switches are false (zero), auto is disabled
	private int getNetworkIndex()
	{
		int value = 0;
		
		value = 1;  // testing only

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
	

}

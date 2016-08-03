package Systems;

/**
 * Standard interface for Systems
 * 
 * @author Scott
 *
 */

public interface System {
	
	// Returns a boolean representing whether the Updater should run update()
	public boolean isInitialized();

	// This method will be run as the robot starts up in RobotInit
	public void initialize();
	
	//This runs at the beginning of Autonomous mode
	public void autonomousInit();
	
	//This runs at the beginning on Telop mode.
	public void teleopInit();
	
	// This method will be run regularly in teleop
	public void teleopPeriodic();
	
	//This runs if the system is disable.  Run as safety measure.
	public void disabledInit();
	
	//This runs at the initialization for test mode.
	public void testInit();
	
	//This runs regularly during test mode
	public void testPeriodic();
	
}

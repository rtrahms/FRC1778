package Sensors;

public interface Sensor {
	
	//Return true if sensor is initialized
	public boolean isInitialized();
	
	//Use this as the robot powers up
	public void initialize();
	
	//This is run at the start of Autonomous
	public void autoInit();
	
	//This is run periodically during Autonoumous
	public void autoPeriodic();
	
	//This is run at the start of teleop
	public void teleopInit():
	
	//Run this during a reset command
	public void Reset();
	
	//Run this at start of test mode
	public void testInit();
	
	//Run this periodically during test mode
	public void testPeriodic();

}


package org.usfirst.frc.team1778.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * FIRST 1778 CannonBot 2016 -now with RioDuino communication!
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	RioDuinoAssembly rioDuino;
	DriverStation.Alliance teamColor;
	int teamLocation;
	String rioDuinoStr;
	boolean normalState;
	
	// joystick device id
	private static final int JOYSTICK_ID = 0;
	
    // joystick object
    private static Joystick testJoy;

    public void robotInit() {
		System.out.println("in robotInit()");
		
        testJoy = new Joystick(JOYSTICK_ID);
    	
		rioDuino = new RioDuinoAssembly();
		rioDuinoStr = "disabledInit";
		rioDuino.SendString(rioDuinoStr);
		normalState = true;
    }

    // autonomous methods
    public void autonomousInit() {

    	teamColor = DriverStation.getInstance().getAlliance();
    	teamLocation = DriverStation.getInstance().getLocation();

    	System.out.println("in autonomousInit(), station #" + teamLocation);

    	if (teamColor == DriverStation.Alliance.Blue)
    		rioDuinoStr = "autoInitBlue";
    	else
    		rioDuinoStr = "autoInitRed";
    	rioDuino.SendString(rioDuinoStr);
    }
    
    public void autonomousPeriodic() {
		//System.out.println("in autonomousPeriodic()");
    	
    }

    // operator control methods
    public void teleopInit() {

    	teamColor = DriverStation.getInstance().getAlliance();
    	teamLocation = DriverStation.getInstance().getLocation();
		
    	System.out.println("in teleopInit(), station #" + teamLocation);  
		
    	if (teamColor == DriverStation.Alliance.Blue)
    		rioDuinoStr = "teleopInitBlue";
    	else
    		rioDuinoStr = "teleopInitRed";
    	rioDuino.SendString(rioDuinoStr);
    }
    
    public void teleopPeriodic() {
		//System.out.println("in teleopPeriodic()"); 
		
    	// if we get user input, temporarily switch away from current state to send event to rioduino
		double joyValue = testJoy.getY();
		if (Math.abs(joyValue) > 0.1)
		{
			normalState = false;
			rioDuino.SendString("userInput1");
		}
		
		// if we sent some user input to rioduino, switch back to last normal state
		if (!normalState)
		{
			rioDuino.SendString(rioDuinoStr);
			normalState = true;
		}
    }
    
    // test methods
    public void testInit() {
    	rioDuinoStr = "testInit";
		rioDuino.SendString(rioDuinoStr);
    }
    
    public void testPeriodic() {
    
    }
    
    // disabled methods
    public void disabledInit() {
		System.out.println("in disabledInit()");  
		rioDuino.SendString("disabledInit");
    }

    public void disabledPeriodic() {
        
    }
    
}

package StateMachine;

import java.util.prefs.Preferences;

import Systems.NavXSensor;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Utility;

// event triggered when gyro gets to a certain predetermined angle
public class GyroAngleEvent extends Event {
	
	private String name;
	
	// which side of the gyro angle determines the trigger
	public enum AnglePolarity { kGreaterThan, kLessThan };

	private double angleToTurn = 0.0;
	private double accuracyDeg = 5.0;
	private AnglePolarity polarity;
	
	public GyroAngleEvent(double angleToTurn, AnglePolarity polarity)
	{
		this.name = "<Gyro Angle Event>";
		
		this.angleToTurn = angleToTurn;
		this.polarity = polarity;
		
		NavXSensor.initialize();
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("GyroAngleEvent initialized!");
		NavXSensor.reset();
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		
		if (polarity == AnglePolarity.kGreaterThan) {
			// trigger only if angle is greater than target
			if ((NavXSensor.getYaw() - angleToTurn) > 0) {
				System.out.println("GyroAngleEvent triggered!");
				return true;
			}
		}
		else {
			// trigger only if angle is less than target
			if ((NavXSensor.getYaw() - angleToTurn) < 0) {
				System.out.println("GyroAngleEvent triggered!");
				return true;
			}			
		}
		
		return false;
	}
	
	public void persistWrite(int counter, Preferences prefs) {

		// create node for event
		Preferences eventPrefs = prefs.node(counter + "_" + this.name);
	
		// store event details
		eventPrefs.put("class",this.getClass().toString());
		eventPrefs.putDouble("angleToTurn", this.angleToTurn);
	}

}

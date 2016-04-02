package canStateMachine;

import Systems.GyroSensor;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Utility;

// event triggered when gyro gets to a certain predetermined angle
public class GyroAngleEvent extends Event {
	
	// which side of the gyro angle determines the trigger
	public enum AnglePolarity { kGreaterThan, kLessThan };

	private double angleToTurn = 0.0;
	private double accuracyDeg = 5.0;
	private AnglePolarity polarity;
	
	public GyroAngleEvent(double angleToTurn, AnglePolarity polarity)
	{
		this.angleToTurn = angleToTurn;
		this.polarity = polarity;
		
		GyroSensor.initialize();
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("GyroAngleEvent initialized!");
		GyroSensor.reset();
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		//System.out.println("angle = " + myGyro.getAngle() + " angleToTurn = " + angleToTurn);
		/*
		 * old code - possible to move past angle and NOT trigger!
		if (Math.abs(GyroSensor.getAngle() - angleToTurn) < accuracyDeg)
		{
			System.out.println("GyroAngleEvent triggered!");
			return true;
		}
		*/
		
		if (polarity == AnglePolarity.kGreaterThan) {
			// trigger only if angle is greater than target
			if ((GyroSensor.getAngle() - angleToTurn) > 0) {
				System.out.println("GyroAngleEvent triggered!");
				return true;
			}
		}
		else {
			// trigger only if angle is less than target
			if ((GyroSensor.getAngle() - angleToTurn) < 0) {
				System.out.println("GyroAngleEvent triggered!");
				return true;
			}			
		}
		
		return false;
	}

}

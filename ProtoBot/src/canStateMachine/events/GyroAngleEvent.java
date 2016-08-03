package canStateMachine.events;

import Systems.GyroSensor;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Utility;

/**
 * GyroAngleEvents (degrees, polarity)
 * @author ScottD
 *
 */

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
	public void initialize (String args)
	{
		ParseMyArguments(args);
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
	
	public void ParseMyArguments(String argSection)
	{
		String arg;
		int argEndPos, argStartPos;
		
		argEndPos = argSection.indexOf(",")+1;		
		arg  = argSection.substring(0, argEndPos).trim();			
		angleToTurn = Double.parseDouble(arg);
		
		argStartPos = argEndPos + 1 ;
		arg  = argSection.substring(argStartPos).trim();	
		accuracyDeg = Double.parseDouble(arg);
	
	}

}

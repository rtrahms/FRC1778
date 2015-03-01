package canStateMachine;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Utility;

// event triggered when gyro gets to a certain predetermined angle
public class GyroAngleEvent extends Event {
	
	private double angleToTurn = 0.0;
	private Gyro myGyro;
	private double accuracyDeg = 2.0;
	
	public GyroAngleEvent(double angleToTurn)
	{
		this.angleToTurn = angleToTurn;
        myGyro = new Gyro(0);
	}
	
	// overloaded initialize method
	public void initialize()
	{
		//System.out.println("GyroAngleEvent initialized!");
		myGyro.reset();
		
		super.initialize();
	}
	
	// overloaded trigger method
	public boolean isTriggered()
	{
		//System.out.println("angle = " + myGyro.getAngle() + " angleToTurn = " + angleToTurn);
		if (Math.abs(myGyro.getAngle() - angleToTurn) < accuracyDeg)
		{
			System.out.println("GyroAngleEvent triggered!");
			return true;
		}
		
		return false;
	}

}

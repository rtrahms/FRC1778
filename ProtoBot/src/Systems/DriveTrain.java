package Systems;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveTrain implements Systems  {
	static CANTalon motorL,motorR;
	
	// takes motor IDs and sets them to the correct motor variable
	public DriveTrain(int leftMotorID,int rightMotorID){
		motorL = new CANTalon(leftMotorID);
		motorR = new CANTalon(rightMotorID);
	}
	
	// call to change the power given to the motor
	public static void ChangeSpeed(double powerL,double powerR){
		motorL.set(powerL);
		motorR.set(powerR);
	}
}
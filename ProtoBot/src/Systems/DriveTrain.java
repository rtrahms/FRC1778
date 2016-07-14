package Systems;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveTrain {
	CANTalon motorL,motorR;
	
	public DriveTrain(int leftMotorID,int rightMotorID){
		motorL = new CANTalon(leftMotorID);
		motorR = new CANTalon(rightMotorID);
	}
	
	public void ChangeSpeed(float powerL,float powerR){
		motorL.set(powerL);
		motorR.set(powerR);
	}
}
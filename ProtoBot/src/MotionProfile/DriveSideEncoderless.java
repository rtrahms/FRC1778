package motionProfile;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * 
 * @author samcf_000
 * @param _motorA
 *            A SpeedController to set speed
 * @param _motorB
 *            A SpeedController to set speed
 * @param _enc
 *            An Encoder to read distance from
 */
public class DriveSideEncoderless implements ProfileOutput {

	private ArrayList<SpeedController> motors;

	public DriveSideEncoderless(ArrayList<SpeedController> _motors) {
		this.motors = _motors;
	}

	public void addMotor(SpeedController _motor) {
		motors.add(_motor);
	}

	@Override
	public void setSpeed(double speed) {
		for (int i = 0; i < motors.size(); i++) {
			motors.get(i).set(speed);
		}
	}

	public double getSpeed() {
		return motors.get(0).get();
	}
}

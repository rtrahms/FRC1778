package motionProfile;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Encoder;
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
public class DriveSide implements ProfileSource, ProfileOutput {

	private ArrayList<SpeedController> motors;
	private Encoder enc;

	public DriveSide(ArrayList<SpeedController> _motors, Encoder _enc) {
		this.enc = _enc;

		motors = new ArrayList<SpeedController>();

		// Copy motors from arg list to the private list
		for (int i = 0; i < _motors.size() - 1; i++) {
			this.motors.add(_motors.get(i));
		}
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

	@Override
	public double getDistance() {
		return enc.getDistance();
	}

	public double getSpeed() {
		return motors.get(0).get();
	}
}

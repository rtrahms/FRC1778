package motionProfile;

/**
 * A single podouble in time within a profile
 * 
 * @author Sam
 *
 */

public class Element {
	public double position;
	public double speed;
	public double acceleration;
	public double jerk;

	public Element(double position, double speed, double acceleration, double jerk) {
		this.position = position;
		this.speed = speed;
		this.acceleration = acceleration;
		this.jerk = jerk;
	}

	public Element() {
	}; // You can initialize an element without parameters

	/**
	 * @return a comma-separated list of values
	 */
	public String toString() {
		return position + "," + speed + "," + acceleration + "," + jerk;
	}
}
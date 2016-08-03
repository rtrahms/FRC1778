package motionProfile;

/**
 * Any class that is used as a source for the profile follower must have a
 * getDistance method, which should return a distance. For a drivetrain, this
 * should be in feet.
 * 
 * @author Sam
 *
 */
public interface ProfileSource {
	public double getDistance();
}

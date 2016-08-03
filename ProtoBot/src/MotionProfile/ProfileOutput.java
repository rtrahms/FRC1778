package motionProfile;

/**
 * Any object used as an output of the profile follower must have a set()
 * method. The set() method should set the speed to the value given between -1
 * and 1.
 * 
 * @author Sam
 *
 */
public interface ProfileOutput {
	public void setSpeed(double speed);
}

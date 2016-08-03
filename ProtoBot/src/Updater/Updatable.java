package updater;

/**
 * Any class that can be updated by the updater must have an update method.
 * 
 * @author Sam
 *
 */
public interface Updatable {
	// This method will be run regularly
	public void update();

	// Returns a boolean representing whether the Updater should run update()
	public boolean isEnabled();

	// Enables the updatable
	public void enable();

	// Disables the updatable
	public void disable();

	// A method to print that describes the updatable
	public String toString();
}

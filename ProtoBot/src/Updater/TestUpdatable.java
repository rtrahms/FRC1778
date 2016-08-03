package updater;

/**
 * A very simple updatable to test the updater and confirm that it is updating
 * on the system. Will spam the console when enabled.
 * 
 * @author Sam Fuchs
 *
 */

public class TestUpdatable implements Updatable {

	boolean isEnabled = false;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		System.out.println("TestUpdatable: Updated");
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		System.out.println("TestUpdatable: Enabled");
		isEnabled = true;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		System.out.println("TestUpdatable: Disabled");
		isEnabled = false;
	}

	@Override
	public String toString() {
		return "Test Updatable";
	}
}

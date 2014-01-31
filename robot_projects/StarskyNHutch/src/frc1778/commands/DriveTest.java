// DriveTest.java

package frc1778.commands;

public class DriveTest extends CommandBase {

    public DriveTest() {
        super("DriveTest");
        requires(drive);
    }

    protected void initialize() {
        drive.TEST();
        System.out.println("DriveTest Init\n");
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
        System.out.println("DriveTest End");
    }

    protected void interrupted() {
        end();
    }
}

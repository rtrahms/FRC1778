// Autonomous command

package frc1778.commands;

public class Autonomous extends CommandBase {

    public Autonomous() {
        super("Autonomous");
        requires(drive);
    }

    protected void initialize() {
      	System.out.println("autonomous command init");
        drive.percentMode();
        drive.brakeMode();
        drive.enable();
    }

    protected void execute() {
        drive.setLeftRight(.5, .5);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
	System.out.println("autonomous end");
        drive.setLeftRight(0.0, 0.0);
        drive.disable();        
    }

    protected void interrupted() {
        end();
    }
}

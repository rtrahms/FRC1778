/**
 * TankDriveWithJoysticks.java
 */
package frc1778.commands;

public class TankDriveWithJoysticks extends CommandBase {
    double last_left;
    double last_right;
    double rate_o_change;
    double deadband;
    double left;
    double right;
    double j2Z;
    double minmax;
    double rate_o_change_multiply;

    public TankDriveWithJoysticks() {
        requires(drive);
    }

//  Called just before this Command runs the first time
    protected void initialize () {
	System.out.println("tank drive with joysticks start\n");
        drive.percentMode();
        drive.brakeMode();
        drive.enable();
        last_left = 0.0;
        last_right = 0.0;
        rate_o_change = 0.0;
        rate_o_change_multiply = 0.05;
    }

//  Called repeatedly when this Command is scheduled to run
    protected void execute () {
    //  get joystick values
        left  = oi.getLeftSpeed();
        right = oi.getRightSpeed();
        j2Z   = oi.getRightThrottle();

    //  base everything on the throttle
        rate_o_change = (j2Z + 1) * rate_o_change_multiply;
        deadband = 0.1;
        minmax = (j2Z + 1) / 2;

        left  *= minmax;
        right *= minmax;

    //  change the last left value based on the rate of change joystick and deadband
        if (Math.abs(left) > Math.abs(deadband)) {
            if (last_left < left) {
                last_left += rate_o_change;
            }
            if (last_left > left) {
                last_left -= rate_o_change;
            }
        }
        
    //  check if joystick is within deadband
        if (left < deadband && left > -deadband) {
            last_left = 0;
        }

    //  check if the last_left value is within the minmax
        if (last_left >= minmax) {
            last_left = minmax;
        } 
        else if (last_left <= -minmax) {
            last_left = -minmax;
        }

    //  change the last right value based on the rate of change joystick and deadband
        if (Math.abs(right) > Math.abs(deadband)) {
            if (last_right >= right) {
                last_right -= rate_o_change;
            }
            if (last_right < right) {
                last_right += rate_o_change;
            }
        }

    //  check if joystick is within the deadband
        if (right < deadband && right > -deadband) {
            last_right = 0;
        }

    //  check if the last right value is within the minmax
        if (last_right > minmax) {
            last_right = minmax;
        } else if (last_right < -minmax) {
            last_right = -minmax;
        }
    //  set the motor values
        drive.setLeftRight(last_left, last_right);
    }

//  we're never done - we gotta be kicked out
    protected boolean isFinished() {
	return false;
    }

//  Called once after isFinished returns true
    protected void end() {
	System.out.println("tank drive with joysticks stop");
        drive.setLeftRight(0.0, 0.0);
        drive.disable();
    }

//  Called when another command which requires one or more of the same
//  subsystems is scheduled to run
    protected void interrupted() {
	end();
    }
}

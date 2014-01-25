// DriveTest.java

package frc1778.commands;

public class DriveTest extends CommandBase {
    void DriveTest() {       
        requires(drive);
    }
    
    protected void initialize ()
    {   drive.TEST ();
        System.out.println ("DriveTest Init\n");
    }

//  Called repeatedly when this Command is scheduled to run
    protected void execute () {}

//  Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished ()
    {   return true;
    }

//  Called once after isFinished returns true
    protected void end ()
    {   System.out.println("DriveTest End");
    }

//  Called when another command which requires one or more of the same
//  subsystems is scheduled to run
    protected void interrupted ()
    {   end ();
    }
}

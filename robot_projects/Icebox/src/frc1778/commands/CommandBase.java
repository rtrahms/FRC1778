package frc1778.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc1778.OI;
import frc1778.subsystems.Drive;
import frc1778.subsystems.Gate;
import frc1778.subsystems.Roller;
// import frc1778.subsystems.CameraSubsystem

public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static Drive drive;
    public static Roller roller;
    public static Gate gate;
    // public static CameraSubsystem camera;

    // TODO: Declare a single static instance of front gate
    // TODO: Matt should do this
    
    public static void init() {      
        drive = new Drive ();
        roller = new Roller ();
        gate = new Gate ();
        
        //  MUST be init'd here.  don't move it
        oi = new OI();
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}

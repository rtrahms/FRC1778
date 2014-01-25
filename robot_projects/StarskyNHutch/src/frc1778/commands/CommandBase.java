package frc1778.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc1778.OI;
import frc1778.subsystems.Drive;

/**
 * base for all commands.  All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static Drive drive;

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        System.out.println("in CommandBase.init()");
        
        drive = new Drive ();
        System.out.println("in CommandBase.init() 2");
        
        oi = new OI();
        System.out.println("in CommandBase.init() 3");
        

        // Show what command your subsystem is running on the SmartDashboard
//        SmartDashboard.putData(DriveTest);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}

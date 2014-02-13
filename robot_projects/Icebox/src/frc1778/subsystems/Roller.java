/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc1778.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc1778.commands.RollerOp;
import frc1778.RobotMap;

/**
 *
 * @author hudsodav000
 */ 
public class Roller extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    CANJaguar roller;
    RobotMap rMap;
    
    public Roller() {
        super("Roller");
        rMap = new RobotMap();
        rMap.DBG("Roller");
    }
    
    public void setRollerSpeed(double spd) {
        System.out.println("setRollerSpeed" + spd);
        try {
            roller.setX(spd, (byte) 0);
        } catch (CANTimeoutException e) {
            System.out.println("X setRollerSpeed");
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RollerOp());
    }
    
    public void setSafety(boolean enabled) {
        rMap.DBG("setSafety");
        roller.setSafetyEnabled(enabled);
    }
}

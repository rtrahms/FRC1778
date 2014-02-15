package frc1778.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc1778.RobotMap;
import frc1778.commands.GateOp;
public class Gate extends Subsystem {
    final double pid_gate[] = { 0.5, 0.45, 0.6 };
    CANJaguar gate;
    RobotMap rMap;
    public Gate() {
        super("Gate");
        rMap = new RobotMap();
        try
        { 
            gate = new CANJaguar(rMap.GATE_JAG_ID);
            gate.changeControlMode(CANJaguar.ControlMode.kPosition);
            gate.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            gate.setPID(pid_gate[0],pid_gate[1], pid_gate[2]);
            gate.disableControl();
        }
        catch(CANTimeoutException e)
        {
              e.printStackTrace();
        }
    }

    public void initDefaultCommand() 
    {
        setDefaultCommand(new GateOp());
    }

    public void setSafety(boolean enabled) {
        rMap.DBG("setSafety "+(enabled?"on":"off"));
        gate.setSafetyEnabled(enabled);
    }

    /**
     * BOOM - get the motor going
     *
     * @param left
     * @param right
     */ 
    public void setGatePos(double gatepos) {
        rMap.DBG("set gatepos " + gatepos);
        try {
            gate.setX(gatepos);
        } catch (CANTimeoutException e)
        {
            rMap.DBG("X setGatePos");
        }
    }

    /**
     * override jumper and turn brake on
     */
    public void brakeMode() {
        rMap.DBG("breakMode");
        try {
            gate.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X brakeMode");
        }
    }

    /**
     * override jumper and turn braking off so we coast
     */
    public void coastMode() {
        rMap.DBG("coastMode");
        try {
            gate.configNeutralMode(CANJaguar.NeutralMode.kCoast);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X coastMode");
        }
    }
    
    /**
     * non-PID percent voltage mode(the usual one -1..0..1)
     */
    public void percentMode() {
        rMap.DBG("percentMode");
        try {
            gate.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            coastMode();
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X percentMode");
        }
    }
    
    /**
     * non-PID absolute voltage mode
     */
    public void voltageMode() {
        rMap.DBG("voltageMode");
        try {
            gate.changeControlMode(CANJaguar.ControlMode.kVoltage);
            coastMode();
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X voltageMode");
        }
    }

    /**
     * Always need to re-enable jaguar when the mode is changed.
     */
    public void enable() {
        rMap.DBG("voltageMode");
        try {
            gate.enableControl(0.0);
            setSafety(true);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X enable");
        }
    }

    public void disable() {
        rMap.DBG("disable");
        try {
            gate.disableControl();
            setSafety(false);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X disable");
        }
    }

    /**
     * PID speed controlled - call Enable to start PID
     */
    public void speedMode() {
        rMap.DBG("speedMode");
        try {
            gate.changeControlMode(CANJaguar.ControlMode.kSpeed);
            gate.setPID(0.7, 0.0, 0.0);
            coastMode();
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X speedMode");
        }
    }

    /**
     * PID position controlled - call Enable to start PID
     */
    public void positionMode() {
        try {
            gate.changeControlMode(CANJaguar.ControlMode.kPosition);
            gate.setPID(0.5, 0.45, 0.6);
            brakeMode();
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X positionMode");
        }
    }

    /**
     * see if either position mode pid has reached desired spot that we set with
     * Straight()=>SetLeftRight
     * @return ()
     */
    public boolean atPosition() {
        try {
            System.out.println("lGet= " + gate.getX() + " rGet= " + gate.getX());
            if (Math.abs(gate.getX() - gate.getPosition()) < 0.02) {
                return true;
            }
            if (Math.abs(gate.getX() - gate.getPosition()) < 0.02) {
                return true;
            }
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X atPosition");
        }
        return false;
    }

    public double getRightPosition() {
        try {
            return gate.getPosition();
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X getRightPosition");
        }
        return 0.0;
    }

    public void jagDump(String str, CANJaguar j) {
        try {
            int fw = j.getFirmwareVersion();
            double p = j.getPosition();
            double s = j.getSpeed();
            double c = j.getOutputCurrent();
            rMap.DBG(str + " pos= " + p + " speed= " + s + 
                               " current= " + c + "firmware= " + fw);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X jagDump");
        }
    }
}
/**
 * Drive.java - controls the 4 or 2 jaguar that do wheel driving. Including
 * dealing with all the dang jag modes. position, speed and current are PID
 * modes (the =JAG= does the PID on board) remaining are NON-PID modes:
 * percentVBus is the usual -1..0..1 usually used. voltage uses and absolute
 * voltage number - rarely used.
 */
package frc1778.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc1778.RobotMap;
import frc1778.commands.TankDriveWithJoysticks;

public class Drive extends Subsystem {
//  master (back) jaguar are the only ones that listen to the encoders.
//  slave (front) jaguar always follow what the master jaguar do.

    CANJaguar l_Master;
    CANJaguar r_Master;
    CANJaguar l_Slave;
    CANJaguar r_Slave;
    RobotMap rMap;

    public Drive() {
        super("DriveWheels");
        rMap = new RobotMap();
        rMap.DBG("Drive init start");
        try {
        //  master (back) jaguar are the only ones that listen to the encoders.    CANJaguar l_Master;
        //  slave (front) jaguar always follow what the master jaguar do.	
            l_Master = new CANJaguar(rMap.L_MASTER, CANJaguar.ControlMode.kVoltage);
            l_Master.disableControl();
            rMap.DBG("LM jag");
            
            r_Master = new CANJaguar(rMap.R_MASTER, CANJaguar.ControlMode.kVoltage);
            r_Master.disableControl();
            rMap.DBG("RM jag");
 
        //  Slaves are always in kVoltage and can never be in anything else.
            l_Slave = new CANJaguar(rMap.L_SLAVE, CANJaguar.ControlMode.kVoltage);
            rMap.DBG("LS jag");
            
            r_Slave = new CANJaguar(rMap.R_SLAVE, CANJaguar.ControlMode.kVoltage);
            rMap.DBG("RH jag");

            double voltage = l_Master.getBusVoltage();
            l_Master.configMaxOutputVoltage(voltage);
            r_Master.configMaxOutputVoltage(voltage);

            l_Slave.configMaxOutputVoltage(voltage);
            r_Slave.configMaxOutputVoltage(voltage);

            //  enable GetSpeed()
            l_Master.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            r_Master.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            l_Master.configEncoderCodesPerRev(rMap.ENCODER_TICKS_PER_WHEEL_REV);
            r_Master.configEncoderCodesPerRev(rMap.ENCODER_TICKS_PER_WHEEL_REV);
            
        //  enable GetPosition()
            l_Master.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            r_Master.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);

            voltageMode();
            disable();
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X Drive");
        }
        rMap.DBG("Drive end");
    }

    public void initDefaultCommand() { // Set the default command for a subsystem here. 
        setDefaultCommand(new TankDriveWithJoysticks());
    }

    public void setSafety(boolean enabled) {
        rMap.DBG("setSafety " + (enabled ? "on" : "off"));
        l_Master.setSafetyEnabled(enabled);
        r_Master.setSafetyEnabled(enabled);

        l_Slave.setSafetyEnabled(enabled);
        r_Slave.setSafetyEnabled(enabled);
    }

    /**
     * BOOM - set them there motors going
     * @param left
     * @param right
     */
    public void setLeftRight(double left, double right) {
        rMap.DBG("setLeftRight " + left + ", " + right);
        try {
            r_Master.setX(-right, (byte) 0);
            l_Master.setX(left,   (byte) 0);
            
            // slaves always stay in voltage mode 
            r_Slave.setX(r_Master.getOutputVoltage(), (byte) 0);
            l_Slave.setX(l_Master.getOutputVoltage(), (byte) 0);
        } catch (CANTimeoutException e)
        {
            rMap.DBG("X setLeftRight");
        }
    }

    /**
     * override jumper and turn brake on
     */
    public void brakeMode() {
        rMap.DBG("breakMode");
        try {
            l_Master.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            r_Master.configNeutralMode(CANJaguar.NeutralMode.kBrake);

            l_Slave.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            r_Slave.configNeutralMode(CANJaguar.NeutralMode.kBrake);
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
            l_Master.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            r_Master.configNeutralMode(CANJaguar.NeutralMode.kCoast);

            r_Slave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            l_Slave.configNeutralMode(CANJaguar.NeutralMode.kCoast);
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
            l_Master.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            r_Master.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
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
            l_Master.changeControlMode(CANJaguar.ControlMode.kVoltage);
            r_Master.changeControlMode(CANJaguar.ControlMode.kVoltage);
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
            l_Master.enableControl(0.0);
            r_Master.enableControl(0.0);
            setSafety(true);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X enable");
        }
    }

    public void disable() {
        rMap.DBG("disable");
        try {
            l_Master.disableControl();
            r_Master.disableControl();
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
            l_Master.changeControlMode(CANJaguar.ControlMode.kSpeed);
            l_Master.setPID(0.7, 0.0, 0.0);
            r_Master.changeControlMode(CANJaguar.ControlMode.kSpeed);
            r_Master.setPID(0.7, 0.0, 0.0);
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
            l_Master.changeControlMode(CANJaguar.ControlMode.kPosition);
            l_Master.setPID(0.7, 0.0, 0.0);
            r_Master.changeControlMode(CANJaguar.ControlMode.kPosition);
            r_Master.setPID(0.7, 0.0, 0.0);
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
            rMap.DBG("lGet= " + l_Master.getX() + " rGet= " + r_Master.getX());
            jagShow();
            if (Math.abs(r_Master.getX() - r_Master.getPosition()) < 0.02) {
                return true;
            }
            if (Math.abs(l_Master.getX() - l_Master.getPosition()) < 0.02) {
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
            return r_Master.getPosition();
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

    public void jagTest(String str, CANJaguar j) {
        try {
            if (null == j) {
                rMap.DBG(str + " not enabled\n");
                return;
            }
            jagDump(str, j);
            j.setX(0.3);
            Timer.delay(1.0);

            jagDump(str, j);
            j.setX(0.2);
            Timer.delay(1.0);

            jagDump(str, j);
            j.setX(0.1);
            Timer.delay(1.0);

            j.setX(0.0);
            jagDump(str, j);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X jagTest");
        }
    }

    /**
     * put our jags into regular percent coast mode test one at a time so we
     * don't grind nothin then put back in standard mode make sure a side moves
     * in only ONE direction and that net console shows encoders reporting
     * expected signs
     */
    public void TEST() {
        try {
            setSafety(false);
            coastMode();

        //  put in percent mode for some simple testin
            l_Slave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            l_Slave.enableControl(0.0);
            jagTest("left front", l_Slave);
            
        //  put it back the way you found it
            l_Slave.changeControlMode(CANJaguar.ControlMode.kVoltage);
            l_Slave.enableControl(0.0);

        //  same deal for right side
            r_Slave.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            r_Slave.enableControl(0.0);
            jagTest("right front", r_Slave);

            r_Slave.changeControlMode(CANJaguar.ControlMode.kVoltage);
            r_Slave.enableControl(0.0);

            percentMode(); // only sets mode for masters 
            enable();
            setSafety(false); // since Enable sets it true :/ 
            jagTest("left rear", l_Master);
            jagTest("right rear", r_Master);

            setSafety(true);
        } catch (CANTimeoutException e) 
        {
            rMap.DBG("X TEST");
        }
    }

    public void jagShow() {
        jagDump(" leftRear", l_Master);
        jagDump("rightRear", r_Master);
    }
}

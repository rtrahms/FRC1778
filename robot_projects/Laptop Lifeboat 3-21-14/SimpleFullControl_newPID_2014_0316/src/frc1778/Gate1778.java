/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc1778;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author Rob
 */
public class Gate1778 {

    private final int GATE_MOTOR_ID = 6;
    private final int POTENTIOMETER_CHANNEL = 2;
    
    // gate throttle (how fast the gate moves, and direction)
    private final double GATE_STEP_MAGNITUDE_SLOW = 0.4;
    private final double GATE_STEP_MAGNITUDE_FAST = 0.85;
    private final double GATE_STEP_POLARITY_DEFAULT = -1.0;
    
    // PID coefficients for gate jaguar
    //private final double PID_GATE[] = { 0.5, 0.45, 0.6 };
    private final double PID_GATE[] = { 0.1, 0.001, 0.0 };
  
    // potentiometer values for gate position (Test robot)
    private final double GATE_CLOSED = 136.0;
    private final double GATE_OPEN = 0.55;

    // potentiometer values for gate position (Bagged robot)
    //private final double GATE_CLOSED = 0.17;
    //private final double GATE_OPEN = 0.06;
    
    private double gateStep = GATE_STEP_POLARITY_DEFAULT * GATE_STEP_MAGNITUDE_FAST;
    private double gateIncrement = 0.0;
    
    private PIDController gateController;
    
    private CANJaguar gateMotor;
    private AnalogChannel gatePotentiometer;
    
    public Gate1778() {
        
        // instantiate gate motor, PID controller, etc
        
        gatePotentiometer = new AnalogChannel(POTENTIOMETER_CHANNEL);
        
        try {
            // set up gate motor
            gateMotor = new CANJaguar(GATE_MOTOR_ID);

            // do not uncomment these lines unless you have the potentiometer connected
            // directly to the gate jaguar
            //gateMotor.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            
        } catch (CANTimeoutException e) {
            e.printStackTrace();
        }
        
        // instantiate PID controller, point to the gate pot and gate motor
        gateController = new PIDController(PID_GATE[0],PID_GATE[1],PID_GATE[2],
                                            gatePotentiometer, gateMotor, 50);
        gateController.enable();
    }
    
    
    public void enable() {
        try {
            gateMotor.enableControl();
        } catch (CANTimeoutException e) {
            e.printStackTrace();
        }
    }
    
    public void disable() {
        try {
            gateMotor.disableControl();
        } catch (CANTimeoutException e) {
            e.printStackTrace();
        }
    }

    
    // method used during autonomous
    public void open() {

        gateController.setSetpoint(GATE_OPEN);

        /*
        try {
            
            //pot_pos = gateMotor.getPosition();
            //System.out.println("Pot pos = "+ pot_pos);
            
            if(pot_pos < GATE_CLOSED && pot_pos > GATE_OPEN) {
                gateMotor.setX(-gateStep);
            }
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        */
    }
    
    // method used during autonomous
    public void close() {
        
        gateController.setSetpoint(GATE_CLOSED);
        
        /*
        try {
            
            //pot_pos = gateMotor.getPosition();
            //System.out.println("Pot pos = "+ pot_pos);
            
            //if(pot_pos < GATE_CLOSED && pot_pos > GATE_OPEN) {
            //    gateMotor.setX(gateStep);
            //}
            
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }  
        */
    }
    
    // method used during operator control
    public void manualOp(double joystickValue) {
        
        gateStep = GATE_STEP_POLARITY_DEFAULT * GATE_STEP_MAGNITUDE_FAST;
        gateIncrement = joystickValue*gateStep;
        
        // read current potentiometer value
        double set_pos = gatePotentiometer.getAverageValue();
        System.out.println("Gate1778.manualOp: set_pos = " + set_pos);
        
        if(set_pos < GATE_CLOSED && set_pos > GATE_OPEN) {
            set_pos += gateIncrement;
        }
        else{
            set_pos += gateIncrement*0.1;
        }
        gateController.setSetpoint(set_pos);
        
        /*
        gateStep = GATE_STEP_POLARITY_DEFAULT * GATE_STEP_MAGNITUDE_SLOW;
        try {
            pot_pos = gateMotor.getPosition();
            //System.out.println("Gate1778.manualOp: Pot pos = "+ pot_pos);
            
            if(pot_pos < GATE_CLOSED && pot_pos > GATE_OPEN) {
                gateStep = GATE_STEP_POLARITY_DEFAULT * GATE_STEP_MAGNITUDE_FAST; 
            }
            
            // update gate position
            gateIncrement = joystickValue*gateStep;
            System.out.println("Gate1778.manualOp: gateIncrement = " + gateIncrement);

            gateMotor.setX(gateIncrement);
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }    
        */

    }

}

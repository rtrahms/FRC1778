/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc1778;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;
import java.lang.Math.*;

/**
 *
 * @author Rob
 */
public class Ultrasonic1778 {
    
    private final int PING_CHANNEL = 1;
    private final int ECHO_CHANNEL = 2;
    
    private Ultrasonic ultraSensor;
    private DigitalOutput pingChannel;
    private DigitalInput echoChannel;
   
    public Ultrasonic1778() {
        
        pingChannel = new DigitalOutput(PING_CHANNEL);
        echoChannel = new DigitalInput(ECHO_CHANNEL);
        ultraSensor = new Ultrasonic(pingChannel,echoChannel);
        ultraSensor.setAutomaticMode(true);
    }
    
    public double getRangeInches() {
        //ultraSensor.ping();
        return ultraSensor.getRangeInches();
    }
    
    public double getRangeMM() {
        //ultraSensor.ping();
        return ultraSensor.getRangeMM();
    }
    
    public double getRangeLightyears() {
        double distInches = ultraSensor.getRangeInches();
        return distInches * 0.00000000000000000268483946;
    }
    
    public double getRangeApples() {
        double distInches = ultraSensor.getRangeInches();
        return distInches / 3.25;
    }
    
    public double getRangeSmoots() {
        double distInches = ultraSensor.getRangeInches();
        return distInches * 0.0149253731;
    }
    
    public double getRangeAngstroms() {
        double distInches = ultraSensor.getRangeInches();
        return distInches * 254000000;
    }
    
    public double getRangeDaylans() {
        double distInches = ultraSensor.getRangeInches();
        return distInches / 68;
    }
    

}

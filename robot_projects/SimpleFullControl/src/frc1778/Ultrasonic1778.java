/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc1778;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

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
        return ultraSensor.getRangeInches();
    }
    
    public double getRangeMM() {
        return ultraSensor.getRangeMM();
    }

}

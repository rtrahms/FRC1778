/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package starterjriorobot1778;

import first1778.StarterRobot1778;
import org.jRIO.DashboardDisplay.DashboardDisplay;
import org.jRIO.mainframe.DigitalSidecar;
import org.jRIO.mainframe.Main;
import org.jRIO.mainframe.cRIO;

/**
 *
 * @author rob
 */
public class StarterJrioRobot1778 
{

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        new DashboardDisplay();     // creates new Dashboard Display
        cRIO.setModuleCount(false); // false for 8 slot cRIO
        new DigitalSidecar();       // will set default digital sidecar module slot
        
        // assuming that StarterRobot1778 is the main robot class for the project
        Main.setRobotBase(new StarterRobot1778());
        
        Main.start();   // hands control off to the robot for simulation
    }

}


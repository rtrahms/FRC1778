/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iterativerobotsim1778;

import frc1778.*;
import RobotCode.*;
import java.util.Scanner;

/**
 *
 * @author rob
 */
public class IterativeRobotSim1778 {
    static int input;
    static boolean InIsValid;
    static IterativeRobot1778 robot = new IterativeRobot1778();
    static int i = 0;
            
            
    public static void main(String[] args) {

        // test out a teleop
        robot.teleopInit();
        while (true)
        {
            robot.teleopPeriodic();
        }
    }
}

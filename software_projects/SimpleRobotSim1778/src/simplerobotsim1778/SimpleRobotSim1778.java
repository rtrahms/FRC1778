/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simplerobotsim1778;

import RobotCode.*;
import java.util.Scanner;

import frc1778.*;

/**
 *
 * @author rob
 */
public class SimpleRobotSim1778 {
    static int input;
    static boolean InIsValid;
    static SimpleRobot1778 robot = new SimpleRobot1778();
    static int i = 0;
            
            
    public static void main(String[] args) {
        //asks which mode, teleop or autonomous
        do {
            System.out.println("(1) Autonomous \n(2) Teleop");
            
            Scanner scan = new Scanner(System.in);
            input = scan.nextInt();
            
            if (input == 1 || input == 2) {
                InIsValid = true;
            }
            
            if (input == 1)
            {
                // simple autonomous
                robot.autonomous();
            }
            
            if (input == 2)
            {
                // simple teleop
                robot.operatorControl();
            }   
        } while (!InIsValid);
  
    }
}

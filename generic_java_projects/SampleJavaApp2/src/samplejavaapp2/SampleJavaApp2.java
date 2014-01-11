/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package samplejavaapp2;

/**
 *
 * @author Rob
 */
public class SampleJavaApp2 {

    Subsystem driveTrain;
    Subsystem articulator;
    Subsystem soundPlayer;
    Subsystem lightingControl;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Subsystem driveTrain = new Subsystem("Drive Train",10);
        Subsystem articulator = new Subsystem("Articulator",5);
        Subsystem soundPlayer = new Subsystem("Sound Player",20);
        Subsystem lightingControl = new Subsystem("Lighting Control",2);
    }
    
}

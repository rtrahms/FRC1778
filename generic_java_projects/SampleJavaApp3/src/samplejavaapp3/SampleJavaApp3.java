/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package samplejavaapp3;

import java.util.Calendar;


/**
 *
 * @author Rob
 */
public class SampleJavaApp3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Calendar now = Calendar.getInstance();
        
        System.out.println("Current date: " + (now.get(Calendar.MONTH) + 1) +
                                               "-" +
                                               now.get(Calendar.DATE) +
                                               "-" + 
                                               now.get(Calendar.YEAR));
        
        for (int i=0; i < 100; i++) {
            System.out.println("Step " + i);
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
            }
        }
    }
    
}

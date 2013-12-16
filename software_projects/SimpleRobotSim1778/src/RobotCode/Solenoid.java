/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RobotCode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author aubrey
 */
public class Solenoid {
    private JFrame frame;
    private JLabel solenoidstate;
    private boolean state = false;
    
    
    public Solenoid(int channel) {
        frame = new JFrame("Solenoid: " + channel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(800, 0);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(200, 50));
        solenoidstate = new JLabel("State: " + state );
        frame.add(solenoidstate, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void set(boolean statein) {
        state = statein;
        solenoidstate.setText("State: " + state);
        solenoidstate.validate();
        solenoidstate.repaint();
    }
    
    public boolean get() {
        return state;
    }
}

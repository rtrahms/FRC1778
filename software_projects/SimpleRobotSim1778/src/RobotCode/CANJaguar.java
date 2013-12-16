package RobotCode;

/*
 *  This file is part of frcjcss.
 *
 *  frcjcss is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  frcjcss is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with frcjcss.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * A Jaguar speed controller emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.12.2010.3
 */
public class CANJaguar implements ComponentListener, ActionListener {
    
    public static final int kControllerRate = 1000;
    public static final double kApproxBusVoltage = 12.0;
    
    
    private double X;
   
    private long startTime; 
    private boolean isGraphRunning;
    private JFrame frame;
    private JLabel jaguarSpeed;
    private JButton startStop;
    
    private SpeedGrapher graph;

    public static class ControlMode {
        public final int value;
        static final int kPercentVbus_val = 0;
        static final int kCurrent_val = 1;
        static final int kSpeed_val = 2;
        static final int kPosition_val = 3;
        static final int kVoltage_val = 4;
        public static final ControlMode kPercentVbus = new ControlMode(kPercentVbus_val);
        public static final ControlMode kCurrent = new ControlMode(kCurrent_val);
        public static final ControlMode kSpeed = new ControlMode(kSpeed_val);
        public static final ControlMode kPosition = new ControlMode(kPosition_val);
        public static final ControlMode kVoltage = new ControlMode(kVoltage_val);

        private ControlMode(int value) {
            this.value = value;  
        }
    }
    
    /**
     * Faults reported by the Jaguar
     */
    public static class Faults {

        public final int value;
        static final int kCurrentFault_val = 1;
        static final int kTemperatureFault_val = 2;
        static final int kBusVoltageFault_val = 4;
        static final int kGateDriverFault_val = 8;
        public static final Faults kCurrentFault = new Faults(kCurrentFault_val);
        public static final Faults kTemperatureFault = new Faults(kTemperatureFault_val);
        public static final Faults kBusVoltageFault = new Faults(kBusVoltageFault_val);
        public static final Faults kGateDriverFault = new Faults(kGateDriverFault_val);

        private Faults(int value) {
            this.value = value;
        }
    }

    /**
     * Limit switch masks
     */
    public static class Limits {

        public final int value;
        static final int kForwardLimit_val = 1;
        static final int kReverseLimit_val = 2;
        public static final Limits kForwardLimit = new Limits(kForwardLimit_val);
        public static final Limits kReverseLimit = new Limits(kReverseLimit_val);

        private Limits(int value) {
            this.value = value;
        }
    }

    /**
     * Determines which sensor to use for position reference.
     */
    public static class PositionReference {

        public final byte value;
        static final byte kQuadEncoder_val = 0;
        static final byte kPotentiometer_val = 1;
        static final byte kNone_val = (byte)0xFF;
        public static final PositionReference kQuadEncoder = new PositionReference(kQuadEncoder_val);
        public static final PositionReference kPotentiometer = new PositionReference(kPotentiometer_val);
        public static final PositionReference kNone = new PositionReference(kNone_val);

        private PositionReference(byte value) {
            this.value = value;
        }
    }

    /**
     * Determines which sensor to use for speed reference.
     */
    public static class SpeedReference {

        public final byte value;
        static final byte kEncoder_val = 0;
        static final byte kInvEncoder_val = 2;
        static final byte kQuadEncoder_val = 3;
        static final byte kNone_val = (byte)0xFF;
        public static final SpeedReference kEncoder = new SpeedReference(kEncoder_val);
        public static final SpeedReference kInvEncoder = new SpeedReference(kInvEncoder_val);
        public static final SpeedReference kQuadEncoder = new SpeedReference(kQuadEncoder_val);
        public static final SpeedReference kNone = new SpeedReference(kNone_val);

        private SpeedReference(byte value) {
            this.value = value;
        }
    } 
    
     /**
     * Determines which sensor to use for position reference.
     */
    public static class LimitMode {

        public final byte value;
        static final byte kSwitchInputsOnly_val = 0;
        static final byte kSoftPositionLimit_val = 1;
        public static final LimitMode kSwitchInputsOnly = new LimitMode(kSwitchInputsOnly_val);
        public static final LimitMode kSoftPostionLimits = new LimitMode(kSoftPositionLimit_val);

        private LimitMode(byte value) {
            this.value = value;
        }
    }


    /**
     * Creates a new Jaguar speed controller.
     * @param channel The Digital Sidecar channel it should be connected to.
     */
    public CANJaguar(int deviceNumber) {
        frame = new JFrame("CANJaguar Emulator: " + deviceNumber);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setLocation(510, 0);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(300, 320));
        
        //tells the current speed of the jaguar in % above the graph.
        jaguarSpeed = new JLabel("Current Speed: " + (X*100) + "%");
        frame.add(jaguarSpeed, BorderLayout.NORTH);
        
        //allows user to stop the movement of the graph. button located under the graph.
        startStop = new JButton("Stop Graph");
        startStop.addActionListener(this);
        frame.add(startStop, BorderLayout.SOUTH);
        
        //makes the actual graph.
        graph = new SpeedGrapher(300, 300);
        frame.add(graph, BorderLayout.CENTER);
        
        startTime = 0;
        isGraphRunning = true;
        
        frame.addComponentListener(this);

        frame.pack();
        frame.setVisible(true);
    }

	/**
     * Sets the value of the Jaguar using a value between -1.0 and +1.0.
     * @param speed The speed value of the Jaguar between -1.0 and +1.0.
     */
    public void setX(double speed) {
    	if (System.currentTimeMillis() - startTime > 35 && isGraphRunning) {
    		graph.appendSpeed(speed);
    		startTime = System.currentTimeMillis();
    	}
        this.X = speed;
        jaguarSpeed.setText((int)((speed*100)*10)/10.0 + "%");
    }

    /**
     * Gets the most recent value of the Jaguar.
     * @return The most recent value of the Jaguar from -1.0 and +1.0.
     */
    public double getX() {
        return X;
    }
    
    //add pidWrite method?
    
	public void componentResized(ComponentEvent e) {
		graph.setGraphSize(frame.getWidth(), frame.getHeight());
		graph.repaint();
	}
    
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startStop) {
			startStop.setText((isGraphRunning ? "Start" : "Stop") + " Graph");
			isGraphRunning = !isGraphRunning;
		}
	}
	
	//extra stuffs
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}

}
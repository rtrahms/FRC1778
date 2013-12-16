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
 * A Victor speed controller emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.12.2010.3
 */
public class Victor implements ComponentListener, ActionListener {

    private double speed;
    private long startTime;
    private boolean isGraphRunning;

    private JFrame frame;
    private JLabel victorNum;
    private JLabel victorSpeed;
    private JButton startStop;
    
    private SpeedGrapher graph;

    /**
     * Creates a new Victor speed controller.
     * @param channel The Digital Sidecar channel it should be connected to.
     */
    public Victor(int channel) {
        frame = new JFrame("Victor Emulator: " + channel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setLocation(510, 0);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(300, 320));
        
        //tells the current speed of the victor in % above the graph.
        victorSpeed = new JLabel("Current Speed: " + (speed*100) + "%");
        frame.add(victorSpeed, BorderLayout.NORTH);
        
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
     * Sets the value of the Victor using a value between -1.0 and +1.0.
     * @param speed The speed value of the Victor between -1.0 and +1.0.
     */
    public void set(double speed) {
    	if (System.currentTimeMillis() - startTime > 35 && isGraphRunning) {
    		graph.appendSpeed(speed);
    		startTime = System.currentTimeMillis();
    	}
        this.speed = speed;
        victorSpeed.setText((int)((speed*100)*10)/10.0 + "%");
    }

    /**
     * Gets the most recent value of the Victor.
     * @return The most recent value of the Victor from -1.0 and +1.0.
     */
    public double get() {
        return speed;
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
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

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyListener;

/**
 * Joystick emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.11.2010.7
 */
public class Joystick implements KeyListener {
    //TODO: Add Joystick button support using KeyListener
    //      -The above is done for one button at a time. Detecting many buttons being pressed at the same 
    //       time is apparently not a trivial task for at least linux.
    //TODO: Implement offsets and noise.
    
    private final int JSHEIGHT = 500;//joy stick area height
    private final int JSWIDTH = 500;//joy stick area width

    private double x, y, z;//-1 to 1
    private int xpos, ypos, zpos;//pixel position. z is 0-460.
    private double xOffset, yOffset;
    private double drift;
    
    private int button;
    private String buttonStr;
    
    private long keyTime;
    private boolean isTestingTime;

    private boolean mouseClicked = false;
    private boolean trigger = false;

    private JFrame frame;
    private Grid grid;

    /**
     * Creates a new Joystick window based on the Cartesian coordinate system.
     * @param port The port the Joystick is connected to on the Driver Station.
     */
    public Joystick(int port) {
        frame = new JFrame("Joystick Emulator: " + port);
        
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(JSWIDTH, JSHEIGHT+100));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grid = new Grid();
        frame.add(grid, BorderLayout.CENTER);
        
        frame.addKeyListener(this);
        
        zpos = 230; //starting pixel position of z axis(out of 460)
        button = -1;//-1 == no button being pressed.
        
        frame.pack();
        frame.setVisible(true);        
    }
    

    /**
     * The X value of the Joystick.
     * @return The X value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getX() {
        return x;
    }

    /**
     * The Y value of the Joystick.
     * @return The Y value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getY() {
        return y;
    }
    
    /**
     * The Z value of the Joystick.
     * @return The Z value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getZ() {
        return z;
    }
    
    public double getRawAxis(int axis) {
        switch (axis) {
            case 1: return getX();
            case 2: return getY();
            case 3: return getZ();
            default: return 0;
        }
    }
    
     /**
     * The current state of the trigger on the Joystick.
     * @return True if the trigger is being pressed down, false if not.
     */
    public boolean getTrigger() {
        return trigger;
    }
    
    /**
     * Get the button value for buttons 1 through 10, with 0 being 10.
     * @return True if the provided button is being pressed, false if not.
     */
    public boolean getRawButton(int but) {
        return (but == button);
    }
    
    public void keyPressed(KeyEvent e) {
        int key = (int)e.getKeyChar()-48;
        if (key >= 0 && key <= 9) {
        	if(key == 0) {
        		button = 10;
        	}
        	else {
        		button = key;
        	}
        }
        grid.repaint();
    }
    
    public void keyReleased(KeyEvent e) {
        button = -1;
        grid.repaint();
    }
    public void keyTyped(KeyEvent e) {}

    @SuppressWarnings("serial")
    class Grid extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
        Grid() {
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        }
        public void paintComponent(Graphics g) {
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            
            //clears graph.
            g.setColor(Color.white);
            g.fillRect(0, 0, grid.getWidth(), grid.getHeight());
            g.setColor(Color.black);
            
            //checks if trigger is set and draws a red filled rectangle if it is.
            if (trigger) {
                g.setColor(Color.red);
                g.fillRect(xpos-20, ypos-20, 40, 40);
                g.setColor(Color.black);
            }
            
            //draws x and y axis and bottom border of grid.
            g.drawLine(0, JSHEIGHT/2, getWidth(), JSHEIGHT/2);
            g.drawLine(getWidth()/2, 0, getWidth()/2, JSHEIGHT);
            g.drawLine(0, JSHEIGHT, getWidth(), JSHEIGHT);
            
            //draws z axis
            g.drawLine(20,  JSHEIGHT+50, 480, JSHEIGHT+50);
            g.drawLine(20,  JSHEIGHT+25, 20,  JSHEIGHT+75);
            g.drawLine(480, JSHEIGHT+25, 480, JSHEIGHT+75);
            
            //draws zpos
            g.setColor(Color.red);
            g.drawLine(20+(int)zpos, JSHEIGHT+25, 20+(int)zpos, JSHEIGHT+75);
            g.setColor(Color.black);
            g.drawString("z = " + round(z, 3), 225, JSHEIGHT+25);
            
            //drawing joystick and mouse positions
            g.drawString("Mouse: (" + xpos + ", " + ypos + ")", 5, 40);
            g.drawString("Joystick: (" + round(x,3) + ", " + round(y, 3) + ")", 5, 20);
            g.drawString("Trigger is " + (trigger?"on.":"off."), 5, 60);
            g.drawString("Joystick is " + (mouseClicked?"":"not ") + "locked", 5, 80);
            g.drawString("Button being pressed: " + ((button != -1) ? button : "None"), 5, 100);
            
            //box around cursor
            g.drawRect(xpos-20, ypos-20, 40, 40);
            
            //crosshair
            g.drawLine(xpos, ypos-20, xpos, ypos+20);
            g.drawLine(xpos-20, ypos, xpos+20, ypos);
        }
        
        public void determineMousePos(MouseEvent e) {
            if(!mouseClicked) {
                xpos = e.getX();
                ypos = e.getY();
                if (ypos > JSHEIGHT)
                    ypos = JSHEIGHT;
                x = (double)(xpos-JSHEIGHT/2.0)/(JSHEIGHT/2.0);
                y = (double)((getWidth()/2.0)-ypos)/(getWidth()/2.0);
            }
            repaint();
        }
        
        public double round(double preNum, int decPlaces) {
            return (double)Math.round((preNum*Math.pow(10, decPlaces)))/Math.pow(10, decPlaces);
        }

        public void mouseMoved(MouseEvent e) {
            determineMousePos(e);
        }
        
        public void mouseDragged(MouseEvent e) {
            determineMousePos(e);
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 1)
                mouseClicked = !mouseClicked;
            else if (e.getButton() == 3)
                trigger = true;
            repaint();
        }
        
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == 3) {
                trigger = false;
                repaint();
            }
        }
        
        public void mouseWheelMoved(MouseWheelEvent e) {
            zpos-=e.getWheelRotation()*10;
            if (zpos < 0)
                zpos = 0;
            else if (zpos > 460)
                zpos = 460;
            z = ((double)zpos/460*2)-1;
            repaint();
        }
        
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
        
    }
}
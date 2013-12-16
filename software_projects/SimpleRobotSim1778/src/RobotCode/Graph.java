package RobotCode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

/**
 * Handles points on a graph as well as provides interactive viewing of the graph.
 * 
 * @author Patrick Jameson
 * @version 11.20.2010.0
 */
@SuppressWarnings("serial")
public class Graph extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private int width, height, xAxisLoc, yAxisLoc, preX, preY, mouseX, mouseY;
	private int yStart, yEnd;
	private int scale;
	private double[][] points = {};
	private boolean restrictedRange;
	private double range1, range2;
	
	public Graph(int _width, int _height) {
		width = _width;
		height = _height;
		xAxisLoc = width/2;
		yAxisLoc = height/2;
		
		scale = 12;
		restrictedRange = false;
		
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	/**
	 * Draws the graph.
	 */
	public void paintComponent(Graphics g) {
		//clears graph.
		g.setColor(Color.gray);
		g.fillRect(0,0,width,height);
		
		//draws grid.
		g.setColor(new Color(230, 230, 230));//greyish
		if (restrictedRange) {
			yStart = (int)(yAxisLoc-(scale*range1));
			yEnd = (int)(yAxisLoc+(scale*range2));
		} else {
			yStart = yAxisLoc%scale;//to line up the x axis with the background grid.
			yEnd = height;
		}
		
		//clears graphing area.
		g.setColor(Color.white);
		g.fillRect(0, yStart, width, yEnd-yStart);
		
		g.setColor(new Color(230, 230, 230));//greyish
		for (int x = 0;x <= width;x+=scale)
			g.drawLine(x+xAxisLoc%scale, yStart, x+xAxisLoc%scale, yEnd);
		for (int y = yStart;y <= yEnd;y+=scale)
			g.drawLine(0, y, width, y);
		
		//draws x and y axis.
		g.setColor(Color.black);
		//g.drawLine(xAxisLoc, 0, xAxisLoc, height);//y axis.
		g.drawLine(0, yAxisLoc, width, yAxisLoc);//x axis.
		
		//draw points
		g.setColor(Color.red);
		for (int i = 0;i < points.length;i++) {
			int x = (int)(xAxisLoc+(points[i][0]*scale));
			int y = (int)(yAxisLoc-(points[i][1]*scale));
			//g.fillOval(x-scale/6, y-scale/6, scale/3, scale/3);//draws a dot at each point.
			if (i < points.length-1)
				g.drawLine(x, y, (int)(xAxisLoc+(points[i+1][0]*scale)), (int)(yAxisLoc-(points[i+1][1]*scale)));
		}
		
		//draws box next to mouse showing points.
		/*g.setColor(Color.black);
		double x = (xAxisLoc-mouseX)/(double)scale;
		double y = (yAxisLoc-mouseY)/(double)scale;
		g.drawString("("+round(x, 2) + ", ", mouseX, mouseY);
		g.drawString(round(y, 2)+")", mouseX+60, mouseY);*/
	}
	
	public double round(double preNum, int decPlaces) {
    	return (double)Math.round((preNum*Math.pow(10, decPlaces)))/Math.pow(10, decPlaces);
    }
	
	/**
	 * Sets the points to be graphed.
	 * 
	 * @param _points points to be graphed in the format of _points[point number][0 for x and 1 for y]
	 */
	public void setPoints(double[][] _points) {
		points = _points;
		repaint();
	}
	
	/**
	 * Gets the starting point for use in mouseDragged.
	 */
	public void mousePressed(MouseEvent e) {
		preX = e.getX();
		preY = e.getY();
	}
	
	/**
	 * Moves graph with the dragging of the mouse.
	 */
	public void mouseDragged(MouseEvent e) {
		moveGraph(e.getX() - preX, e.getY() - preY);
		preX = e.getX();
		preY = e.getY();
		/*mouseX = preX;
		mouseY = preY;*/
	}
	
	/**
	 * moves the graph by (mX, mY)
	 * @param mX move the x axis mX pixels
	 * @param mY move the y axis mY pixels
	 */
	public void moveGraph(int mX, int mY) {
		xAxisLoc += mX;
		yAxisLoc += mY;
		repaint();
	}
	
	/**
	 * Sets the view of the graph at width/2 based on the value of pX.
	 * @param pX value that will be shown in the position of width/2
	 */
	public void setXAxisPosition(double pX) {
		xAxisLoc = (int)(pX*scale)*-1+width/2;
		repaint();
	}
	
	/**
	 * Sets the view of the graph at width/2 based on the value of pY.
	 * @param pY value that will be shown in the position of width/2
	 */
	public void setYAxisPosition(double pY) {
		yAxisLoc = (int)(pY*scale)*-1+width/2;
		repaint();
	}
	
	/**
	 * TODO: Restricts the graph to the range provided.
	 * @param _range1
	 * @param _range2
	 */
	public void setRange(double _range1, double _range2) {
		restrictedRange = true;
		range1 = _range1;
		range2 = _range1;
	}
	
	/**
	 * Zooms the graph in and out when user rolls the mouse wheel relative to the position of the mouse. 
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		double preX = (xAxisLoc-e.getX())/(double)scale;
		double preY = (yAxisLoc-e.getY())/(double)scale;
		scale -= e.getWheelRotation()*3;
		if (scale < 3)
			scale = 3;
		else if (scale >= 60)
			scale = 60;
		
		xAxisLoc = (int)((preX*scale)+e.getX());
		yAxisLoc = (int)((preY*scale)+e.getY());
		repaint();
	}
	
	/**
	 * Sets the height and width of the graph.
	 * @param _width Desired width of graph.
	 * @param _height Desired height of graph.
	 */
	public void setGraphSize(int _width, int _height) {
		width = _width;
		height = _height;
	}
	
	/**
	 * @returns the current scale of the graph(zoom level)
	 */
	public int getScale() {
		return scale;
	}
	
	public void mouseMoved(MouseEvent e) {
		/*mouseX = e.getX();
		mouseY = e.getY();
		repaint();*/
	}
	
	//extra stuffs
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}

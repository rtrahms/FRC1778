package RobotCode;

/**
 * Handles a graphing environment for the speeds of speed controllers such as Victors and Jaguars.
 * 
 * @author Patrick Jameson
 * @version 11.20.2010.0
 */
@SuppressWarnings("serial")
public class SpeedGrapher extends Graph {
	private double[][] points;
	private double curTime;
	private int numPoints;
	/**
	 * Constructs a SpeedGrapher with desired dimensions.
	 * 
	 * @param _width Desired width of the graph.
	 * @param _height Desired height of the graph.
	 */
	public SpeedGrapher(int _width, int _height) {
		super(_width, _height);
		points = new double[1000][2];//1500 is the allocated amount of points.
		curTime = 0;
		numPoints = 0;
	}
	
	/**
	 * Appends a point on the graph with the speed provided.
	 * 
	 * TODO: make this more efficient.
	 * 
	 * @param speed Speed to be graphed.
	 */
	public void appendSpeed(double speed) {
		double[] addedPoints = {curTime, speed*10};
		if (numPoints < points.length-1) {
			numPoints++;
			points[numPoints] = addedPoints;
		} else {
			System.arraycopy(points, 1, points, 0, points.length-1);
			points[points.length-1] = addedPoints;
		}
		
		double[][] sendingPoints = new double[numPoints][2];
		System.arraycopy(points, 0, sendingPoints, 0, numPoints);
		
		setRange(10, -10);
		setPoints(sendingPoints);
		setXAxisPosition(curTime-(super.getWidth()/2/super.getScale())+(20/super.getScale()));
		
		curTime+=0.15;
		
	}
}

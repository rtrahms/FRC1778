package org.usfirst.frc.team1778.robot.camera;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Camera {
	public AxisCamera camera;
	public String address;
	public ColorImage lastImage;
	
	public final int LOW_FILTER_THRESHOLD[] = {};
	public final int HIGH_FILTER_THRESHOLD[] = {};
	
	public Camera(String address) {
		this.address = address;
		this.camera = new AxisCamera(address);
	}
	
	public ColorImage getImage() {
		ColorImage image = null;
		try {
			image = camera.getImage();
		} catch (NIVisionException e) {
			e.printStackTrace();
		}
		if(image != null) lastImage = image;
		return image;
	}
	
	public BinaryImage filterImage(ColorImage image) {
		int redLow = LOW_FILTER_THRESHOLD[0];
		int redHigh = HIGH_FILTER_THRESHOLD[0];
		int greenLow = LOW_FILTER_THRESHOLD[0];
		int greenHigh = HIGH_FILTER_THRESHOLD[0];
		int blueLow = LOW_FILTER_THRESHOLD[0];
		int blueHigh = HIGH_FILTER_THRESHOLD[0];
		try {
			return image.thresholdRGB(redLow, redHigh, greenLow, greenHigh, blueLow, blueHigh);
		} catch (NIVisionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}

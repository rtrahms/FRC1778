
package Systems;

import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.AxisCamera.*;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.*;

public class TargetFinder {
	
	private static final Resolution DEFAULT_RESOLUTION = Resolution.k320x240;
	private static final int DEFAULT_FPS = 24;
	private static final int DEFAULT_BRIGHTNESS = 50;
	private static final int DEFAULT_COMPRESSION = 30;
	private static final int DEFAULT_COLORLEVEL = 50;
	private static final WhiteBalance DEFAULT_WHITEBALANCE = WhiteBalance.kFixedIndoor;
	
	private static final int H_LOW = 0;
	private static final int S_LOW = 0;
	private static final int V_LOW = 0;
	private static final int H_HIGH = 0;
	private static final int S_HIGH = 0;
	private static final int V_HIGH = 0;
	
	private static final int W_MIN = 0;
	private static final int W_MAX = 0;
	private static final int H_MIN = 0;
	private static final int H_MAX = 0;
	private static final int A_MIN = 0;
	//private static final int A_MAX;
	private static final double R_MIN = W_MIN/H_MAX;
	private static final double R_MAX = W_MAX/H_MIN;
	
	private static final boolean CONNECTIVITY8 = true;
	private static final int EROSIONS = 1;
	private static final int TARGET_WIDTH = 20; // Inches
	private static final int TARGET_HEIGHT = 14; // Inches
	private static final double FOCAL_LENGTH = 0.0;	// F=(P*D)/TARGET_WIDTH
	
	private AxisCamera camera;
	private ColorImage image;
	private String address;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public void initCamera(String address) {
		if (this.address != address)
			this.camera = new AxisCamera(address);
		
		this.address = address;
		
		camera.writeResolution(DEFAULT_RESOLUTION);
		camera.writeMaxFPS(DEFAULT_FPS);
		camera.writeBrightness(DEFAULT_BRIGHTNESS);
		camera.writeCompression(DEFAULT_COMPRESSION);
		camera.writeColorLevel(DEFAULT_COLORLEVEL);
		camera.writeWhiteBalance(DEFAULT_WHITEBALANCE);
	}
	
	public boolean updateImage() {
		if (camera != null && camera.isFreshImage()) {
			return camera.getImage(this.image);
		}
		System.out.println("Camera not initialized");
		return false;
	}
	
	public ColorImage getImage() {
		return this.image;
	}

	public Resolution getResolution() {
		if(camera != null)
			return camera.getResolution();
		System.out.println("Camera not initialized");
		return DEFAULT_RESOLUTION;
	}
	
	private BinaryImage filterHSV(ColorImage image) throws NIVisionException {
		return image.thresholdHSV(H_LOW, H_HIGH, S_LOW, S_HIGH, V_LOW, V_HIGH);
	}
	
	private BinaryImage erode(BinaryImage image, int erosions) throws NIVisionException {
		return image.removeSmallObjects(CONNECTIVITY8, erosions);
	}

	private BinaryImage convexHull(BinaryImage image) throws NIVisionException {
		return image.convexHull(CONNECTIVITY8);
	}
	
	private ParticleAnalysisReport[] getReports(BinaryImage image) throws NIVisionException {
		return image.getOrderedParticleAnalysisReports();
	}
	
	private boolean hasTarget(ParticleAnalysisReport[] reports) {
		if(reports.length > 0) {
			ParticleAnalysisReport r = reports[0];
			double ratio = ((double)r.boundingRectWidth)/((double)r.boundingRectHeight);
			if(/*ratio > R_MIN && ratio < R_MAX &&*/ r.particleArea > A_MIN) {
				return true;
			}
		}
		return false;
	}
	
	public ParticleAnalysisReport getTarget(ColorImage image) throws NIVisionException {
		ParticleAnalysisReport[] reports = getReports(convexHull(erode(filterHSV(image),EROSIONS)));
		if(hasTarget(reports)) {
			return reports[0];
		} else {
			return null;
		}
	}
	
	public double getTargetDistance(ParticleAnalysisReport target) {
		return TARGET_WIDTH*FOCAL_LENGTH/target.boundingRectWidth;
	}

	public void initDefaultCommand() {
		//setDefaultCommand(new UpdateCamera());
	}
}

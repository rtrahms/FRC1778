
package Systems;

import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.AxisCamera.*;
import edu.wpi.first.wpilibj.image.*;

public class TargetFinder {
	
	private static final Resolution DEFAULT_RESOLUTION = Resolution.k320x240;
	private static final int DEFAULT_FPS = 24;
	private static final int DEFAULT_BRIGHTNESS = 50;
	private static final int DEFAULT_COMPRESSION = 30;
	private static final int DEFAULT_COLORLEVEL = 50;
	private static final WhiteBalance DEFAULT_WHITEBALANCE = WhiteBalance.kFixedIndoor;
	
	private static final int H_LOW = 37;
	private static final int S_LOW = 32;
	private static final int V_LOW = 117;
	private static final int H_HIGH = 89;
	private static final int S_HIGH = 144;
	private static final int V_HIGH = 233;
	
	private static final int W_MIN = 0;
	private static final int W_MAX = 0;
	private static final int H_MIN = 0;
	private static final int H_MAX = 0;
	private static final int A_MIN = 0;
	//private static final int A_MAX;
	//private static final double R_MIN = W_MIN/H_MAX;
	//private static final double R_MAX = W_MAX/H_MIN;
	
	private static final boolean CONNECTIVITY8 = true;
	private static final int EROSIONS = 1;
	private static final int TARGET_WIDTH = 20; // Inches
	private static final int TARGET_HEIGHT = 14; // Inches
	private static final double FOCAL_LENGTH = 0.0;	// F=(P*D)/TARGET_WIDTH
	
	private static AxisCamera camera;
	private static ColorImage image;
	private static BinaryImage binaryImage;
	private static String address;
	private static boolean hasTarget;
	
	
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public static void initCamera(String address) {
		camera = new AxisCamera(address);
		try {
			image = new HSLImage();
			binaryImage = new BinaryImage();
		} catch (NIVisionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//address = address;
		/*
		camera.writeResolution(DEFAULT_RESOLUTION);
		camera.writeMaxFPS(DEFAULT_FPS);
		camera.writeBrightness(DEFAULT_BRIGHTNESS);
		camera.writeCompression(DEFAULT_COMPRESSION);
		camera.writeColorLevel(DEFAULT_COLORLEVEL);
		camera.writeWhiteBalance(DEFAULT_WHITEBALANCE);
		*/
	}
	
	public static boolean updateImage() {
		if (camera != null && camera.isFreshImage()) {
			return camera.getImage(image);
		}
		System.out.println("Camera not initialized");
		return false;
	}
	
	public static ColorImage getImage() {
		return image;
	}

	public static Resolution getResolution() {
		if(camera != null)
			return camera.getResolution();
		System.out.println("Camera not initialized");
		return DEFAULT_RESOLUTION;
	}
	
	private static BinaryImage filterHSV(ColorImage image) throws NIVisionException {
		return image.thresholdHSV(H_LOW, H_HIGH, S_LOW, S_HIGH, V_LOW, V_HIGH);
	}
	
	private static BinaryImage erode(BinaryImage image, int erosions) throws NIVisionException {
		return image.removeSmallObjects(CONNECTIVITY8, erosions);
	}

	private static BinaryImage convexHull(BinaryImage image) throws NIVisionException {
		return image.convexHull(CONNECTIVITY8);
	}
	
	private static ParticleAnalysisReport[] getReports(BinaryImage image) throws NIVisionException {
		return image.getOrderedParticleAnalysisReports();
	}
	
	private static boolean hasTarget(ParticleAnalysisReport[] reports) {
		if(reports.length > 0) {
			ParticleAnalysisReport r = reports[0];
			double ratio = ((double)r.boundingRectWidth)/((double)r.boundingRectHeight);
			if(/*ratio > R_MIN && ratio < R_MAX &&*/ r.particleArea > A_MIN) {
				return true;
			}
		}
		return false;
	}
	
	public static ParticleAnalysisReport getTarget(ColorImage image) throws NIVisionException {
		binaryImage = filterHSV(image);
		binaryImage = erode(binaryImage,EROSIONS);
		binaryImage = convexHull(binaryImage);
		ParticleAnalysisReport[] reports = getReports(binaryImage);
		if(hasTarget(reports)) {
			hasTarget = true;
			return reports[0];
		} else {
			hasTarget = false;
			return null;
		}
	}
	
	public boolean seenTarget() {
		return this.hasTarget;
	}
	
	public static double getNormX(ParticleAnalysisReport target) {
		if(target == null) return 0.0;
		return target.center_mass_x_normalized;
	}
	
	public static double getArea(ParticleAnalysisReport target) {
		if(target == null) return 0.0;
		return target.particleArea;
	}
	
	public static int getHeight(ParticleAnalysisReport target) {
		if(target == null) return 0;
		return target.boundingRectHeight;
	}
	
	public static int getWidth(ParticleAnalysisReport target) {
		if(target == null) return 0;
		return target.boundingRectWidth;
	}
	
	public static double getTargetDistance(ParticleAnalysisReport target) {
		if(target == null) return 0.0;
		return TARGET_WIDTH*FOCAL_LENGTH/target.boundingRectWidth;
	}

	public static void initDefaultCommand() {
		//setDefaultCommand(new UpdateCamera());
	}
}

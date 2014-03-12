package frc1778;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera1778 {

    private AxisCamera camera;          // the axis camera object (connected to the switch)
    private CriteriaCollection cc;      // the criteria for doing the particle filter operation
    private boolean hasTarget;
    private boolean isRobotLeft;
    private ParticleAnalysisReport[] reports;
    
    public Camera1778() {
        camera = AxisCamera.getInstance();  // get an instance ofthe camera
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 2, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 2, 400, false);
        
        hasTarget = false;
        isRobotLeft = false;
    }
    
    public boolean hasTarget() {
        
        //this.runCam();
        //return hasTarget;
        return false;
    }
    
    public void setLeft(boolean isRobotLeft)
    {
        this.isRobotLeft = isRobotLeft;
    }
    
    public void runCam() {
        int Red_min = 0;
        int Green_min = 190;
        int Blue_min = 153;
        int Red_max = 110;
        int Green_max = 250;
        int Blue_max = 203;

        try {
            ColorImage image = camera.getImage();
            BinaryImage thresholdImage = image.thresholdRGB(Red_min, Red_max, Green_min, Green_max, Blue_min, Blue_max);   // keep only green objects
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles
            reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
            
            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());
            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();

       } catch (AxisCameraException ex) {
           ex.printStackTrace();

       } catch (NIVisionException ex) {
            ex.printStackTrace();
       }
    }
    
    public double getX() {
        double total = 0;
        for (int i = 0; i < reports.length; i++) {                                // print results
            ParticleAnalysisReport r = reports[i];
            total += r.center_mass_x_normalized;
            SmartDashboard.putNumber("CameraX", r.center_mass_x_normalized);
        }
        return total/reports.length;
    }
    
    public double getY() {
        double total = 0;
        for (int i = 0; i < reports.length; i++) {                                // print results
            ParticleAnalysisReport r = reports[i];
            total += r.center_mass_y_normalized;
            SmartDashboard.putNumber("CameraY", r.center_mass_y_normalized);
        }
        return total/reports.length;
    }
}
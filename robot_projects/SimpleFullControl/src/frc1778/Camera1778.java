/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc1778;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author Rob
 */
public class Camera1778 implements PIDSource {
 
    // final String CAMERA_IP_ADDR = "10.17.78.11";
    final String CAMERA_IP_ADDR = "10.04.02.11";
    final double CAM_FOV_ANGLE = 67.0/2.0;  // M1013 camera FOV/2
    
    private AxisCamera camera;
    private ParticleAnalysisReport[] reports;
    private ParticleAnalysisReport highestReport;
    private ParticleAnalysisReport lowestReport;
    public boolean beginCalc = false;
    private boolean checkSize = false;
    public double lastImageTime = 0;
    public boolean searchHigh = false;

    public Camera1778() {
        camera = AxisCamera.getInstance(CAMERA_IP_ADDR);
        camera.writeResolution(AxisCamera.ResolutionT.k160x120);
        camera.writeCompression(60);
        camera.writeMaxFPS(15);
    }
    
    public void getImage() throws AxisCameraException, NIVisionException {
        if (camera.freshImage()) {
            ColorImage image = null;
            try {
                image = camera.getImage();
            } catch (AxisCameraException e) {
                e.printStackTrace();
            } catch (NIVisionException e) {
                e.printStackTrace();
            }
            
            BinaryImage thresholdImage = image.thresholdHSI(108, 141, 219, 255, 125, 255);  // keep bright green
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(true, 2);
            BinaryImage convexHullImage = bigObjectsImage.convexHull(true);
            
            reports = convexHullImage.getOrderedParticleAnalysisReports();  // get list of results
            int lowestIndex = 0;
            int highestIndex = 0;
            
            // pre sort setting
            if (reports.length > 0) {
                lowestReport = reports[0];
                highestReport = reports[0];
            }
            if (reports.length > 4) {
                checkSize = true;
            }
            
            // sort by report size
            for (int i=0; i < reports.length; i++)
            {
                ParticleAnalysisReport r = reports[i];
                if (r.center_mass_y > reports[lowestIndex].center_mass_y) {
                    lowestIndex = i;
                    lowestReport = r;
                }
                if (r.center_mass_y < reports[highestIndex].center_mass_y) {
                    highestIndex = i;
                    highestReport = r;
                }
            }
            
            // timestamp for the image
            lastImageTime = Timer.getFPGATimestamp();
            beginCalc = true;  // ready to process images
            
            // Save images (optional)
            System.out.println("Saving images");
            image.write("../../images/orig" + Timer.getFPGATimestamp() + ".jpg");
            convexHullImage.write("../../images/cvhi" + Timer.getFPGATimestamp() + ".jpg");
            
            // free memory for images
            image.free();
            thresholdImage.free();
            bigObjectsImage.free();
            convexHullImage.free();
        }
    }
    
    public int getTargetCenterX() {
     
        if (searchHigh) {
            if (highestReport != null) {
                return highestReport.center_mass_x;
            } else {
                return camera.getResolution().width/2;
            }
         } else {
            if (lowestReport != null) {
                return lowestReport.center_mass_x;
            } else {
                return camera.getResolution().width/2;
            }
        }
    }  
    
    public int getTargetCenterY() {
     
        if (searchHigh) {
            if (highestReport != null) {
                return highestReport.center_mass_y;
            } else {
                return camera.getResolution().height/2;
            }
         } else {
            if (lowestReport != null) {
                return lowestReport.center_mass_y;
            } else {
                return camera.getResolution().height/2;
            }
        }
    }
    
    /*
     * Return the width of the target, in pixels
     */
    public int getTargetWidth(){
        if(searchHigh){
                if(highestReport != null){
                        return highestReport.boundingRectWidth;
                }else{
                        return 10;
                }
        }else{
                if(lowestReport != null){
                        return lowestReport.boundingRectWidth;
                }else{
                        return 10; 
                }
        }
    }
    
    /*
     * Return the height of the target, in pixels
     */
    public int getTargetHeight(){
        if(searchHigh){
                if(highestReport != null){
                        return highestReport.boundingRectHeight;
                }else{
                        return 10;
                }
        }else{
                if(lowestReport != null){
                        return lowestReport.boundingRectHeight;
                }else{
                        return 10; 
                }
        }
    }
    
    /*
     * Return the area of the target, in pixels
     */
    public double getTargetArea(){
        if(searchHigh){
                if(highestReport != null){
                        return highestReport.particleArea;
                }else{
                        return 10;
                }
        }else{
                if(lowestReport != null){
                        return lowestReport.particleArea;
                }else{
                        return 10; 
                }
        }
    }
    
    /*
     * Return the distance to the center of the target, in cm.
     */
    public double getDistance(){
        ParticleAnalysisReport report = null;
        if(searchHigh){
            report = highestReport;
        }else{
            report = lowestReport;
        }
        if(beginCalc && report != null){
            double targetHeightMeters=.4572;                                            //18in in meters
            double targetHeightPixels=lowestReport.boundingRectHeight;
            double fieldHeightMeters;                                                   //Determined with mathematics
            double fieldHeightPixels= camera.getResolution().height;
            double theta = Math.toRadians(CAM_FOV_ANGLE);                     //Axis M1013 angle/2
            double distance;

            fieldHeightMeters = (targetHeightMeters * fieldHeightPixels)/targetHeightPixels;

            fieldHeightMeters /= 2;         //Half the height, for right triangle

            distance = (fieldHeightMeters)/Math.tan(theta);

            //TODO: Check for an equation
            double fixedDistance = distance;
            fixedDistance *= 100; //to centimeters
            return fixedDistance;
        }else{
            return 0;
        }
    }
    
    /*
     * Return the required angle to turn to be centered toward the target
     * in degrees
     */
    public double getHeading(){
        double d = getDistance();   //m
        double x = ((getTargetCenterX() + (getTargetHeight()/.4572)*.245) -320) *.55555;
        if(d != 0){
            return MathUtils.atan(x/d) * 57.2957795;    //to degrees
        }else{
            return 0;
        }
    }
    
    /*
     * Return the current resolution setting of the camera.
     */
    public AxisCamera.ResolutionT getResolution(){
        return camera.getResolution();
    }
     
    /*
     * Return the center pixel, for use in pid.
     */
    public double pidGet() {
        double x = getHeading();
        return x;
    }
    
}

package org.usfirst.frc.team1778.robot;
import Utility.SimpleUtil;

public class DriveControl {
	
	double oldWheel, quickStopAccumulator;
    private double throttleDeadband = 0.02;
    private double wheelDeadband = 0.02;
	
	public DriveControl(){
		
	}
	
	public void calculateDrive(double throttle, double wheel, boolean isQuickTurn,boolean isHighGear){
		
		
		
		double wheelNonLinearity;
		wheel = handleDeadband(wheel, wheelDeadband);
        throttle = handleDeadband(throttle, throttleDeadband);
		
		
		double negInertia = wheel - oldWheel;
        oldWheel = wheel;
        
        if (isHighGear) {
            wheelNonLinearity = 0.6;
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        } else {
            wheelNonLinearity = 0.5;
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        }
		
        double sensitivity;
        double angularPower;
        double linearPower;
        
        double negInertiaAccumulator = 0.0;
        double negInertiaScalar;
        
        
        if (isHighGear) {
            negInertiaScalar = 4.0;
            sensitivity =  .5; // what is the sensitivity
        } else {
            if (wheel * negInertia > 0) {
                negInertiaScalar = 2.5;
            } else {
                if (Math.abs(wheel) > 0.65) {
                    negInertiaScalar = 5.0;
                } else {
                    negInertiaScalar = 3.0;
                }
            }
            sensitivity = .85; // Constants.sensitivityLow.getDouble();
        }
	
        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;

        wheel = wheel + negInertiaAccumulator;
        if (negInertiaAccumulator > 1) {
            negInertiaAccumulator -= 1;
        } else if (negInertiaAccumulator < -1) {
            negInertiaAccumulator += 1;
        } else {
            negInertiaAccumulator = 0;
        }
        linearPower = throttle;
        
        double rightPower,leftPower,overPower;
        
        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator
                        + alpha * 5 * -1 * SimpleUtil.limit(true, wheel, 1);
            }
            overPower = 1.0;
            if (isHighGear) {
                sensitivity = 1.0;
            } else {
                sensitivity = 1.0;
            }
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity
                    - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }
        
        
        
        
        
        
        overPower = 0.0;
        angularPower = Math.abs(throttle) * wheel * sensitivity
                - quickStopAccumulator;
        
        rightPower = leftPower = linearPower;
        leftPower += angularPower;
        rightPower -= angularPower;

        if (leftPower > 1.0) {
            rightPower -= overPower * (leftPower - 1.0);
            leftPower = 1.0;
        } else if (rightPower > 1.0) {
            leftPower -= overPower * (rightPower - 1.0);
            rightPower = 1.0;
        } else if (leftPower < -1.0) {
            rightPower += overPower * (-1.0 - leftPower);
            leftPower = -1.0;
        } else if (rightPower < -1.0) {
            leftPower += overPower * (-1.0 - rightPower);
            rightPower = -1.0;
        }	
        
        
		
		// sets the final values for motor speed.
		Systems.DriveTrain.ChangeSpeed(-leftPower,rightPower);
		
	}
	
	public double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
	
}

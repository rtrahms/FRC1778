package org.usfirst.frc.team1778.robot.camera;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class TargetReport {

	public final static boolean USE_CONNECTIVITY8 = false;
	public final static int ERROSIONS = 4;
	public BinaryImage image;
	public int particleCount, width, height;
	public double normalizedCenterOfMassX, normalizedCenterOfMassY;
	public ParticleAnalysisReport[] particles;
	
	public TargetReport(BinaryImage image) {
		this.image = image;
		try {
			this.width = image.getWidth();
			this.height = image.getHeight();
			this.image.convexHull(USE_CONNECTIVITY8);
			this.image.removeSmallObjects(USE_CONNECTIVITY8, ERROSIONS);
			this.particleCount = this.image.getNumberParticles();
			this.particles = this.image.getOrderedParticleAnalysisReports();
		} catch (NIVisionException e) {
			e.printStackTrace();
		}
		this.normalizedCenterOfMassX = this.getTotalNormalizedCenterOfMassX();
		this.normalizedCenterOfMassY = this.getTotalNormalizedCenterOfMassY();
	}
	
	public double getTotalNormalizedCenterOfMassX() {
		double centerOfMass = 0;
		for(int n = 0; n < particles.length; n++) {
			ParticleAnalysisReport particle = getParticle(n);
			centerOfMass += particle.center_mass_x_normalized;
		}
		centerOfMass /= particles.length;
		return centerOfMass;
	}
	
	public double getTotalNormalizedCenterOfMassY() {
		double centerOfMass = 0;
		for(int n = 0; n < particles.length; n++) {
			ParticleAnalysisReport particle = getParticle(n);
			centerOfMass += particle.center_mass_y_normalized;
		}
		centerOfMass /= particles.length;
		return centerOfMass;
	}
	
	public ParticleAnalysisReport getParticle(int index) {
		return particles[index];
	}
	
	/*
	public void removeSmallParticles(int errosion) {
		try {
			
		} catch (NIVisionException e) {
			e.printStackTrace();
		}
	}
	*/
}

package edu.wpi.first.wpilibj;

/**
 * User: IisMathwizard
 * Date: 10/10/13
 * Time: 12:36 AM
 */
public interface MotorSafety
{
    public static final double DEFAULT_SAFETY_EXPIRATION = 0.1;

    void setExpiration(double timeout);
    double getExpiration();
    boolean isAlive();
    void stopMotor();
    void setSafetyEnabled(boolean enabled);
    boolean isSafetyEnabled();
    String getDescription();
}

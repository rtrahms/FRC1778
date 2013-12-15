package edu.wpi.first.wpilibj;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 11:18 AM
 */
public interface SpeedController extends PIDOutput
{
    void disable();
    double get();
    void set(double speed);
    void set(double speed, byte syncGroup);
}

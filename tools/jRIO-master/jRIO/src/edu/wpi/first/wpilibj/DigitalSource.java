package edu.wpi.first.wpilibj;

/**
 * User: IisMathwizard
 * Date: 10/10/13
 * Time: 11:16 AM
 */
public abstract class DigitalSource extends InterruptableSensorBase
{
    public DigitalSource()
    {

    }

    public abstract int getModuleForRouting();
    public abstract int getChannelForRouting();
}

package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.parsing.IDeviceController;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 11:21 AM
 */
public class Jaguar extends SafePWM implements SpeedController, IDeviceController
{
    public Jaguar(int channel)
    {
        super(channel);
    }

    public Jaguar(int slot, int channel)
    {
        super(slot, channel);
    }

    @Override
    public double get()
    {
        return getSpeed();
    }

    @Override
    public void set(double speed)
    {
        setPosition((speed + 1.0) / 2.0);
    }

    @Override
    @Deprecated
    public void set(double speed, byte syncGroup)
    {
        set(speed);
    }

    @Override
    public void pidWrite(double output)
    {
        set(output);
    }
}

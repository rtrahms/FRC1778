package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import com.iismathwizard.jrio.jRIO;

/**
 * User: IisMathwizard
 * Date: 10/10/13
 * Time: 12:39 AM
 */
public class SafePWM extends PWM implements MotorSafety
{
    protected double _timeout = MotorSafety.DEFAULT_SAFETY_EXPIRATION;
    protected boolean _safety = true;
    protected boolean _isAlive = true;

    SafePWM(int channel)
    {
        super(channel);
    }

    SafePWM(int slot, int channel)
    {
        channel --;
        try
        {
            if(jRIO.getModule(slot) instanceof DigitalModule)
            {
                DigitalModule module = (DigitalModule) jRIO.getModule(slot);
                if(channel < module.pwms.length
                        && channel >= 0
                        && module.pwms[channel]  == null)
                {
                    module.pwms[channel] = this;
                    this.channel = channel;
                    this.moduleNumber = module.getModuleNumber();
                }
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("Digital Module was not found at slot " + slot);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void setExpiration(double timeout)
    {
        this._timeout = timeout;
    }

    @Override
    public double getExpiration()
    {
        return this._timeout;
    }

    @Override
    public boolean isAlive()
    {
        return _isAlive;
    }

    @Override
    public void stopMotor()
    {
        this.setRaw(0); //disables
    }

    @Override
    public void setSafetyEnabled(boolean enabled)
    {
        _safety = enabled;
    }

    @Override
    public boolean isSafetyEnabled()
    {
        return _safety;
    }

    @Override
    public String getDescription()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void disable()
    {
        stopMotor();
        _isAlive = false;
    }
}

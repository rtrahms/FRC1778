package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import com.iismathwizard.jrio.jRIO;
import edu.wpi.first.wpilibj.communication.ModulePresence;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 11:32 AM
 */
public class PWM extends SensorBase implements LiveWindowSendable
{
    protected int channel, moduleNumber = 1; //channel is 0 -> (channel_max - 1)

    // settings
    private final int MAX = 255;
    private final int MIN = 0;

    private int max = 255;
    private int deadbandMax = 128;
    private int center = 128;
    private int deadbandMin = 128;
    private int min = 1;

    private int value = center; //default to center

    private boolean deadbandEnabled = false;
    //end settings

    protected static final int kDefaultMinPwmHigh = 102;
    protected static final int kDefaultPwmPeriod = 774;
    public static final int kPwmDisabled = 0;

    PWM(int channel)
    {
        channel --;
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, 1)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, 1) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, 1);
                if(dMod.pwms[channel] == null)
                {
                    dMod.pwms[channel] = this;
                }
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("Default digital module couldn't be found.");
            e.printStackTrace();
        }

        this.channel = channel;
        this.moduleNumber = 1;
    }

    PWM(int moduleNumber, int channel)
    {
        channel --;
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, moduleNumber)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber);
                if(dMod.pwms[channel] == null)
                {
                    dMod.pwms[channel] = this;
                }
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("The Digital module couldn't be found.");
            e.printStackTrace();
        }

        this.channel = channel;
        this.moduleNumber = moduleNumber;
    }

    PWM(){};

    public void free()
    {
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, moduleNumber)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber);
                dMod.pwms[channel] = null;
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("The Digital module couldn't be found.");
            System.out.println("The PWM couldn't be freed.");
            e.printStackTrace();
        }
    }

    public void enableDeadbandElimination(boolean eliminateDeadband)
    {
        this.deadbandEnabled = eliminateDeadband;
    }

    public void setBounds(int max, int deadbandMax, int center, int deadbandMin, int min)
    {
        this.max = max;
        this.deadbandMax = deadbandMax;
        this.center = center;
        this.deadbandMin = deadbandMin;
        this.min = min;
    }

    public int getModuleNumber()
    {
        return moduleNumber;
    }

    public int getChannel()
    {
        return channel;
    }

    public void setPosition(double pos)
    {
        setRaw((int)Math.round(255 * pos));
    }

    public double getPosition()
    {
        int temp = getRaw();

        return ((double)temp / MAX);
    }

    public double getSpeed()
    {
        return (((2.0 * getRaw()) / MAX) - 1);
    }

    public void setRaw(int value)
    {
        if(value > MAX)
        {
            value = MAX;
        }
        if(value < MIN)
        {
            value = MIN;
        }

        this.value = value;
    }

    public int getRaw()
    {
        int temp = value;

        if(temp > max)
        {
            temp = max;
        }

        if(temp < min)
        {
            temp = min;
        }

        if(temp == kPwmDisabled)
        {
            return temp; //short circuit when we are disabled
        }

        if(temp < deadbandMax && temp > deadbandMin && deadbandEnabled)
        {
            temp = center;
        }

        if(temp > MAX)
        {
            temp = MAX;
        }

        if(temp < MIN)
        {
            temp = MIN;
        }

        return temp;
    }
}

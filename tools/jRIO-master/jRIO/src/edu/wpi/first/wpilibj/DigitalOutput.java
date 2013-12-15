package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import com.iismathwizard.jrio.jRIO;
import edu.wpi.first.wpilibj.communication.ModulePresence;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.parsing.IInputOutput;

/**
 * User: IisMathwizard
 * Date: 10/10/13
 * Time: 11:19 AM
 */
public class DigitalOutput extends DigitalSource
        implements IInputOutput, LiveWindowSendable
{
    private boolean value;
    private int moduleNumber, channel;

    public DigitalOutput(int channel)
    {
        this(ModulePresence.ModuleType.kDigital.getValue(), channel);
    }

    public DigitalOutput(int moduleNumber, int channel)
    {
        channel --;
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, moduleNumber)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber);
                if(dMod.slots[channel] == null)
                {
                    dMod.slots[channel] = this;
                }
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("Default digital module couldn't be found.");
            e.printStackTrace();
        }

        this.channel = channel;
        this.moduleNumber = moduleNumber;
    }

    @Override
    public int getModuleForRouting()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getChannelForRouting()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void set(boolean value)
    {
        this.value = value;
    }

    /**
     * IGNORE THIS METHOD; FOR jRIO USE ONLY
     * @return the boolean value
     */
    public boolean $_get()
    {
        return value;
    }
}

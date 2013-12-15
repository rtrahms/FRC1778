package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import com.iismathwizard.jrio.jRIO;
import edu.wpi.first.wpilibj.communication.ModulePresence;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.parsing.IInputOutput;

/**
 * User: IisMathwizard
 * Date: 10/10/13
 * Time: 11:18 AM
 */
public class DigitalInput extends DigitalSource
        implements IInputOutput, LiveWindowSendable
{
    private boolean value;
    private int moduleNumber, channel;

    public DigitalInput(int channel)
    {
        this(1, channel);
    }

    public DigitalInput(int moduleNumber, int channel)
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

    public boolean get()
    {
        return value;
    }

    /**
     * IGNORE THIS METHOD; FOR jRIO USE ONLY
     * @param value value to set
     */
    protected void $_setValue(boolean value)
    {
        this.value = value;
    }
}

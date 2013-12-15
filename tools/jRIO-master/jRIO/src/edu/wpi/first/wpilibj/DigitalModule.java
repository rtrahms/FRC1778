package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import com.iismathwizard.jrio.jRIO;
import edu.wpi.first.wpilibj.communication.ModulePresence;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 10:05 PM
 */
public class DigitalModule extends Module
{
    protected PWM[] pwms; //for digital slot management
    protected Relay[] relays; // for digital slot management

    static int kExpectedLoopTiming = 260;

    protected DigitalModule(int moduleNumber)
    {
        super(ModulePresence.ModuleType.kDigital, moduleNumber);
        pwms = new PWM[SensorBase.kPwmChannels];
        relays = new Relay[SensorBase.kRelayChannels];
    }

    /**
     * (SINGLETON FACTORY) Gets the module at given location else creates a new one to return
     * @param moduleNumber the module location to either get or create the module
     * @return a if not the module at the given moduleNumber
     */
    public static DigitalModule getInstance(int moduleNumber)
    {
        try
        {
            if(jRIO.hasModule(moduleNumber)
                    && jRIO.getModule(moduleNumber).getModuleType().getValue() == ModulePresence.ModuleType.kDigital.getValue()
                    && jRIO.getModule(moduleNumber) instanceof DigitalModule)
            {
                return (DigitalModule) jRIO.getModule(moduleNumber);
            }
            else
            {
                jRIO.addModule(new DigitalModule(moduleNumber), moduleNumber);
                return (DigitalModule)jRIO.getModule(moduleNumber);
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("Digital Module doesn't exist at: " + moduleNumber + " ... creating module instance.");
            return new DigitalModule(moduleNumber);
        }
    }

    public static int remapDigitalChannel(int channel)
    {
        return channel;
    }

    public static int unmapDigitalChannel(int channel)
    {
        return channel;
    }

    public void setPWM(int channel, int value)
    {
        if(pwms[channel] != null)
        {
            pwms[channel].setRaw(value);
        }
    }

    public int getPWM(int channel)
    {
        if(pwms[channel] != null)
        {
            return pwms[channel].getRaw();
        }
        return -1;
    }

    public void setRelayForward(int channel, boolean on)
    {
        if(relays[channel] != null && relays[channel]._direction.value == Relay.Direction.kForward.value)
        {
            relays[channel].set(on ? Relay.Value.kForward : Relay.Value.kReverse);
        }
    }

    public void setRelayReverse(int channel, boolean on)
    {
        if(relays[channel] != null && relays[channel]._direction.value == Relay.Direction.kReverse.value)
        {
            relays[channel].set(on ? Relay.Value.kReverse : Relay.Value.kForward);
        }
    }

    public boolean getRelayForward(int channel)
    {
        if(relays[channel] != null && relays[channel]._direction.value == Relay.Direction.kForward.value || relays[channel]._direction.value == Relay.Direction.kBoth.value)
        {
            return relays[channel].get().value == Relay.Value.kForward.value || relays[channel].get().value == Relay.Value.kOn.value;
        }
        return false;
    }

    public byte getRelayForward()
    {
        int result = 0;
        for(int count = 0; count < relays.length && count < 8; count ++)
        {
            if(getRelayForward(count))
            {
                result += Math.pow(2, count);
            }
        }
        return (byte)result;
    }

    public boolean getRelayReverse(int channel)
    {
        if(relays[channel] != null && relays[channel]._direction.value == Relay.Direction.kReverse.value || relays[channel]._direction.value == Relay.Direction.kBoth.value)
        {
            return relays[channel].get().value == Relay.Value.kReverse.value || relays[channel].get().value == Relay.Value.kOn.value;
        }
        return false;
    }

    public byte getRelayReverse()
    {
        int result = 0;
        for(int count = 0; count < relays.length && count < 8; count ++)
        {
            if(getRelayReverse(count))
            {
                result += Math.pow(2, count);
            }
        }
        return (byte)result;
    }

    /**
     * Allocate Digital I/O channels. Allocate channels so that they are not accidentally reused. Also the direction is set at the time of the allocation.
     * @param channel The channel to allocate.
     * @param input Indicates whether the I/O pin is an input (true) or an output (false).
     * @return True if the I/O pin was allocated, false otherwise.
     */
    public boolean allocateDIO(int channel, boolean input)
    {
        if(channel >= slots.length || channel < 0)
        {
            throw new RuntimeException("IO Channel doesn't exists: " + channel + " on DigitalModule: " + m_moduleNumber);
        }
        if(slots[channel] == null)
        {
            if(input)
            {
                new DigitalInput(m_moduleNumber, channel);
            }
            else
            {
                new DigitalOutput(m_moduleNumber, channel);
            }

            return true;
        }

        return false;
    }

    public void freeDIO(int channel)
    {
        if(channel >= slots.length || channel < 0)
        {
            throw new RuntimeException("IO Channel doesn't exists: " + channel + " on DigitalModule: " + m_moduleNumber);
        }
        slots[channel] = null;
    }
}

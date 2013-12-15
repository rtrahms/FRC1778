package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import com.iismathwizard.jrio.jRIO;
import edu.wpi.first.wpilibj.communication.ModulePresence;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.parsing.IDeviceController;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 10:54 PM
 */
public class Relay extends SensorBase
        implements IDeviceController, LiveWindowSendable
{
    private int channel, moduleNumber = 1;

    Direction _direction;
    private Value _value = Value.kOff;

    public static class Direction
    {
        public static final Direction kBoth = new Direction(2);
        public static final Direction kForward = new Direction(1);
        public static final Direction kReverse = new Direction(0);

        public final int value;

        private Direction(int value)
        {
            this.value = value;
        }

    }

    public class InvalidValueException extends RuntimeException
    {
        public InvalidValueException(String message)
        {
            super(message);
        }
    }

    public static class Value
    {
        public static final Value kForward = new Value(1);
        public static final Value kReverse = new Value(-1);
        public static final Value kOn = new Value(2);
        public static final Value kOff = new Value(0);

        public final int value;

        private Value(int value)
        {
            this.value = value;
        }

    }

    public Relay(int channel)
    {
        channel --;
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, 1)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, 1) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, 1);
                if(dMod.relays[channel] == null)
                {
                    dMod.relays[channel] = this;
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

        setDirection(Direction.kBoth);
    }

    public Relay(int moduleNumber, int channel)
    {
        channel --;
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, moduleNumber)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber);
                if(dMod.relays[channel] == null)
                {
                    dMod.relays[channel] = this;
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

        setDirection(Direction.kBoth);
    }

    public Relay(int channel, Direction direction)
    {
        this(channel);
        setDirection(direction);
    }

    public Relay(int moduleNumber, int channel, Direction direction)
    {
        this(moduleNumber, channel);
        setDirection(direction);
    }

    public void free()
    {
        try
        {
            if(jRIO.hasModule(ModulePresence.ModuleType.kDigital, moduleNumber)
                    && jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber) instanceof DigitalModule)
            {
                DigitalModule dMod = (DigitalModule) jRIO.getModule(ModulePresence.ModuleType.kDigital, moduleNumber);
                dMod.relays[channel] = null;
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            System.out.println("The Digital module couldn't be found.");
            System.out.println("The Relay couldn't be freed.");
            e.printStackTrace();
        }
    }

    public void set(Value value)
    {
        switch (_direction.value)
        {
            case 2: //both
                this._value = value;
                break;
            case 1: //forward
                if(_value.value != Value.kReverse.value)
                {
                    this._value = value;
                }
                break;
            case 0: //reverse
                if(_value.value != Value.kForward.value)
                {
                    this._value = value;
                }
                break;
            default:
                break;
        }
    }

    public Value get()
    {
        switch (_direction.value)
        {
            case 2:
                return _value;
            case 1:
                if(_value.value == Value.kForward.value)
                {
                    return Value.kOn;
                }
                else
                {
                    return Value.kOff;
                }
            case 0:
                if(_value.value == Value.kReverse.value)
                {
                    return Value.kOn;
                }
                else
                {
                    return Value.kOff;
                }
            default:
                return Value.kOff;
        }
    }

    public void setDirection(Direction direction)
    {
        this._direction = direction;
    }
}

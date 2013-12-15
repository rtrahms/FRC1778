package edu.wpi.first.wpilibj;

import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import edu.wpi.first.wpilibj.communication.ModulePresence;
import com.iismathwizard.jrio.jRIO;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 11:49 AM
 */
public class Module extends SensorBase
{
    protected SensorBase[] slots; //used for slot management

    protected int m_moduleNumber; //assume this goes from 0-7 (slot number)
    protected static Module[] m_modules;
    protected ModulePresence.ModuleType m_moduleType;

    /**
     * creates the module
     * @param moduleType the type
     * @param moduleNumber the number (0-7)
     */
    protected Module(ModulePresence.ModuleType moduleType, int moduleNumber)
    {
        m_moduleType = moduleType;
        m_moduleNumber = moduleNumber;

        switch(m_moduleType.getValue())
        {
            case 0:
                slots = new SensorBase[SensorBase.kAnalogChannels];
                break;
            case 1:
                slots = new SensorBase[SensorBase.kDigitalChannels];
                break;
            case 2:
                slots = new SensorBase[SensorBase.kSolenoidChannels];
                break;
            default:
                slots = new SensorBase[0];
                break;
        }

        jRIO.addModule(this, moduleNumber);
    }

    /**
     * Gets the modules at their respective default locations
     * @param moduleType the type of module to get
     * @param moduleNumber the number of which module to get of the specified type
     * @return the module if found. If not found, then return null
     */
    public static Module getModule(ModulePresence.ModuleType moduleType, int moduleNumber)
    {
        try
        {
            if(jRIO.hasModule(moduleType, moduleNumber) && jRIO.getModule(moduleType, moduleNumber).getModuleType().getValue() == moduleType.getValue())
            {
                return jRIO.getModule(moduleType, moduleNumber);
            }
            else
            {
                jRIO.addModule(new Module(moduleType, moduleNumber), moduleType, moduleNumber);
                return jRIO.getModule(moduleType, moduleNumber);
            }
        }
        catch (ModuleDoesNotExistException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return new Module(moduleType, moduleNumber);
        }
    }

    public int getModuleNumber()
    {
        return m_moduleNumber;
    }

    public ModulePresence.ModuleType getModuleType()
    {
        return m_moduleType;
    }
}

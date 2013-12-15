/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 9:03 AM
 */

package com.iismathwizard.jrio;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Module;
import com.iismathwizard.jrio.exceptions.ModuleDoesNotExistException;
import edu.wpi.first.wpilibj.communication.ModulePresence;

import java.util.ArrayList;

public class jRIO
{
    private static Module[] _moduleArray = new Module[8];
    private static ArrayList<Joystick> _joysticks = new ArrayList<Joystick>();

    private jRIO(){} //no accessible constructor

    /**
     * adds a module to the jRIO assuming 8 slots
     * @param module the module to add
     * @param slot the channel to add to 0-7
     * @return returns true if successful; false otherwise
     */
    public static boolean addModule(Module module, int slot)
    {
        if(_moduleArray[slot] == null)
        {
            _moduleArray[slot] = module;
            return true;
        }
        else
        {
            throw new RuntimeException("There already exists a module in slot: " + slot);
        }
    }

    public static boolean addModule(Module module, ModulePresence.ModuleType moduleType, int moduleNumber)
    {
        int index = (moduleNumber - 1) * 4;

        switch(moduleType.getValue())
        {
            case 1: index += 1;
                break;
            case 2: index += 2;
                break;
            default: index += 0; // case 0 and otherwise
                break;
        }

        if(_moduleArray[index] == null)
        {
            _moduleArray[index] = module;
            return true;
        }
        return false;
    }

    /**
     * Checks to see if a module at the specified slot exists
     * @param slot the slot to check 0-7
     * @return true if the module exists, false otherwise
     */
    public static boolean hasModule(int slot)
    {
        return _moduleArray[slot] != null;
    }

    public static boolean hasModule(ModulePresence.ModuleType moduleType, int moduleNumber)
    {
        int index = (moduleNumber - 1) * 4;

        switch(moduleType.getValue())
        {
            case 1: index += 1;
                break;
            case 2: index += 2;
                break;
            default: index += 0; // case 0 and otherwise
                break;
        }

        return _moduleArray[index] != null;
    }

    /**
     * Attempts to get the module at the specified slot
     * @param slot the slot of the module to get 0-7
     * @return the module if it exists
     * @throws ModuleDoesNotExistException if the module at the specified slot does not exist
     */
    public static Module getModule(int slot) throws ModuleDoesNotExistException
    {
        if(hasModule(slot))
        {
            return _moduleArray[slot];
        }
        else
        {
            throw new ModuleDoesNotExistException("The Module at slot: " + slot + " does not exist.");
        }
    }

    public static Module getModule(ModulePresence.ModuleType moduleType, int moduleNumber) throws ModuleDoesNotExistException
    {
        int index = (moduleNumber - 1) * 4;

        switch(moduleType.getValue())
        {
            case 1: index += 1;
                break;
            case 2: index += 2;
                break;
            default: index += 0; // case 0 and otherwise
                break;
        }

        if(hasModule(index))
        {
            return _moduleArray[index];
        }
        else
        {
            throw new ModuleDoesNotExistException("The Module at slot: " + index + " does not exist.");
        }
    }
}

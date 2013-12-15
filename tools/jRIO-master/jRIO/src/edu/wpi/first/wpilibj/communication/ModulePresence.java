package edu.wpi.first.wpilibj.communication;

import edu.wpi.first.wpilibj.Module;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 11:50 AM
 */
public class ModulePresence
{
    static int kMaxModuleNumber = 2;

    ModulePresence(){}

    static boolean getModulePresence(ModuleType moduleType, int moduleNumber)
    {
        return true; // TODO: need to check
    }

    public static class ModuleType
    {
        public static ModuleType kAnalog = new ModuleType(0);
        public static ModuleType kDigital = new ModuleType(1);
        public static ModuleType kSolenoid = new ModuleType(2);
        public static ModuleType kUnknown = new ModuleType(-1);

        int value = -1;

        private ModuleType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

}

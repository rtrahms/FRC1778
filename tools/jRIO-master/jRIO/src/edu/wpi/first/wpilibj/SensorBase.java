package edu.wpi.first.wpilibj;

/**
 * User: IisMathwizard
 * Date: 10/9/13
 * Time: 11:22 AM
 */
public abstract class SensorBase
{
    static int kAnalogChannels = 8;
    static int kAnalogModules = 2;
    static int kDigitalChannels = 14;
    static int kPwmChannels = 10;
    static int kRelayChannels = 8;
    static int kSolenoidChannels = 8;
    static int kSolenoidModules = 2;
    static int kSystemClockTicksPerMicrosecond = 40;

    static private int defaultAnalogModule = 1;
    static private int defaultDigitalModule = 2;
    static private int defaultSolenoidModule = 3;

    SensorBase(){}

    protected static void checkAnalogChannel(int channel)
    {
        //todo: need implementing
    }

    protected static void checkAnalogModule(int moduleNumber)
    {

    }

    protected static void checkDigitalChannel(int channel)
    {

    }

    protected static void checkDigitalModule(int moduleNumber)
    {

    }

    protected static void checkPWMChannel(int channel)
    {

    }

    protected static void checkPWMModule(int moduleNumber)
    {

    }

    protected static void checkRelayChannel(int channel)
    {

    }

    protected static void checkRelayModule(int moduleNumber)
    {

    }

    protected static void checkSolenoidChannel(int channel)
    {

    }

    protected static void checkSolenoidModule(int moduleNumber)
    {

    }

    public void free()
    {

    }

    static int getDefaultAnalogModule()
    {
        return defaultAnalogModule;
    }

    static int getDefaultDigitalModule()
    {
        return defaultDigitalModule;
    }

    static int getDefaultSolenoidModule()
    {
        return defaultSolenoidModule;
    }

    static void setDefaultAnalogModule(int moduleNumber)
    {
        defaultAnalogModule = moduleNumber;
    }

    static void setDefaultDigitalModule(int moduleNumber)
    {
        defaultDigitalModule = moduleNumber;
    }

    static void setDefaultSolenoidModule(int moduleNumber)
    {
        defaultSolenoidModule = moduleNumber;
    }

}

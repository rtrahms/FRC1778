// RobotMap.java

package frc1778;

/**
 * all our dang hardware IDs
 */
public class RobotMap {
//#define CAMERA_IP "10.17.78.11"
    /* Drive Motors controlled by jaguars on CANbus:
     * L - left,  R - right
     * F - front, B - back.
     * */

    private static RobotMap instance;

    public int NUMBER_OF_DRIVE_JAGS = 4;

    public int RB_JAG_ID = 1; // must have 2 drive motors at least
    public int LB_JAG_ID = 2;
    public int RF_JAG_ID = 3; // these are optional and paired into the same gearbox
    public int LF_JAG_ID = 4;

    public int JAGS = 4;

    public int R_MASTER = RB_JAG_ID; // has encoder
    public int R_SLAVE = RF_JAG_ID;
    public int L_MASTER = LB_JAG_ID; // has encoder
    public int L_SLAVE = LF_JAG_ID;

    public int OUTPUT_SPROCKET = 12;
    public int WHEEL_SPROCKET = 36;// will be different for each robot.
    public int ENCODER_TICKS = 250;

    public int ENCODER_TICKS_PER_WHEEL_REV = 
               ((ENCODER_TICKS * WHEEL_SPROCKET) / OUTPUT_SPROCKET);

    public static RobotMap getInstance() {
        if (instance == null) {
            instance = new RobotMap();
        }
        return instance;
    }
    
    public void DBG (String s)
    {
        System.out.println (s);
    }
}

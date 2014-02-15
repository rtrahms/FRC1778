// RobotMap.java

package frc1778;

/**
 * all our dang hardware IDs
 */
public class RobotMap {
//  private static String CAMERA_IP = "10.17.78.11";

    private static RobotMap instance;

    // arm/gate subsystem
    public int GATE_JAG_ID = 4;
    
    // roller subsystem
    public int ROLLER_JAG_ID = 6;
    
    // drive subsystem - controlled by jaguars on CANbus:
    // L - left,  R - right
    // F - front, B - back.   
    public int RF_JAG_ID = 8; // front are optional and paired to same gearbox
    public int RB_JAG_ID = 5; // must have 2 drive motors at least
    public int LF_JAG_ID = 2;
    public int LB_JAG_ID = 1;
    public int R_MASTER = RB_JAG_ID; // with encoder
    public int R_SLAVE  = RF_JAG_ID;
    public int L_MASTER = LB_JAG_ID; // with encoder
    public int L_SLAVE  = LF_JAG_ID;

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

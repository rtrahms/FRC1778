package Utility;

//The XBox One controller also have rumble capabitlities
public class XboxOne {
	public static final int DPAD = 0;//GetPOV () direction for directional gamepad
	public static final int A = 1;//1 Button group left
	public static final int B = 2;//2 Button group down
	public static final int X = 3;//3 Button group right
	public static final int Y = 4;//4 Button group up
	public static final int LEFT_BUMPER = 5;
	public static final int RIGHT_BUMPER = 6;
	public static final int SELECT = 7;//Top middle button on left
	public static final int START = 8;//Top middle button on right
	public static final int LEFT_PRESS_JOYSTICK = 9;
	public static final int RIGHT_PRESS_JOYSTICK = 10;

	public class Axis {
		public static final int LEFT_X = 0;
		public static final int LEFT_Y = 1;
		public static final int LEFT_TRIGGER = 2;//range 0 to 1
		public static final int RIGHT_TRIGGER= 3;//range 0 to -1 (need to verify)
		public static final int RIGHT_X = 4;
		public static final int RIGHT_Y = 5;
	}
}

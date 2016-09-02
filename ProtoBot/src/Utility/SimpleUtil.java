package Utility;

public class SimpleUtil {
	
	public static double limit(boolean limitHigh, double input, double limit){
		if(limitHigh)
			if(input > limit)
				return limit;
			else
				return input;
		else
			if(input < limit)
				return limit;
			else
				return input;
	}
}

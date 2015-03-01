package org.usfirst.frc.team1778.robot;

public class IdleState extends AutoState {
	
	public IdleState() {
		this.name = "<Idle State>";		
	}
	
	public IdleState(String name)
	{
		this.name = name;
	}
	
	// no need for enter, process, exit overloaded methods
	// WE DON'T DO ANYTHING IN IDLE!
}

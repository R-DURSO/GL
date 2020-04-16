package data.actions;

import java.io.Serializable;

public class ActionTypes implements Serializable {
	public static final int ACTION_MAKE_ALLIANCE = 0;
	public static final int ACTION_BREAK_ALLIANCE = 1;
	public static final int ACTION_UPGRADE_CAPITAL = 2;
	public static final int ACTION_DESTROY_BUILDING = 3;
	public static final int ACTION_DESTROY_UNITS= 4;
	public static final int ACTION_CONSTRUCT = 5;
	public static final int ACTION_CREATE_UNITS = 6;
	public static final int ACTION_ATTACK = 7;
	public static final int ACTION_MOVE = 8;
	
	public static final int NUMBER_ACTIONS= 9;
}

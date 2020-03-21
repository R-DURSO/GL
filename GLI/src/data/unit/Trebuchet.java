package data.unit;

import data.Power;

public class Trebuchet  extends Units {
	
	private static final int BASE_HEALTH = 5;
	
	private static final int RANGE_MOVING = 0;
	private static final int MOVEMENT_MOVING = 1;
	private static final int DAMAGE_MOVING = 0;
	
	private static final int RANGE_INSTALLED = 3;
	private static final int MOVEMENT_INSTALLED = 0;
	private static final int DAMAGE_INSTALLED = 40;
	
	private static final int DEFENSE = 0;
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 1;
	
	
	/*specific to Trebuchet : 2 states (moving and installed)
	  moving : can move but not attack
	  installed : can attack but can't move anymore
	  state can be changed at every moment (normally)
	 */
	private final int STATE_MOVING = 0;
	private final int STATE_INSTALLED = 1;
	private int state = STATE_MOVING;
	
	//always 1 Trebuchet per Unit "stack"
	public Trebuchet(Power owner) {
		super(owner, BASE_HEALTH, MOVEMENT_MOVING, 1);
	}

	public int getTypes() {
		return UnitTypes.UNIT_TREBUCHET;
	}

	public int getCost() {
		return COST;
	}
	
	public int getFoodCost() {
		return COST_PER_TURN;
	}
	
	public int getRange() {
		switch(this.state) {
		case STATE_INSTALLED:
			return RANGE_INSTALLED;
		case STATE_MOVING:
			return RANGE_MOVING;
		default:
			return 0;
		}
	}

	public int getDamage() {
		switch(this.state) {
		case STATE_INSTALLED:
			return DAMAGE_INSTALLED;
		case STATE_MOVING:
			return DAMAGE_MOVING;
		default:
			return 0;
		}
	}
	
	public int getDefense() {
		return DEFENSE;
	}
	
	public int getMaxNumber() {
		return NUMBER_MAX_UNITS;
	}
	
	public void changeState() {
		switch(this.state) {
		case STATE_INSTALLED:
			this.state = STATE_MOVING;
			break;
		case STATE_MOVING:
			this.state = STATE_INSTALLED;
			break;
		}
	}
	
	public String toString() {
		return super.toString() ;
	}
}

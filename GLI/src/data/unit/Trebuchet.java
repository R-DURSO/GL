package data.unit;

import java.io.Serializable;

import data.Power;

public class Trebuchet  extends Units implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5609110852041930135L;

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
	  state will change depending on Action (movement change to moving, attack change to installed)
	 */
	
	public final static int STATE_MOVING = 0;
	public final static int STATE_INSTALLED = 1;
	
	private int state = STATE_MOVING;
	
	//always 1 Trebuchet per Unit "stack"
	public Trebuchet(Power owner) {
		super(owner, 1);
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

	public int getHealth() {
		return BASE_HEALTH;
	}
	
	public int getMovement() {
		switch(this.state) {
		case STATE_INSTALLED:
			return MOVEMENT_INSTALLED;
		case STATE_MOVING:
			return MOVEMENT_MOVING;
		default:
			return 0;
		}
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

	public boolean isSiegeUnit() {
		return true;
	}
	
	public int getSiegeDamage() {
		return getDamage();
	}
	
	public int getState() {
		return this.state;
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
		String str = "";
		switch(this.state) {
		case STATE_INSTALLED:
			str = "pret a attaquer";
			break;
		case STATE_MOVING:
			str = "en mouvement";
			break;
		}
		return "Trebuchet "+str+super.toString() ;
	}
}

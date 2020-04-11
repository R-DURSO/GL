package data.unit;

import java.io.Serializable;

import data.Power;

public class Pikeman extends Units implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7584778662983243460L;
	private static final int BASE_HEALTH = 5;
	private static final int RANGE = 1;
	private static final int MOVEMENT = 2;
	private static final int DAMAGE = 4;
	private static final int DEFENSE = 1;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 20;

	public Pikeman (int numberUnits, Power owner) {
		super(owner, numberUnits);
	}

	public int getTypes() {
		return UnitTypes.UNIT_PIKEMAN;
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
		return MOVEMENT;
	}
	
	public int getRange() {
		return RANGE;
	}

	public int getDamage() {
		return DAMAGE;
	}
	
	public int getDefense() {
		return DEFENSE;
	}
	
	public int getMaxNumber() {
		return NUMBER_MAX_UNITS;
	}

	public boolean isSiegeUnit() {
		return false;
	}
	
	public int getSiegeDamage() {
		return DAMAGE;
	}
	
	public String toString() {
		return "Piquier "+super.toString() ;
	}
}

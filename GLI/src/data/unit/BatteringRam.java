package data.unit;

import java.io.Serializable;

import data.Power;

public class BatteringRam extends Units implements Serializable{
	private static final int BASE_HEALTH = 100;
	private static final int RANGE = 1;
	private static final int MOVEMENT = 20;
	private static final int DAMAGE = 5;
	private static final int DAMAGE_SIEGE = 500;
	private static final int DEFENSE = 20;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 1;
	
	public BatteringRam (int numberUnits, Power owner) {
		super(owner, numberUnits);
	}

	public int getTypes() {
		return UnitTypes.UNIT_BATTERING_RAM;
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
		return true;
	}
	
	public int getSiegeDamage() {
		return DAMAGE_SIEGE;
	}
	
	public String toString() {
		return "B�lier"+super.toString() ;
	}
}

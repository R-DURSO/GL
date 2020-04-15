package data.unit;

import java.io.Serializable;

import data.Power;

public class BatteringRam extends Units implements Serializable{
	private static final int BASE_HEALTH = 15;
	private static final int RANGE = 1;
	private static final int MOVEMENT = 2;
	private static final int DAMAGE = 5;
	private static final int DAMAGE_SIEGE = 50;
	private static final int DEFENSE = 2;
	
	public static final int COST = 50;
	public static final int COST_PER_TURN = 15;
	public static final int NUMBER_MAX_UNITS = 1;
	
	public BatteringRam (Power owner) {
		super(owner, 1);
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
		return "Bélier"+super.toString() ;
	}
}

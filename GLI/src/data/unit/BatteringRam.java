package data.unit;

import data.Power;

public class BatteringRam extends Units {
	private static final int BASE_HEALTH = 10;
	private static final int RANGE = 1;
	private static final int MOVEMENT = 1;
	private static final int DAMAGE = 5;
	private static final int DAMAGE_SIEGE = 50;
	private static final int DEFENSE = 2;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 1;
	
	public BatteringRam (int numberUnits, Power owner) {
		super(owner, BASE_HEALTH, MOVEMENT, numberUnits);
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
		return "Belier"+super.toString() ;
	}
}

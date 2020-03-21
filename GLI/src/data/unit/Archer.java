package data.unit;

import data.Power;

public class Archer extends Units {
	private static final int BASE_HEALTH = 2;
	private static final int RANGE = 2;
	private static final int MOVEMENT = 2;
	private static final int DAMAGE = 2;
	private static final int DEFENSE = 1;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 20;
	
	
	public Archer (int numberUnits, Power owner) {
		super(owner, BASE_HEALTH, MOVEMENT, numberUnits);
	}

	public int getTypes() {
		return UnitTypes.UNIT_ARCHER;
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
	
	public String toString() {
		return "Archer" + super.toString() ;
	}
}
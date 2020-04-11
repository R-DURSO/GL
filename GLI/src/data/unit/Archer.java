package data.unit;

import java.io.Serializable;

import data.Power;

public class Archer extends Units implements Serializable{
	private static final long serialVersionUID = 9034922748864820071L;
	
	private static final int BASE_HEALTH = 2;
	private static final int RANGE = 2;
	private static final int MOVEMENT = 2;
	private static final int DAMAGE = 2;
	private static final int DEFENSE = 1;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 20;
	
	
	public Archer (int numberUnits, Power owner) {
		super(owner, numberUnits);
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
		return "Archer" + super.toString() ;
	}
}
package data.unit;

import java.io.Serializable;

import data.Power;

/**
 * <p>Only Units that can go on {@link data.boxes.WaterBox WaterBoxes}</p>
 * <p>Can Contain a Unit, and transport it anywhere else</p>
 * @author Maxence
 */
public class Boat extends Units implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8368385142346761807L;
	private static final int BASE_HEALTH = 1;
	private static final int RANGE = 0;
	private static final int MOVEMENT = 5;
	private static final int DAMAGE = 0;
	private static final int DEFENSE = 0;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 1;
	
	
	/* specific to Boat : can contain Units*/
	private Units containedUnits;

	public Boat (Power owner) {
		super(owner, 1);
		this.containedUnits = null;
	}
	
	public Units getContainedUnits() {
		return containedUnits;
	}
	
	public int getContainedUnitsTypes() {
		return containedUnits.getTypes();
	}
	
	public void setContainedUnits(Units containedUnits) {
		this.containedUnits = containedUnits;
	}
	
	public boolean hasContainedUnits () {
		return containedUnits != null;
	}

	public int getTypes() {
		return UnitTypes.UNIT_BOAT;
	}

	public int getHealth() {
		return BASE_HEALTH;
	}
	
	public int getMovement() {
		return MOVEMENT;
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
		return false;
	}
	
	public int getSiegeDamage() {
		return DAMAGE;
	}
	
	public String toString() {
		if(containedUnits != null) {
			return "Bateau, contient : " + containedUnits;
		}else {
			return "Bateau vide";
		}
	}
}

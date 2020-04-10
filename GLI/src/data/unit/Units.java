package data.unit;

import java.io.Serializable;

import data.Power;

/**
 * <p>Represents a Units in the Game.</p>
 * <p>A Units move across the Map, capturing new territory for the {@link data.Power Power} that control it.</p>
 * <p>A Units have HP, range and damage capabilities, as well as defence and number.</p>
 * Some Units can't have number, so be numerous on the same Boxes,
 * <ul>
 * 	<li>{@link data.unit.Boat Boat}</li>
 * 	<li>{@link data.unit.Trebuchet Trebuchet}</li>
 * </ul>
 * <p>Unis are created by a {@link process.management.UnitManager UnitManager}.</p>
 * @author Maxence
 *
 */
public abstract class Units implements Serializable{
	private int number;
	private boolean isMoving = false;
	
	private Power owner;
	
	public Units(Power owner, int number) {
		this.owner = owner;
		this.number = number;
	}
	
	/**
	 * Returns the number max of units of a defined type, or -1 if the type is not recognized
	 * @param unitsType
	 * @return numberMax of unit types
	 */
	public static int getNumberMaxUnits(int unitsType) {
		switch (unitsType) {
		case UnitTypes.UNIT_ARCHER:
			return Archer.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_BATTERING_RAM:
			return BatteringRam.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_BOAT:
			return Boat.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_CAVALRY:
			return Cavalry.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_INFANTRY:
			return Infantry.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_PIKEMAN:
			return Pikeman.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_TREBUCHET:
			return Trebuchet.NUMBER_MAX_UNITS;
		default:
			return -1;
		}
	}
	
	/**
	 * Returns the cost of a unit of a defined type, or -1 if the type is not recognized
	 * @param unitsType
	 * @return numberMax of unit types
	 */
	public static int getUnitCost(int unitsType) {
		switch (unitsType) {
		case UnitTypes.UNIT_ARCHER:
			return Archer.COST;
		case UnitTypes.UNIT_BATTERING_RAM:
			return BatteringRam.COST;
		case UnitTypes.UNIT_BOAT:
			return Boat.COST;
		case UnitTypes.UNIT_CAVALRY:
			return Cavalry.COST;
		case UnitTypes.UNIT_INFANTRY:
			return Infantry.COST;
		case UnitTypes.UNIT_PIKEMAN:
			return Pikeman.COST;
		case UnitTypes.UNIT_TREBUCHET:
			return Trebuchet.COST;
		default:
			return -1;
		}
	}
	
	/**
	 * Returns the cost of a unit of a defined type, or -1 if the type is not recognized
	 * @param unitsType
	 * @return numberMax of unit types
	 */
	public static int getUnitCostPerTurn(int unitsType) {
		switch (unitsType) {
		case UnitTypes.UNIT_ARCHER:
			return Archer.COST_PER_TURN;
		case UnitTypes.UNIT_BATTERING_RAM:
			return BatteringRam.COST_PER_TURN;
		case UnitTypes.UNIT_BOAT:
			return Boat.COST_PER_TURN;
		case UnitTypes.UNIT_CAVALRY:
			return Cavalry.COST_PER_TURN;
		case UnitTypes.UNIT_INFANTRY:
			return Infantry.COST_PER_TURN;
		case UnitTypes.UNIT_PIKEMAN:
			return Pikeman.COST_PER_TURN;
		case UnitTypes.UNIT_TREBUCHET:
			return Trebuchet.COST_PER_TURN;
		default:
			return -1;
		}
	}
	
	/**
	 * @return Which Types the Unit is
	 * @see {@link data.unit.UnitTypes UnitTypes}
	 */
	public abstract int getTypes();
	public abstract int getCost();
	public abstract int getFoodCost();
	/**
	 * <p>get the Range of the {@link data.unit.Units Unit}</p>
	 * @return
	 * <ul>
	 * 		<li>0 if it can't attack</li>
	 * 		<li>1 if it attack on melee</li>
	 * 		<li>More if it can attack at distance</li>
	 * </ul>
	 */
	public abstract int getRange();
	/**
	 * @return the effective Attack of the {@link data.unit.Units Unit}
	 */
	public abstract int getDamage();
	/**
	 * @return the effective Defense of the {@link data.unit.Units Unit}
	 */
	public abstract int getDefense();
	/**
	 * @return the Max you can have on a stack of {@link data.unit.Units Unit}
	 */
	public abstract int getMaxNumber();
	public abstract boolean isSiegeUnit();
	public abstract int getSiegeDamage();
	public abstract int getHealth();
	public abstract int getMovement();
	
	
	public int getNumber() {
		return number;
	}
	
	public void addNumber (int number) {
		this.number += number;
		if (this.number <= 0) {
			this.number = 0;
		}
	}
	
	public void subNumber (int number) {
		this.number -= number;
		if (this.number <= 0) {
			this.number = 0;
		}
	}
	
	public Power getOwner() {
		return owner;
	}
	
	/**
	 * @return true if the {@link data.unit.Units Unit} can move
	 */
	public boolean getIsMoving() {
		return isMoving;
	}
	
	/**
	 * Set isMoving so that the {@link data.unit.Units Unit} cannot be used in ActionValidator
	 */
	public void setIsMoving() {
		this.isMoving = true;
	}

	/**
	 * Set isMoving so that the {@link data.unit.Units Unit} can be used again
	 */
	public void resetIsMoving() {
		this.isMoving = false;
	}

	public String toString() {
		return "("+number+") attaque:"+getDamage()+" defense:"+getDefense();
	}
}

package data.unit;

import java.io.Serializable;

import data.Power;
import data.boxes.Box;

/**
 * <p>Represents a Units in the {@link process.game.GameLoop Game}.</p>
 * <p>A Units move across the {@link data.GameMap Map}, capturing new territory for the {@link data.Power Power} that control it.</p>
 * <p>A Units have HP, {@link data.unit.Units#getRange() range} and {@link data.unit.Units#getDamage() damage},
 * as well as {@link data.unit.Units#getDefense() defense} and {@link data.unit.Units#getNumber() number}.</p>
 * <ul>Some Units can't have number, so they is only one on the same {@link data.boxes.Box box}:
 * 	<li>{@link data.unit.Boat Boat}</li>
 * 	<li>{@link data.unit.BatteringRam BatteringRam}</li>
 * 	<li>{@link data.unit.Trebuchet Trebuchet}</li>
 * </ul>
 * <p>Unis are created by a {@link process.management.UnitManager UnitManager}.</p>
 * @author Maxence
 *
 */
public abstract class Units implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int number;
	private boolean isMoving = false;
	
	private Power owner;
	
	public Units(Power owner, int number) {
		this.owner = owner;
		this.number = number;
	}
	
	/**
	 * Returns the number max of {@link data.unit.Units Unit}, or -1 if the {@link data.unit.UnitTypes Types} is not recognized
	 * @param unitsType {@link data.unit.UnitTypes UnitTypes}
	 * @return numberMax of given unitTypes
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
	 * Returns the cost in {@link data.resource.Gold Gold} of a {@link data.unit.Units Unit}, or -1 if the given {@link data.unit.UnitTypes Types} is not recognized
	 * @param unitsType {@link data.unit.UnitTypes UnitTypes}
	 * @return Cost of a single Unit
	 * @see {@link data.unit.Units#getCost() getCost()}
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
	 * Returns the Production Cost in {@link data.resource.Food Food} of a {@link data.unit.Units Unit}, or -1 if the given {@link data.unit.UnitTypes Types} is not recognized
	 * @param unitsType {@link data.unit.UnitTypes UnitTypes}
	 * @return Cost per Turn of a single Unit
	 * @see {@link data.unit.Units#getFoodCost() getFoodCost()}
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
	
	/**
	 * Returns the cost in {@link data.resource.Gold Gold} of a {@link data.unit.Units Unit}
	 * @return Cost of the implemented Unit
	 */
	public abstract int getCost();
	
	/**
	 * Returns the cost in {@link data.resource.Food Food} of a {@link data.unit.Units Unit}
	 * @return Cost of the implemented Unit
	 */
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
	/**
	 * @return true if the {@link data.unit.Units Unit} is from a {@link data.building.army.Workshop WorkShop},
	 * <br>giving it special damage to {@link data.building.Building Buildings}
	 */
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

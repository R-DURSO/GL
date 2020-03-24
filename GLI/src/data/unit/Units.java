package data.unit;

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
public abstract class Units {
	private int health;
	private int movement;
	private int number;
	private boolean isMoving = false;
	
	private Power owner;
	
	public Units(Power owner, int health, int move, int number) {
		this.owner = owner;
		this.health = health;
		this.movement = move;
		this.number = number;
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

	public int getHealth() {
		return health;
	}

	public int getMovement() {
		return movement;
	}
	
	public boolean getIsMoving() {
		return isMoving;
	}
	
	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public String toString() {
		return ": "+number+", attaque:"+getDamage()+" defense:"+getDefense();
	}
}

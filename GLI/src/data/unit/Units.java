package data.unit;

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
	private int range;
	private int movement;
	private int number;
	private int damage;
	private int defense;
	private int maxNumber;
	
	public Units(int health, int range, int move, int number, int damage, int defense, int maxNumber) {
		this.health = health;
		this.range = range;
		this.movement = move;
		this.number = number;
		this.damage = damage;
		this.defense = defense;
		this.maxNumber = maxNumber;
	}
	
	public abstract int getTypes();
	public abstract int getCost();
	public abstract int getFoodCost();
	
	
	
	public int getNumber() {
		return number;
	}
	
	public void addNumber (int number) {
		this.number += number;
		if (this.number <= 0) {
			this.number = 0;
		}
	}
	
	public void substractNumber (int number) {
		this.number -= number;
		if (this.number <= 0) {
			this.number = 0;
		}
	}

	public int getHealth() {
		return health;
	}


	public int getRange() {
		return range;
	}


	public int getMovement() {
		return movement;
	}


	public int getDamage() {
		return damage;
	}


	public int getDefense() {
		return defense;
	}
	
	public int getMaxNumber() {
		return maxNumber;
	}

	public String toString() {
		return ": "+number+", atk:"+damage+" def:"+defense;
	}
}

package data.unit;

import process.visitor.unit_visitor.UnitVisitor;

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
	
	
	public abstract <U> U accept(UnitVisitor<U> visitor);
	public abstract int getTypes();
	public abstract int getCost();
	
	
	
	public int getNumber() {
		return number;
	}
	
	public void addNumber (int number) {
		this.number += number;
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

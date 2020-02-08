package data.unit;

public  abstract class Units {
	private int health;
	private int range;
	private int movement;
	private int number;
	private int damage;
	private int defense;
	
	public Units(int health, int range, int move, int number, int damage, int defense) {
		this.health = health;
		this.range = range;
		this.movement = move;
		this.number = number;
		this.damage = damage;
		this.defense = defense;
	}
	
	
	
	public String toString() {
		return "unit"; // TODO methode a remplir 
	}
}

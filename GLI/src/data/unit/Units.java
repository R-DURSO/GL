package data.unit;

public  abstract class Units {
	private int hp;
	private int range;
	private int movement;
	private int number;
	private int damage;
	
	public Units(int hp, int range, int move, int num, int dmg) {
		this.hp = hp;
		this.range = range;
		this.movement = move;
		this.number = num;
		this.damage = dmg;
	}
	
	
	
	public String toString() {
		return "unit"; // TODO methode a remplir 
	}
}

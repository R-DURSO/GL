package data.unit;

public  abstract class Units {
	private int hp;
	private int range;
	private int movement;
	private int number;
	private int damage;
	private int cost;
	
	public Units(int hp, int range, int move, int num, int dmg, int cost) {
		this.hp = hp;
		this.range = range;
		this.movement = move;
		this.number = num;
		this.damage = dmg;
		this.cost = cost;
	}
	
	public String toString() {
		return ""; // methode a remplir 
	}
}

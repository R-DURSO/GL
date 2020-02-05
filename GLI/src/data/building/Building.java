package data.building;

public abstract class Building {
	
	private int cost;
	private int buildTime;
	private int hp;
	
	public Building(int cost, int buildTime, int hp) {
		super();
		this.cost = cost;
		this.buildTime = buildTime;
		this.hp = hp;
	}
}
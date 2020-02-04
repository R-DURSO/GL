package data.buildings;

public abstract class Building {
	
	private int posX;
	private int posY;
	private int cost;
	private int buildTime;
	private int health;
	
	
	protected Building(int x, int y, int c0st, int bTime, int HP) {
		posX = x;
		posY = y;
		cost = c0st;
		buildTime = bTime;
		health = HP;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
	
	
	
}

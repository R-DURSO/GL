package data;

public abstract class Building {
	
	private int posX;
	private int posY;
	
	protected Building(int x, int y) {
		posX = x;
		posY = y;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
	
	
	
}

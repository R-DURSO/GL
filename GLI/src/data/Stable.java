package data;

public class Stable extends Building {

	private static final int COST = 20;
	private static final int CONSTRUCTION_TIME = 1;
	
	private int health = 1;
	private int constructionTime = 0;
	
	public Stable(int x, int y) {
		super(x, y);
	}
	
	
}

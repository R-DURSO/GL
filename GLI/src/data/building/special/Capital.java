package data.building.special;
import data.building.Building;

public class Capital extends Building {
	private int level = 1;
	
	private static final int MAX_LEVEL = 5;
	private static final int COST_LEVEL_2 = 50;
	private static final int COST_LEVEL_3 = 150;
	private static final int COST_LEVEL_4 = 300;
	private static final int COST_LEVEL_5 = 3000;
	
	
	public Capital() {
		super(0, 0, 5);
	}

}

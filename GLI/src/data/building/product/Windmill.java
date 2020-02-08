package data.building.product;

import data.building.Building;

public class Windmill extends Building {
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	private static final int PRODUCTION_PER_TURN = 20;
	
	
	public Windmill() {
		super(BUILD_TIME, BASE_HEALTH);
	}


}

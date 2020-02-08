package data.building.army;

import data.building.Building;

public class Dock extends BuildingArmy {
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Dock() {
		super(BUILD_TIME, BASE_HEALTH);
	}
}
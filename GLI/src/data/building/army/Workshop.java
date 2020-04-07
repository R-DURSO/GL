package data.building.army;

import data.building.BuildingTypes;

public class Workshop extends BuildingArmy {
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Workshop() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_WORKSHOP;
	}
	
	public String toString() {
		return "Atelier "+super.toString();
	}


}

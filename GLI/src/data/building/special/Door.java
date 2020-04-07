package data.building.special;

import data.building.BuildingTypes;

public class Door extends BuildingSpecial{
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 10;
	
	public Door() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_DOOR;
	}
	
	public String toString() {
		return "Portail "+super.toString();
	}
}

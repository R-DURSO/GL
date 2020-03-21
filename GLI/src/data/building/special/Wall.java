package data.building.special;

import data.building.BuildingTypes;

public class Wall extends BuildingSpecial{
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 20;
	
	public Wall() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_WALL;
	}
	
	public String toString() {
		return "Mur";
	}
}

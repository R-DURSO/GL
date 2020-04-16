package data.building.special;

import java.io.Serializable;

import data.building.BuildingTypes;

public class Wall extends BuildingSpecial implements Serializable{
	private static final int BUILD_TIME = 2;
	public static final int COST = 160;
	private static final int BASE_HEALTH = 30;
	
	public Wall() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_WALL;
	}
	
	public String toString() {
		return "Mur "+super.toString();
	}
}

package data.building.special;

import java.io.Serializable;

import data.building.BuildingTypes;

public class Door extends BuildingSpecial implements Serializable{
	private static final int BUILD_TIME = 2;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 20;
	
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

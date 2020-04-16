package data.building.army;

import java.io.Serializable;

import data.building.BuildingTypes;

public class Barrack extends BuildingArmy implements Serializable {
	private static final int BUILD_TIME = 1;
	public static final int COST = 80;
	private static final int BASE_HEALTH = 5;
	
	public Barrack() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_BARRACK;
	}
	
	public String toString() {
		return "Caserne "+super.toString();
	}

}
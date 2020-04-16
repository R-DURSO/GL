package data.building.army;

import java.io.Serializable;

import data.building.BuildingTypes;

public class Workshop extends BuildingArmy implements Serializable {
	private static final int BUILD_TIME = 1;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 5;
	
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

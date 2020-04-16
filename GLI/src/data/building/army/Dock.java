package data.building.army;

import java.io.Serializable;

import data.building.BuildingTypes;

public class Dock extends BuildingArmy implements Serializable {
	private static final int BUILD_TIME = 1;
	public static final int COST = 60;
	private static final int BASE_HEALTH = 3;
	
	public Dock() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	@Override
	public int getType() {
		return BuildingTypes.BUILDING_DOCK;
	}
	
	public String toString() {
		return "Port "+super.toString();
	}

	
}
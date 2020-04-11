package data.building.army;

import java.io.Serializable;

import data.building.BuildingTypes;

public class Barrack extends BuildingArmy implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 430213026249199630L;
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
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
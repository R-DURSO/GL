package data.building.special;

import java.io.Serializable;

import data.building.Building;

public abstract class BuildingSpecial extends Building implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7191247393515164542L;

	public BuildingSpecial(int buildTime, int health) {
		super(buildTime, health);
	}

}

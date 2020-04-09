package data.building.special;

import java.io.Serializable;

import data.building.Building;

public abstract class BuildingSpecial extends Building implements Serializable {

	public BuildingSpecial(int buildTime, int health) {
		super(buildTime, health);
	}

}

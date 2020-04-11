package data.building.army;
import java.io.Serializable;

import data.building.Building;

public abstract class BuildingArmy extends Building implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990683307078764335L;

	public BuildingArmy(int buildTime, int health) {
		super(buildTime, health);
		// TODO Auto-generated constructor stub
	}

}

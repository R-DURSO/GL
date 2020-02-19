package data.actions;

import data.Position;
import data.Power;

public class ActionConstruct extends Action {
	private Position target;
	private int buildingType;

	public ActionConstruct(Power powerConcerned, int buildingType, Position target) {
		super(powerConcerned);
		this.target = target;
		this.buildingType = buildingType;
	}


	public int getBuildingType() {
		return buildingType;
	}
}

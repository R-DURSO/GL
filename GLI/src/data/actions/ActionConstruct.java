package data.actions;

import java.io.Serializable;

import data.Position;
import data.Power;

public class ActionConstruct extends Action implements Serializable {
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
	
	public Position getTarget() {
		return target;
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_CONSTRUCT;
	}
}

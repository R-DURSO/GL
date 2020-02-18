package data.actions;

import data.Power;

public class ActionConstruct extends Action {
	private int positionX;
	private int positionY;
	private int buildingType;

	public ActionConstruct(Power powerConcerned, int buildingType, int positionX, int positionY) {
		super(powerConcerned);
		this.positionX = positionX;
		this.positionY = positionY;
		this.buildingType = buildingType;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public int getBuildingType() {
		return buildingType;
	}
}

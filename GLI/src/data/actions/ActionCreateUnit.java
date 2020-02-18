package data.actions;

import data.Power;

public class ActionCreateUnit extends Action{
	private int positionX;
	private int positionY;
	private int unitType;
	private int numberUnits;

	public ActionCreateUnit(Power powerConcerned, int unitType, int numberUnits, int positionX, int positionY) {
		super(powerConcerned);
		this.positionX = positionX;
		this.positionY = positionY;
		this.unitType = unitType;
		this.numberUnits = numberUnits;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public int getUnitType() {
		return unitType;
	}
}

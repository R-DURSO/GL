package data.actions;

import data.Power;

public class ActionDestroyBuilding extends Action{
	private int positionX;
	private int positionY;

	public ActionDestroyBuilding(Power powerConcerned, int positionX, int positionY) {
		super(powerConcerned);
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}
}

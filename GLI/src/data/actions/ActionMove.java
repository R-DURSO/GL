package data.actions;

import data.Power;

public class ActionMove extends Action {
	private int positionX;
	private int positionY;

	public ActionMove(Power powerConcerned, int positionX, int positionY) {
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

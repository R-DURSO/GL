package data.actions;

import data.Power;

public class ActionAttack extends Action{
	private int positionX;
	private int positionY;

	public ActionAttack(Power powerConcerned, int positionX, int positionY) {
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

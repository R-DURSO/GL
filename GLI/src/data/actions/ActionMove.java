package data.actions;

import java.io.Serializable;

import data.Power;
import data.boxes.Box;

public class ActionMove extends Action implements Serializable {
	private Box[] path;

	public ActionMove(Power powerConcerned, Box[] path) {
		super(powerConcerned);
		this.path = path;
	}
	
	public Box[] getPath() {
		return path;
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_MOVE;
	}
}

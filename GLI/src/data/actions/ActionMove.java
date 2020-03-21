package data.actions;

import data.Position;
import data.Power;
import data.boxes.Box;

public class ActionMove extends Action {
	private Position from;
	private Position target;
	private Box[] path;

	public ActionMove(Power powerConcerned, Position from, Position target, Box[] path) {
		super(powerConcerned);
		this.from = from;
		this.target = target;
		this.path = path;
	}

	public Position getFrom() {
		return from;
	}

	public Position getTarget() {
		return target;
	}

}

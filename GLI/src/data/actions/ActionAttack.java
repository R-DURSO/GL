package data.actions;

import data.Position;
import data.Power;

public class ActionAttack extends Action{
	private Position from;
	private Position target;
	
	public ActionAttack(Power powerConcerned, Position from, Position target) {
		super(powerConcerned);
		this.from = from;
		this.target = target;
	}

	public Position getFrom() {
		return from;
	}

	public Position getTarget() {
		return target;
	}
}

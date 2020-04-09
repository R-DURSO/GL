package data.actions;

import java.io.Serializable;

import data.Position;
import data.Power;

/**
 * Sub-class of {@link Action}.
 * Defines data of an attack. Don't do anything
 */
public class ActionAttack extends Action implements Serializable{
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

	@Override
	public int getActionType() {
		return ActionTypes.ACTION_ATTACK;
	}
}

package data.actions;

import data.Power;

/**
 * <p>Abstract class which define an action with parameters.</p>
 * <p>Used in {@link process.game.GameLoop GameLoop}.</p>
 * <p>Created (if action is possible) in {@link process.management.ActionValidator ActionValidator}</p>
 */
public abstract class Action {
	private Power power;
	
	public Action(Power powerConcerned) {
		this.power = powerConcerned;
	}

	public Power getPowerConcerned() {
		return power;
	}
	
	public abstract int getActionType();
}

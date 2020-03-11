package data.actions;

import data.Power;

/**
 * Abstract class which define an action with parameters.
 * Used in {@link process.game.GameLoop}.
 * Created (if action is possible) in {@link process.management.ActionValidator}
 */
public abstract class Action {
	private Power power;
	
	public Action(Power powerConcerned) {
		this.power = powerConcerned;
	}

	public Power getPowerConcerned() {
		return power;
	}
}

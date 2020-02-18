package data.actions;

import data.Power;

public abstract class Action {
	private Power power;
	
	public Action(Power powerConcerned) {
		this.power = powerConcerned;
	}

	public Power getPower() {
		return power;
	}
}

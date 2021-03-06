package data.actions;

import java.io.Serializable;

import data.Power;

public class ActionMakeAlliance extends Action implements Serializable{
	private Power potentialAllied;

	public ActionMakeAlliance(Power powerConcerned, Power potentialAllied) {
		super(powerConcerned);
		this.potentialAllied = potentialAllied;
	}

	public Power getPotentialAllied() {
		return potentialAllied;
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_MAKE_ALLIANCE;
	}
}

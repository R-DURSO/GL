package data.actions;

import java.io.Serializable;

import data.Power;

public class ActionBreakAlliance extends Action implements Serializable{

	public ActionBreakAlliance(Power powerConcerned) {
		super(powerConcerned);
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_BREAK_ALLIANCE;
	}
}

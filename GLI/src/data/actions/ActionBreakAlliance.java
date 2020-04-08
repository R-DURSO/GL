package data.actions;

import data.Power;

public class ActionBreakAlliance extends Action{

	public ActionBreakAlliance(Power powerConcerned) {
		super(powerConcerned);
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_BREAK_ALLIANCE;
	}
}

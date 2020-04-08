package data.actions;

import data.Power;

public class ActionUpgradeCapital extends Action{
	
	public ActionUpgradeCapital(Power powerConcerned) {
		super(powerConcerned);
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_UPGRADE_CAPITAL;
	}
}

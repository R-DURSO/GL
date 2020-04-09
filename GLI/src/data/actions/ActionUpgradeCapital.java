package data.actions;

import java.io.Serializable;

import data.Power;

public class ActionUpgradeCapital extends Action implements Serializable{
	
	public ActionUpgradeCapital(Power powerConcerned) {
		super(powerConcerned);
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_UPGRADE_CAPITAL;
	}
}

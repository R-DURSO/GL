package data.actions;

import java.io.Serializable;

import data.Power;

public class ActionUpgradeCapital extends Action implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8114256402509587121L;

	public ActionUpgradeCapital(Power powerConcerned) {
		super(powerConcerned);
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_UPGRADE_CAPITAL;
	}
}

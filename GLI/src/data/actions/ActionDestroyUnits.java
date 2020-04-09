package data.actions;

import java.io.Serializable;

import data.Position;
import data.Power;

public class ActionDestroyUnits extends Action implements Serializable{
	private Position target;
	
	public ActionDestroyUnits(Power powerConcerned, Position target) {
		super(powerConcerned);
		this.target = target;
	}

	public Position getTarget() {
		return target;
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_DESTROY_UNITS;
	}
}

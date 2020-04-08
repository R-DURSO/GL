package data.actions;

import data.Position;
import data.Power;

public class ActionDestroyBuilding extends Action{
	private Position target;
	
	public ActionDestroyBuilding(Power powerConcerned, Position target) {
		super(powerConcerned);
		this.target = target;
	}

	public Position getTarget() {
		return target;
	}
	
	@Override
	public int getActionType() {
		return ActionTypes.ACTION_DESTROY_BUILDING;
	}
}

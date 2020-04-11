package data.actions;

import java.io.Serializable;

import data.Position;
import data.Power;

public class ActionDestroyBuilding extends Action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4899618628428511884L;
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

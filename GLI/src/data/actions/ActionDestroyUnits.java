package data.actions;

import data.Position;
import data.Power;

public class ActionDestroyUnits extends Action {
	private Position target;
	
	public ActionDestroyUnits(Power powerConcerned, Position target) {
		super(powerConcerned);
		this.target = target;
	}

	public Position getTarget() {
		return target;
	}
	
}

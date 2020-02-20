package data.actions;

import data.Position;
import data.Power;

public class ActionCreateUnit extends Action{
	private Position target;
	private int unitType;
	private int numberUnits;

	public ActionCreateUnit(Power powerConcerned, int unitType, int numberUnits, Position target) {
		super(powerConcerned);
		this.target = target;
		this.unitType = unitType;
		this.numberUnits = numberUnits;
	}

	public Position getTarget() {
		return target;
	}

	public int getUnitType() {
		return unitType;
	}
	
	public int getNumberUnits() {
		return numberUnits;
	}
}

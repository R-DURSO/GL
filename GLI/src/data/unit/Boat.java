package data.unit;

import process.visitor.unit_visitor.UnitVisitor;

public class Boat extends Units {
	private static final int BASE_HEALTH = 1;
	private static final int RANGE = 0;
	private static final int MOVEMENT = 5;
	private static final int DAMAGE = 0;
	private static final int DEFENSE = 0;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 1;
	
	
	/* specific to Boat : can contain Units*/
	private Units containedUnits;

	public Boat () {
		super(BASE_HEALTH, RANGE, MOVEMENT, 1, DAMAGE, DEFENSE, NUMBER_MAX_UNITS);
		this.containedUnits = null;
	}
	
	public Boat (Units containedUnits) {
		super(BASE_HEALTH, RANGE, MOVEMENT, 1, DAMAGE, DEFENSE, NUMBER_MAX_UNITS);
		this.containedUnits = containedUnits;
	}
	
	public Units getContainedUnits() {
		return containedUnits;
	}

	public void setContainedUnits(Units containedUnits) {
		this.containedUnits = containedUnits;
	}

	public <U> U accept(UnitVisitor<U> visitor) {
		return visitor.visit(this);
	}

	public int getTypes() {
		return UnitTypes.UNIT_BOAT;
	}

	public int getCost() {
		return COST;
	}
	
	public String toString() {
		return "Boat: contain "+containedUnits+super.toString();
	}
}

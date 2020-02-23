package data.unit;

import process.visitor.unit_visitor.UnitVisitor;

public class Infantry extends Units {
	private static final int BASE_HEALTH = 2;
	private static final int RANGE = 1;
	private static final int MOVEMENT = 1;
	private static final int DAMAGE = 0;
	private static final int DEFENSE = 1;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 20;
	

	public Infantry (int numberUnits) {
		super(BASE_HEALTH, RANGE, MOVEMENT, numberUnits, DAMAGE, DEFENSE, NUMBER_MAX_UNITS);
	}
	
	@Override
	public <U> U accept(UnitVisitor<U> visitor) {
		return visitor.visit(this);
	}

	public int getTypes() {
		return UnitTypes.UNIT_INFANTRY;
	}

	public int getCost() {
		return COST;
	}
	
	@Override
	public int getFoodCost() {
		return COST_PER_TURN;
	}
	
	public String toString() {
		return "Infantry " + super.toString() ;
	}

}

package data.unit;

import process.visitor.unit_visitor.UnitVisitor;

public class Pikeman extends Units {
	private static final int BASE_HEALTH = 5;
	private static final int RANGE = 0;
	private static final int MOVEMENT = 1;
	private static final int DAMAGE = 0;
	private static final int DEFENSE = 1;
	
	private static final int COST = 10;
	private static final int COST_PER_TURN = 3;
	private static final int NUMBER_MAX_UNITS = 20;
	
	
	//pas utile je pense (le joueur créera directement un stack d'unités)
	public Pikeman () {
		super(BASE_HEALTH, RANGE, MOVEMENT, 1, DAMAGE, DEFENSE);
		// TODO Auto-generated constructor stub
	}
	
	public Pikeman (int numberUnits) {
		super(BASE_HEALTH, RANGE, MOVEMENT, numberUnits, DAMAGE, DEFENSE);
		// TODO Auto-generated constructor stub
	}

	public int getTypes() {
		return UnitTypes.UNIT_PIKEMAN;
	}

	public int getCost() {
		return COST;
	}
	
	@Override
	public <U> U accept(UnitVisitor<U> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return super.toString() ;
	}
}

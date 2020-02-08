package data.unit;

public class Boat extends Units {
	private static final int BASE_HEALTH = 1;
	private static final int RANGE = 0;
	private static final int MOVEMENT = 5;
	private static final int DAMAGE = 0;
	private static final int DEFENSE = 0;
	
	private static final int COST = 10;
	private static final int COST_PER_TURN = 3;
	
	
	/* specific to Boat : can contain Units*/
	private Units containedUnits;
	
	public Boat (Units containedUnits) {
		super(BASE_HEALTH, RANGE, MOVEMENT, 1, DAMAGE, DEFENSE);
		this.containedUnits = containedUnits;
	}
	
	public String toString() {
		return super.toString() ;
	}
}

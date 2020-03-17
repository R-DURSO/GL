package data.unit;

public class Cavalry extends Units {
	private static final int BASE_HEALTH = 2;
	private static final int RANGE = 0;
	private static final int MOVEMENT = 3;
	private static final int DAMAGE = 2;
	private static final int DEFENSE = 1;
	
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 20;
	

	public Cavalry (int numberUnits) {
		super(BASE_HEALTH, RANGE, MOVEMENT, numberUnits, DAMAGE, DEFENSE, NUMBER_MAX_UNITS);
		// TODO Auto-generated constructor stub
	}

	public int getTypes() {
		return UnitTypes.UNIT_CAVALRY;
	}

	public int getCost() {
		return COST;
	}
	
	public int getFoodCost() {
		return COST_PER_TURN;
	}
	
	public String toString() {
		return "Cavalier" + super.toString() ;
	}
}

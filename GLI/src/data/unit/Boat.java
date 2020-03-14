package data.unit;

/**
 * <p>Only Units that can go on {@link data.boxes.WaterBox WaterBoxes}</p>
 * <p>Can Contain a Unit, and transport elsewhere</p>
 * @author Maxence
 */
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
	
	public int getContainedUnitsTypes() {
		return containedUnits.getTypes();
	}
	
	public void setContainedUnits(Units containedUnits) {
		this.containedUnits = containedUnits;
	}
	
	public boolean hasContainedUnits () {
		return containedUnits != null;
	}

	public int getTypes() {
		return UnitTypes.UNIT_BOAT;
	}

	public int getCost() {
		return COST;
	}
	
	public int getFoodCost() {
		return COST_PER_TURN;
	}
	
	public String toString() {
		return "Boat: contain "+containedUnits+super.toString();
	}
}

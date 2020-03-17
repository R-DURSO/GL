package data.unit;

public class Trebuchet  extends Units {
	
	private static final int BASE_HEALTH = 5;
	
	private static final int RANGE_MOVING = 0;
	private static final int MOVEMENT_MOVING = 1;
	private static final int DAMAGE_MOVING = 0;
	
	private static final int RANGE_INSTALLED = 3;
	private static final int MOVEMENT_INSTALLED = 0;
	private static final int DAMAGE_INSTALLED = 3;
	
	private static final int DEFENSE = 0;
	public static final int COST = 10;
	public static final int COST_PER_TURN = 3;
	public static final int NUMBER_MAX_UNITS = 1;
	
	
	/*specific to Trebuchet : 2 states (moving and installed)
	  moving : can move but not attack
	  installed : can attack but can't move anymore
	  state can be changed at every moment (normally)
	 */
	private final int STATE_MOVING = 0;
	private final int STATE_INSTALLED = 1;
	private int state = STATE_MOVING;
	
	//always 1 Trebuchet per Unit "stack"
	public Trebuchet() {
		super(BASE_HEALTH, RANGE_MOVING, MOVEMENT_MOVING, 1, DAMAGE_MOVING, DEFENSE, NUMBER_MAX_UNITS);
	}

	public int getTypes() {
		return UnitTypes.UNIT_TREBUCHET;
	}

	public int getCost() {
		return COST;
	}
	
	public int getFoodCost() {
		return COST_PER_TURN;
	}
	
	public void changeState() {
		switch(this.state) {
		case STATE_INSTALLED:
			this.state = STATE_MOVING;
			break;
		case STATE_MOVING:
			this.state = STATE_INSTALLED;
			break;
		}
	}
	
	public String toString() {
		return super.toString() ;
	}
}

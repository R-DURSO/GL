package data.unit;

public class Trebuchet  extends Units {
	private static final int BASE_HEALTH = 5;
	private static final int RANGE = 0;
	private static final int MOVEMENT = 1;
	private static final int DAMAGE = 0;
	
	private static final int COST = 10;
	private static final int COST_PER_TURN = 3;
	
	//always 1 Trebuchet per Unit "stack"
	public Trebuchet() {
		super(BASE_HEALTH, RANGE, MOVEMENT, 1, DAMAGE);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return super.toString() ;
	}
}

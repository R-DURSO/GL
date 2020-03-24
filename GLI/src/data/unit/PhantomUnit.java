package data.unit;

import data.Power;

/**
 * This Unit type is not a playable Unit, it simply ensures that 2 Units never want to go on the same box
 * during preparation time. 
 * <b>This Class must be used only with {@link process.management.ActionValidator}</b>
 * @author Aldric Vitali Silvestre
 * @see process.management.ActionValidator
 */
public class PhantomUnit extends Units{

	public PhantomUnit (Power owner) {
		super(owner, 0, 0, 0);
	}

	@Override
	public int getTypes() {
		return -1;
	}

	@Override
	public int getCost() {
		return -1;
	}

	@Override
	public int getFoodCost() {
		return -1;
	}

	@Override
	public int getRange() {
		return 0;
	}

	@Override
	public int getDamage() {
		return 0;
	}

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	public int getMaxNumber() {
		return 0;
	}

	@Override
	public boolean isSiegeUnit() {
		return false;
	}

	@Override
	public int getSiegeDamage() {
		return 0;
	}

}

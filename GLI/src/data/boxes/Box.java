package data.boxes;

import data.Power;
import data.unit.Units;

/**
 * <p>A Box represent a country.</p>
 * <p>two types of Boxes, {@link data.boxes.GroundBox GroundBox} and {@link data.boxes.WaterBox WaterBox}</p>
 * @author Maxence
 */
public abstract class Box {
	private Units unit;
	private Power owner;
	
	public Box() {
	}

	public Units getUnit() {
		return unit;
	}

	public Power getOwner() {
		return owner;
	}

	public boolean hasOwner() {
		return owner != null;
	}
	
	public boolean hasUnit() {
		return unit != null;
	}

	public void setUnit(Units unit) {
		this.unit = unit;
	}

	public void setOwner(Power owner) {
		this.owner = owner;
	}

	public String toString() {
		String string = "";
		string += " Unit=" + unit;
		if (hasOwner()) {
			string += ", Owner="+ owner.getName();
		}
		return string + " | " ;
	}
	
	/**
	 * permits to have same String length for all boxes toString() return 
	 * @return a String with more spaces, if necessary
	 */
	private String padString(String strToPad, int desiredLength) {
		String result = new String(new char[desiredLength - strToPad.length()]);
		return result.replace('\0', ' ') + strToPad;
	}
}

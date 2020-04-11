package data.boxes;

import java.io.Serializable;

import data.Power;
import data.unit.Units;

/**
 * <p>A Box represent a country.</p>
 * <p>two types of Boxes, {@link data.boxes.GroundBox GroundBox} and {@link data.boxes.WaterBox WaterBox}</p>
 * <p>Boxes can have a {@link data.Power Power} representing them,
 * <br>and contain a {@link data.unit.Units Unit}.</p>
 * @author Maxence
 */
public abstract class Box implements Serializable {
	private static final long serialVersionUID = 1L;
	private Units unit;
	private Power owner;
	
	public Box() {
	}
	
	/**
	 * @return {@link data.unit.Units Unit} contains in this {@link data.boxes.Box Box}.
	 */
	public Units getUnit() {
		return unit;
	}
	
	/**
	 * @return {@link data.Power Power} that represent this {@link data.boxes.Box Box}.
	 */
	public Power getOwner() {
		return owner;
	}
	
	/**
	 * @return true if a {@link data.Power Power} represents this {@link data.boxes.Box Box}.
	 */
	public boolean hasOwner() {
		return owner != null;
	}
	
	/**
	 * @return true if a {@link data.unit.Units Unit} is contained in this {@link data.boxes.Box Box}.
	 */
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

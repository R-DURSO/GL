package data.boxes;

import data.Power;
import data.building.Building;
import data.unit.Units;

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

	public void setUnit(Units unit) {
		this.unit = unit;
	}

	public void setOwner(Power owner) {
		this.owner = owner;
	}

	public String toString() {
		String s = "";
		s += " Unit=" + unit;
		if (hasOwner()) {
			s+= ", Owner="+ owner.getName();
		}
		s+="\n";
		return s;
	}
}

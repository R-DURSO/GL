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

	public void setUnit(Units unit) {
		this.unit = unit;
	}

	public void setOwner(Power owner) {
		this.owner = owner;
	}
	
	public abstract Building getBuilding();
	public abstract void setBuilding(Building building);

	public String toString() {
		String s = "";
		s += " Unit=" + unit;
		if (owner!=null) {
			s+= ", Owner="+ owner.getName();
		}
		s+="\n";
		return s;
	}
	

}

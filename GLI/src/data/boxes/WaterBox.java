package data.boxes;

import data.building.Building;

public class WaterBox extends Box{

	public WaterBox() {
		super();
	}

	public String toString() {
		return "WaterBox " + super.toString();
	}

	public Building getBuilding() {
		return null;
	}

	public void setBuilding(Building building) {}

}

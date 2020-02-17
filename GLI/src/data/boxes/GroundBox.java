package data.boxes;

import data.building.Building;
import data.ressource.ResourceTypes;
import data.unit.Units;

public class GroundBox extends Box{
	
	private Building building;
	private int resourceType;
	
	public GroundBox(int resourceType) {
		super();
		this.resourceType = resourceType;
	}

	public Building getBuilding() {
		return building;
	}

	public int getResourceType() {
		return resourceType;
	}
	
	public String getResourceTypeName() {
		switch (resourceType) {
		case ResourceTypes.NO_RESOURCE:
			return "Void";
		case ResourceTypes.RESOURCE_FOOD:
			return "Food";
		case ResourceTypes.RESOURCE_GOLD:
			return "Gold";
		case ResourceTypes.RESOURCE_STONE:
			return "Stone";
		case ResourceTypes.RESOURCE_WOOD:
			return "Wood";
		default:
			return "unknown";
		}
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Override
	public String toString() {
		return "GroundBox : ressource = " + getResourceTypeName() + " " + super.toString();
	}
	
	

}

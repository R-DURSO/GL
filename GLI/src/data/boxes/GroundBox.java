package data.boxes;

import data.building.Building;
import data.resource.ResourceTypes;
import data.unit.Units;

/**
 * <p>{@link data.boxes.Box Boxes} that are made of land.</p>
 * <p>Can contain {@link data.resource.Resource Resources}, which can be Exploited by {@link data.Power Power} with {@link data.building.BuildingProduct BuildingProduct}.</p>
 * <p>Even if there isn't a Resource, can have diverse {@link data.building.Building Buildings} to help develop your Power ingame.</p>
 * @author Maxence
 */
public class GroundBox extends Box{
	
	private Building building;
	private int resourceType;
	
	public GroundBox(int resourceType) {
		super();
		this.resourceType = resourceType;
	}
	
	public boolean hasBuilding() {
		return building != null; 
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
			return "Pas de resource";
		case ResourceTypes.RESOURCE_FOOD:
			return "Nourriture";
		case ResourceTypes.RESOURCE_GOLD:
			return "Or";
		case ResourceTypes.RESOURCE_STONE:
			return "Pierre";
		case ResourceTypes.RESOURCE_WOOD:
			return "Bois";
		case ResourceTypes.RESOURCE_ARTIFACT:
			return "Artéfact";
		default:
			return "unknown";
		}
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Override
	public String toString() {
		if (hasBuilding()) {
			return "GroundBox : building = " + getBuilding().getClass().getSimpleName()
					+ ", ressource = " + getResourceTypeName() + " " + super.toString();
		}
		return "GroundBox : building = none , ressource = " + getResourceTypeName() 
			+ " " + super.toString();
	}
	
	

}

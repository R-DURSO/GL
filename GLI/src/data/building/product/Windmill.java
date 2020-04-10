package data.building.product;

import java.io.Serializable;

import data.building.BuildingTypes;
import data.resource.ResourceTypes;

/**
 * <p>Mine is a {@link data.building.product.BuildingProduct Building} that product Food each Turn for a Power</p>
 * @author Maxence HENNEKEIN
 */

public class Windmill extends BuildingProduct implements Serializable {
	private static final int BUILD_TIME = 1;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	public static final int PRODUCTION_PER_TURN = 20;
	
	public Windmill() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getCost() {
		return COST;
	}
	
	public int getProductionPerTurn() {
		return PRODUCTION_PER_TURN;
	}

	public int getProductionType() {
		return ResourceTypes.RESOURCE_FOOD;
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_WINDMILL;
	}
	
	public String toString() {
		return "Moulin "+super.toString();
	}
}

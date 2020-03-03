package data.building.product;

import data.building.BuildingTypes;
import data.resource.ResourceTypes;
import process.visitor.building_visitor.BuildingVisitor;

/**
 * <p>Mine is a Building that product Wood each Turn for a Power</p>
 * @author Maxence HENNEKEIN
 */

public class Sawmill extends BuildingProduct {
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	private static final int PRODUCTION_PER_TURN = 20;
	
	
	public Sawmill() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getCost() {
		return COST;
	}
	
	public int getProductionPerTurn() {
		return PRODUCTION_PER_TURN;
	}

	public int getProductionType() {
		return ResourceTypes.RESOURCE_WOOD;
	}
	

	@Override
	public int getType() {
		return BuildingTypes.BUILDING_SAWMILL;
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}
	
	public String toString() {
		return "Sawmill";
	}
}

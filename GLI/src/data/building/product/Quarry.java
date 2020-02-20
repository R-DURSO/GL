package data.building.product;

import data.building.BuildingTypes;
import data.resource.ResourceTypes;
import process.visitor.building_visitor.BuildingVisitor;

public class Quarry extends BuildingProduct{
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	private static final int PRODUCTION_PER_TURN = 20;
	
	public Quarry() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getCost() {
		return COST;
	}
	
	public int getRevenue() {
		return PRODUCTION_PER_TURN;
	}
	
	public int getProductionTypes() {
		return ResourceTypes.RESOURCE_STONE;
	}
	
	@Override
	public int getType() {
		return BuildingTypes.BUILDING_QUARRY;
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}
	
	public String toString() {
		return "Quarry";
	}
}

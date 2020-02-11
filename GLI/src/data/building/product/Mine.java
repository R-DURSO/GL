package data.building.product;

import data.building.Building;
import process.visitor.building_visitor.BuildingVisitor;

public class Mine  extends BuildingProduct{
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	private static final int PRODUCTION_PER_TURN = 20;
	
	
	public Mine() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

}

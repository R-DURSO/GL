package data.building.army;

import data.building.Building;
import process.visitor.building_visitor.BuildingVisitor;

public class Stable extends BuildingArmy {
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Stable() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

}

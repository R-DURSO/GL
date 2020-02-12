package data.building.army;

import process.visitor.building_visitor.BuildingVisitor;

public class Barrack extends BuildingArmy {
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Barrack() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

}
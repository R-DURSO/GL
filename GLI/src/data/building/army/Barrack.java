package data.building.army;

import data.building.BuildingTypes;
import process.visitor.building_visitor.BuildingVisitor;

public class Barrack extends BuildingArmy {
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Barrack() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int getType() {
		return BuildingTypes.BUILDING_BARRACK;
	}

}
package data.building.special;

import data.building.BuildingTypes;
import process.visitor.building_visitor.BuildingVisitor;

public class Door extends BuildingSpecial{
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Door() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	

	@Override
	public int getType() {
		return BuildingTypes.BUILDING_DOOR;
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

}

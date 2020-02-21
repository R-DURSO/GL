package data.building.special;

import data.building.BuildingTypes;
import process.visitor.building_visitor.BuildingVisitor;

public class Wall extends BuildingSpecial{
	private static final int BUILD_TIME = 0;
	public static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Wall() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	

	@Override
	public int getType() {
		return BuildingTypes.BUILDING_WALL;
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

}

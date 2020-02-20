package data.building.special;

import data.building.BuildingTypes;
import process.visitor.building_visitor.BuildingVisitor;

public class Capital extends BuildingSpecial {
	private static final int BUILD_TIME = 0;
	private static final int COST = 0;
	private static final int BASE_HEALTH = 3;
	
	/*Specific to Capital: level system with gold requirements, each level will increase ressources production of the Capital
 		==> will produce all ressources
 	At each level, ressources production will be like 
 						production = base_production * level
 	*/
	private int level = 1;
	private static final int MAX_LEVEL = 5;
	private static final int COST_LEVEL_2 = 50;
	private static final int COST_LEVEL_3 = 150;
	private static final int COST_LEVEL_4 = 300;
	private static final int COST_LEVEL_5 = 3000;
	
	private static final int PRODUCTION_PER_TURN = 20; 
	
	
	public Capital() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	

	@Override
	public int getType() {
		return BuildingTypes.BUILDING_CAPITAL;
	}
	
	@Override
	public <B> B accept(BuildingVisitor<B> visitor) {
		return visitor.visit(this);
	}

}

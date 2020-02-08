package data.building.special;


public class Wall extends BuildingSpecial{
	private static final int BUILD_TIME = 0;
	private static final int COST = 100;
	private static final int BASE_HEALTH = 3;
	
	public Wall() {
		super(BUILD_TIME, BASE_HEALTH);
	}

}

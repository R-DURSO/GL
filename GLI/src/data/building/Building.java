package data.building;

/**
 * <p>A Building, set in the Map, own by a Power</p>
 * A Building can be:
 * <ul>
 * 	<li>A {@link data.building.product.BuildingProduct BuildingProduct}</li>
 * 	<li>A {@link data.building.army.BuildingArmy BuildingArmy}</li>
 * 	<li>A {@link data.building.special.BuildingSpecial BuildingSpecial}</li>
 * </ul>
 * @author Maxence
 */
public abstract class Building {
	
	private int buildTime; //when buildTime == 0, this building can be used
	private int health;
	
	public Building(int buildTime, int health) {
		this.buildTime = buildTime;
		this.health = health;
	}
		
	public int getBuildTime() {
		return buildTime;
	}

	public int getHealth() {
		return health;
	}
	
	public abstract int getType();

	public void decreaseBuildTime() {
		buildTime--;
	}

	public void applyDamage(int damage) {
		this.health -= damage;
	}
}
package data.building;

import java.io.Serializable;

/**
 * <p>A Building, set in the {@link data.GameMap Map}, own by a {@link data.Power Power}</p>
 * A Building can be:
 * <ul>
 * 	<li>A {@link data.building.product.BuildingProduct BuildingProduct}</li>
 * 	<li>A {@link data.building.army.BuildingArmy BuildingArmy}</li>
 * 	<li>A {@link data.building.special.BuildingSpecial BuildingSpecial}</li>
 * </ul>
 * @author Maxence
 */
public abstract class Building implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7993547681005871558L;
	public static final int BUILDING_DEFENSE = 8;
	public static final int CONSTRUCTION_DEFENSE = 4;
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
	
	public int getDefense() {
		if (isFinish()) {
			return BUILDING_DEFENSE;
		}
		else {
			return CONSTRUCTION_DEFENSE;
		}
	}
	
	public abstract int getType();
	
	/**
	 * Reduce the BuildTime in the Building (can't go lower than 0)
	 */
	public void decreaseBuildTime() {
		this.buildTime--;
		if (this.buildTime <= 0) {
			this.buildTime = 0;
		}
	}
	
	/**
	 * @return true when the Building is Constucted
	 */
	public boolean isFinish() {
		return this.buildTime == 0;
	}
	
	/**
	 * Receive damage from an attack
	 * @param damage, the number of damage taken
	 */
	public void applyDamage(int damage) {
		this.health -= damage;
		if (this.health <= 0) {
			this.health = 0;
		}
	}
	
	/**
	 * @return true if Health of the Building is 0
	 */
	public boolean isDestroyed() {
		return this.health == 0;
	}
	
	public String toString() {
		return "["+health+"hp]";
	}
}
package data.building.special;

import java.io.Serializable;

import data.building.BuildingTypes;

/**
 * <p>Capital is a {@link data.building.BuildingSpecial BuildingSpecial},
 * <br>If a Capital is destroyed, the {@link data.Power Power} that hold the Capital lose</p>
 * @author Maxence HENNEKEIN
 */

public class Capital extends BuildingSpecial implements Serializable {
	private static final int BUILD_TIME = 0;
	public static final int COST = 0;
	private static final int BASE_HEALTH = 50;
	
	/*Specific to Capital: level system with gold requirements, each level will increase ressources production of the Capital
 		==> will produce all ressources
 	At each level, ressources production will be like 
 						production = base_production * level
 	*/
	private int level = 1;
	public static final int MAX_LEVEL = 4;
	//costs of level up (gold)
	public static final int COST_LEVEL_2 = 200;
	public static final int COST_LEVEL_3 = 1000;
	public static final int COST_LEVEL_4 = 2000;
	
	
	public Capital() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_CAPITAL;
	}

	public int getLevel() {
		return this.level;
	}
	
	public int getUpgradeCost () {
		if (getLevel() < MAX_LEVEL) {
			switch(getLevel()) {
			case 1:
				return COST_LEVEL_2;
			case 2:
				return COST_LEVEL_3;
			case 3:
				return COST_LEVEL_4;
			}
		}
		return 0;
	}
	
	/**
	 * Upgrade the {@link data.building.special.Capital Capital} the {@link data.Power Power} hold
	 * @return true if upgrade was successful
	 */
	public boolean upgrade() {
		if (getLevel() < MAX_LEVEL) {
			this.level ++;
			this.applyDamage(-20);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "Capitale (niveau " + getLevel() + ") "+super.toString();
	}


}

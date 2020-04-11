package data.building.special;

import java.io.Serializable;

import data.building.BuildingTypes;

/**
 * <p>The Temple is a BuildingSpecial that will spawn at the center of the map</p>
 * <p>If a has bought and build the Temple, he instantly win.</p>
 * @author Maxence HENNEKEIN
 */

public class Temple extends BuildingSpecial implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8925248627917507732L;
	private static final int BUILD_TIME = 7;
	private static final int BASE_HEALTH = 3;
	public static final int COST = 100;
	
	public Temple() {
		super(BUILD_TIME, BASE_HEALTH);
	}
	
	public int getType() {
		return BuildingTypes.BUILDING_TEMPLE;
	}
	
	
	public String toString() {
		return "Temple "+super.toString();
	}

}
package data.resource;

import data.InitialValue;

/**
 * <p>Wood represents the number of Wood a player hold or the Resource on a Box.</p>
 * <p>Wood is used for construction</p>
 * @author Maxence HENNEKEIN
 */

public class Wood extends Resource {
	int variation () {
		return InitialValue.NUMBER_INITIAL_VALUE;
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_WOOD;
	}
	
}

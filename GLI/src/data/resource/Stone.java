package data.resource;

import data.InitialValue;

/**
 * <p>Stone represents the number of Stone a player hold or the Resource on a Box.</p>
 * <p>Stone is used for construction</p>
 * @author Maxence HENNEKEIN
 */

public class Stone extends Resource {
	int variation () {
		return InitialValue.NUMBER_INITIAL_VALUE;
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_STONE;
	}
}

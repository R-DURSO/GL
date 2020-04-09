package data.resource;

import java.io.Serializable;

/**
 * <p>Wood represents the number of Wood a player hold or the Resource on a Box.</p>
 * <p>Wood is used for construction</p>
 * @author Maxence HENNEKEIN
 */

public class Wood extends Resource implements Serializable {
	public Wood(int number) {
		super(number);
	}

	public Wood(int woodInitialValue, int woodBaseProduction) {
		super(woodInitialValue, woodBaseProduction);
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_WOOD;
	}

}

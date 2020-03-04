package data.resource;



/**
 * <p>Wood represents the number of Wood a player hold or the Resource on a Box.</p>
 * <p>Wood is used for construction</p>
 * @author Maxence HENNEKEIN
 */

public class Wood extends Resource {
	public Wood(int number) {
		super(number);
	}

	public int getResourceType() {
		return ResourceTypes.RESOURCE_WOOD;
	}

}

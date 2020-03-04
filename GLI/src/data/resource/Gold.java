package data.resource;


/**
 * <p>Gold represents the number of Gold a player hold or the Resource on a Box.</p>
 * <p>It is mainly used for training Units</p>
 * @author Maxence HENNEKEIN
 */

public class Gold extends Resource {
	public Gold(int number) {
		super(number);
	}

	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_GOLD;
	}

}

package data.resource;



/**
 * <p>Stone represents the number of Stone a player hold or the Resource on a Box.</p>
 * <p>Stone is used for construction</p>
 * @author Maxence HENNEKEIN
 */

public class Stone extends Resource {
	public Stone(int number) {
		super(number);
	}

	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_STONE;
	}
	

}

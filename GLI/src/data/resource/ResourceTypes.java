package data.resource;

import java.io.Serializable;

/**
 * <p>Various static final value are stocked here.</p>
 * @author Maxence HENNEKEIN
 */

public class ResourceTypes implements Serializable {
	
	public static final int NUMBER_TYPE_RESOURCES = 5;
	public static final int NUMBER_PLAYER_RESOURCES = 6;
	
	public static final int NO_RESOURCE = 0;
	public static final int RESOURCE_FOOD = 1;
	public static final int RESOURCE_GOLD = 2;
	public static final int RESOURCE_STONE = 3;
	public static final int RESOURCE_WOOD = 4;

	
	public static final int RESOURCE_ACTIONS = 5;
	public static final int RESOURCE_SCORE = 6;
	public static final int RESOURCE_ARTIFACT = 7;
	
	
	private ResourceTypes() {}
}

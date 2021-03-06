package data.resource;

import java.io.Serializable;

/**
 * <p>Food represents the number of Food a player hold or the Resource on a Box.</p>
 * <p>Going in negative value result in a malus.</p>
 * @author Maxence HENNEKEIN
 */

public class Food extends Resource implements Serializable {
	public Food(int number) {
		super(number);
	}

	public Food(int foodInitialValue, int foodBaseProduction) {
		super(foodInitialValue, foodBaseProduction);
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_FOOD;
	}

}

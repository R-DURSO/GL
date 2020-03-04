package data.resource;

<<<<<<< HEAD
=======
import data.InitialValue;
>>>>>>> refs/remotes/origin/master

/**
 * <p>Food represents the number of Food a player hold or the Resource on a Box.</p>
 * <p>Going in negative value result in a malus.</p>
 * @author Maxence HENNEKEIN
 */

public class Food extends Resource {
	public Food(int number) {
		super(number);
	}

	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_FOOD;
	}
	
<<<<<<< HEAD

=======
>>>>>>> refs/remotes/origin/master
}

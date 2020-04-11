package data.resource;

import java.io.Serializable;

/**
 * <p>Gold represents the number of Gold a player hold or the Resource on a Box.</p>
 * <p>It is mainly used for training Units</p>
 * @author Maxence HENNEKEIN
 */

public class Gold extends Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5364934948044929199L;


	public Gold(int number) {
		super(number);
	}

	public Gold(int goldInitialValue, int goldBaseProduction) {
		super(goldInitialValue, goldBaseProduction);
	}


	public int getResourceType() {
		return ResourceTypes.RESOURCE_GOLD;
	}

}

package data.resource;

import java.io.Serializable;

/**
 * <p>Stone represents the number of Stone a player hold or the Resource on a Box.</p>
 * <p>Stone is used for construction</p>
 * @author Maxence HENNEKEIN
 */

public class Stone extends Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2466313571166110422L;

	public Stone(int number) {
		super(number);
	}

	public Stone(int stoneInitialValue, int stoneBaseProduction) {
		super(stoneInitialValue, stoneBaseProduction);
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_STONE;
	}

}

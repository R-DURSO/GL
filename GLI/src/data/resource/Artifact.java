package data.resource;

import java.io.Serializable;

/**
 * <p>Artifact is at the center of the Map.</p>
 * <p>The only Action possible is to build the Temple, leading to Victory</p>
 * @author Maxence HENNEKEIN
 */

public class Artifact extends Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6338465849189355452L;

	public Artifact() {
		super();
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_ARTIFACT;
	}
	
	public String toString() {
		return "Artéfact";
	}

}

package data.resource;

/**
 * <p>Artifact is at the center of the Map.</p>
 * <p>The only Action possible is to build the Temple, leading to Victory</p>
 * @author Maxence HENNEKEIN
 */

public class Artifact extends Resource {
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

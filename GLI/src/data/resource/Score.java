package data.resource;

/**
 * <p>Score represents the number of score a player have.</p>
 * <p>It can be gain by Combat and territory size.</p>
 * <p>Bot may act differently depending on your Score</p>
 * @author Maxence HENNEKEIN
 */

public class Score extends Resource {
	public Score(){
		super();
	}

	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_SCORE;
	}

}

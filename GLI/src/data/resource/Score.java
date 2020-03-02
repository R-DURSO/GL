package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

/**
 * <p>Score represents the number of score a player have.</p>
 * <p>It can be gain by Combat and territory size.</p>
 * <p>Bot may act differently depending on your Score</p>
 * @author Maxence HENNEKEIN
 */

public class Score extends Resource {
	int variation () {
		return 0;
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_SCORE;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}

}

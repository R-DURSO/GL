package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

/**
 * <p>ActionPoints represents the number of action a player can do each turn.</p>
 * <p>Useable Action can be seen in "data.actions"</p>
 * @author Maxence HENNEKEIN
 */

public class ActionPoints extends Resource {
	private static final int ACTIONS_NUMBER= 3;
	private static final int MAX_ACTIONS = 6 ;
	public ActionPoints() {
		super(ACTIONS_NUMBER);
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_ACTIONS;
	}
	
	int variation () {
		return 0;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

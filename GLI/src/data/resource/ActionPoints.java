package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

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

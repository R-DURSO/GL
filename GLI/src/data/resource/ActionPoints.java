package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

public class ActionPoints extends Resource {
	private int action_Number = 3; 
	private static final int MAX_ACTIONS = 6 ; 
	
	int variation () {
		return 0;
	}

	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

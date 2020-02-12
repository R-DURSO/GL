package data.ressource;

import process.visitor.ressource_visitor.RessourceVisitor;

public class ActionPoints extends Resource {
	int variation () {
		return 0;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

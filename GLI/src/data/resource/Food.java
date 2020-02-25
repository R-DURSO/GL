package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

public class Food extends Resource {
	int variation () {
		return 0;
	}

	public int getResourceType() {
		return ResourceTypes.RESOURCE_FOOD;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

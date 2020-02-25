package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

public class Stone extends Resource {
	int variation () {
		return 0;
	}

	public int getResourceType() {
		return ResourceTypes.RESOURCE_STONE;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

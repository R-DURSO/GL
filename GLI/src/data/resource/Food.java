package data.resource;

import data.InitialValue;
import process.visitor.ressource_visitor.RessourceVisitor;

public class Food extends Resource {
	int variation () {
		return InitialValue.NUMBER_INITIAL_VALUE;
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_FOOD;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

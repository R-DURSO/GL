package data.resource;

import data.InitialValue;
import process.visitor.ressource_visitor.RessourceVisitor;

/**
 * <p>Food represents the number of Food a player hold or the Resource on a Box.</p>
 * <p>Going in negative value result in a malus.</p>
 * @author Maxence HENNEKEIN
 */

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

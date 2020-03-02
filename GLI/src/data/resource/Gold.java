package data.resource;
import data.InitialValue;
import process.visitor.ressource_visitor.RessourceVisitor;

/**
 * <p>Gold represents the number of Gold a player hold or the Resource on a Box.</p>
 * <p>It is mainly used for training Units</p>
 * @author Maxence HENNEKEIN
 */

public class Gold extends Resource {
	int variation () {
		return InitialValue.NUMBER_INITIAL_VALUE ;
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_GOLD;
	}
	
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

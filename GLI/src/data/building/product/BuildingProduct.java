package data.building.product;
import java.io.Serializable;

import data.building.Building;
import data.resource.Resource;

/**
 * <p>Main class for any {@link data.building.Building Building} that generate Resource.</p>
 * <p>A building define for production will call this class</p>
 * <ul> There is 4 type of BuildingProduct
 * 		<li>{@link data.building.product.Mine Mine}</li>
 * 		<li>{@link data.building.product.Quarry Quarry}</li>
 * 		<li>{@link data.building.product.Sawmill Sawmill}</li>
 * 		<li>{@link data.building.product.Windmill Windmill}</li>
 * </ul>
 * @author Maxence HENNEKEIN
 */

public abstract class BuildingProduct extends Building implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5822960656666498271L;
	private boolean onRightResource;

	public BuildingProduct(int buildTime, int health) {
		super(buildTime, health);
	}

	public boolean isOnRightResource(Resource resource) {
		if (getProductionType() == resource.getResourceType()) {
			this.onRightResource = true;
		}
		else {
			this.onRightResource = false;
		}
		return onRightResource;
	}
	
	public boolean isOnRightResource(int resourceType) {
		if (getProductionType() == resourceType) {
			this.onRightResource = true;
		}
		else {
			this.onRightResource = false;
		}
		return onRightResource;
	}
	
	public void setOnRightResource (boolean TrueOrFalse) {
		this.onRightResource = TrueOrFalse;
	}

	public boolean getOnRightResource () {
		return onRightResource;
	}
	
	public abstract int getProductionPerTurn();
	public abstract int getProductionType();

}

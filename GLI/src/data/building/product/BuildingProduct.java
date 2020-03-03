package data.building.product;
import data.building.Building;
import data.resource.Resource;

/**
 * <p>Main class for any Building that generate Resource.</p>
 * <p>A building define for production will call this class</p>
 * @author Maxence HENNEKEIN
 */

public abstract class BuildingProduct extends Building{
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

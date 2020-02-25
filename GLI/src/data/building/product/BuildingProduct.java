package data.building.product;
import data.building.Building;
import data.resource.Resource;

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
	
	public abstract int getProductionPerTurn();
	public abstract int getProductionType();

}

package data.building.product;
import data.building.Building;

public abstract class BuildingProduct extends Building{
	private boolean onRightResource;

	public BuildingProduct(int buildTime, int health) {
		super(buildTime, health);
	}

	public boolean isOnRightResource() {
		return onRightResource;
	}
	
	public void setIsOnRightResource(boolean onRightResource) {
		this.onRightResource = onRightResource;
	}
	
	public abstract int getProductionPerTurn();
	public abstract int getProductionType();

}

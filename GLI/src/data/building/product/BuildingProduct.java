package data.building.product;
import data.building.Building;

public abstract class BuildingProduct extends Building{

	public BuildingProduct(int buildTime, int health) {
		super(buildTime, health);
	}

	public abstract int getRevenue();
	public abstract int getProductionTypes();
	
}

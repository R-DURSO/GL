package data.resource;

import java.io.Serializable;

/**
 * <p>Resource have all methods applicable to infant classes.</p>
 * <p>You can change the amount or productionPerTurn of a Resource here.</p>
 * @author Maxence HENNEKEIN
 */

public abstract class Resource implements Serializable {
	private int amount;
	private int productionPerTurn = 0;
	
	
	public Resource () {
		this.amount = 0;
	}
	
	public Resource(int baseAmount) {
		this.amount = baseAmount;
	}
	
	public Resource(int baseAmount, int baseProduction) {
		this.amount = baseAmount;
		this.productionPerTurn = baseProduction;
	}
	
	
	public int getAmount() {
		return amount;
	}
	
	public int getProductionPerTurn() {
		return productionPerTurn;
	}
	
	public void addValue(int value) {
		this.amount += value;
	}

	public void subValue(int value) {
		this.amount -= value;
	}
	
	/**
	 * Apply the Production of Selected Resource
	 */
	public void productionOfTurn() {
		addValue(this.productionPerTurn);
	}
	
	public void addProductionPerTurn(int production) {
		this.productionPerTurn += production;
	}
	
	public void subProductionPerTurn(int production) {
		this.productionPerTurn -= production;
	}

	public abstract int getResourceType();

	public static String getResourceType(int ResourceType) {
		switch(ResourceType) {
		case ResourceTypes.RESOURCE_ACTIONS:
			return "Actions";
		case ResourceTypes.RESOURCE_FOOD:
			return "Food";
		case ResourceTypes.RESOURCE_WOOD:
			return "Wood";
		case ResourceTypes.RESOURCE_STONE:
			return "Stone";
		case ResourceTypes.RESOURCE_GOLD:
			return "Gold";
		case ResourceTypes.RESOURCE_SCORE:
			return "Score";
		case ResourceTypes.RESOURCE_ARTIFACT:
			return "Artifact";
		default:
			return "Unknown";
		}
	}
	
}

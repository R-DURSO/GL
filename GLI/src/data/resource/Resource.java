package data.resource;

/**
 * <p>Resource have all methods applicable to infant classes.</p>
 * <p>You can change the amount or productionPerTurn of a Resource here.</p>
 * @author Maxence HENNEKEIN
 */

public abstract class Resource {
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
		this.amount += this.productionPerTurn;
	}
	
	public void addProductionPerTurn(int production) {
		this.productionPerTurn += production;
	}
	
	public void subProductionPerTurn(int production) {
		this.productionPerTurn -= production;
	}

	public abstract int getResourceType();
}

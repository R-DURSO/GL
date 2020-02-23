package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

public abstract class Resource {
	private int amount = 0;
	private int productionPerTurn;
	
	
	
	public Resource () {
		productionPerTurn = 0;
	}
	
	public Resource(int productionPerTurn) {
		this.productionPerTurn = productionPerTurn;
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
	
	public void addProductionPerTurn(int production) {
		this.productionPerTurn += production;
	}
	
	public void substractProductionPerTurn(int production) {
		this.productionPerTurn -= production;
	}
	
	public abstract <R> R accept(RessourceVisitor<R> visitor);
}

package data.resource;

import data.InitialValue;
import process.visitor.ressource_visitor.RessourceVisitor;

public abstract class Resource {
	private int amount = InitialValue.NUMBER_INITIAL_VALUE;
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

	public void subValue(int value) {
		this.amount -= value;
	}
	
	public void productionOfTurn() {
		this.amount += this.productionPerTurn;
	}
	
	public void addProductionPerTurn(int production) {
		this.productionPerTurn += production;
	}
	
	public void subProductionPerTurn(int production) {
		this.productionPerTurn -= production;
	}
	
	public abstract <R> R accept(RessourceVisitor<R> visitor);
	public abstract int getResourceType();
}

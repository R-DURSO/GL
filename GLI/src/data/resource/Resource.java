package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

public abstract class Resource {
	private int amount = 0;
	
	//int variation (int nombre, <T> Ressource)
	// On pourrait le formuler comme ça ?
	
	
	public void addValue(int value) {
		this.amount += value;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public abstract <R> R accept(RessourceVisitor<R> visitor);
}

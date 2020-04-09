package data.resource;

import java.io.Serializable;

/**
 * <p>Create a Tax of a set amount with a Resource.</p>
 * <p>Used when a player has to pay a fee</p>
 * @author Maxence HENNEKEIN
 */

public class ResourceCost implements Serializable {
	private int cost;
	private int type;

	public ResourceCost(int cost, int type) {
		this.cost = cost;
		this.type = type;
	}

	public int getCost() {
		return cost;
	}

	public int getType() {
		return type;
	}
}

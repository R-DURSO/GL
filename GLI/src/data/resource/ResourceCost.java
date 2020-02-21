package data.resource;

public class ResourceCost {
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

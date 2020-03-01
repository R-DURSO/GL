package data;

import java.util.ArrayList;
import data.boxes.Box;
import data.resource.*;
import process.management.ResourcesFactory;;

public class Power {
	private String name;
	private ArrayList<Box> territory = new ArrayList<>();
	private Resource[] resources = new Resource[ResourceTypes.NUMBER_PLAYER_RESOURCES];
	private Power ally;
	
	public Power(String name) {
		this.name = name;
		resources[ResourceTypes.RESOURCE_ACTIONS - 1] = ResourcesFactory.createAction();
		resources[ResourceTypes.RESOURCE_FOOD - 1] = ResourcesFactory.createFood();
		resources[ResourceTypes.RESOURCE_GOLD - 1] = ResourcesFactory.createGold();
		resources[ResourceTypes.RESOURCE_WOOD - 1] = ResourcesFactory.createWood();
		resources[ResourceTypes.RESOURCE_STONE - 1] = ResourcesFactory.createStone();
		resources[ResourceTypes.RESOURCE_SCORE - 1] = ResourcesFactory.createScore();
	}
	
	public String getName() {
		return name;
	}
	
	public Resource[] getResources() {
		return resources;
	}
	
	public Resource getResource(int resourceType) {
		return resources[resourceType - 1];
	}
	
	public int getResourceAmount(int resourceType) {
		return resources[resourceType-1 ].getAmount();
	}
	
	public boolean isAllied() {
		return ally != null;
	}
	
	public Power getAlly() {
		return ally;
	}
	
	public int getResourceProductionPerTurn(int resourceType) {
		return resources[resourceType-1].getProductionPerTurn();
	}
	
	public void addResourcesProductionPerTurn(int resourceType, int amount) {
		resources[resourceType-1].addProductionPerTurn(amount);
	}
	
	public void substractResourcesProductionPerTurn(int resourceType, int amount) {
		resources[resourceType-1].substractProductionPerTurn(amount);
	}

	public void setAlly(Power ally) {
		this.ally = ally;
	}
	
	public void removeActionPoint() {
		resources[ResourceTypes.RESOURCE_ACTIONS - 1].addValue(-1);
	}
	
	public void addBox(Box box) {
		this.territory.add(box);
	}
	
	public void removeBox(Box box) throws IllegalArgumentException{
		if(! this.territory.remove(box))
			throw new IllegalArgumentException("La case n'appartient pas à " + getName());
	}
	
	public ArrayList<Box> getTerritory() {
		return territory;
	}
	
	public String toString() {
		return "Power "+name+": ";
	}
}

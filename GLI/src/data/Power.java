package data;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public Resource getResource(int resourceType) {
		return resources[resourceType];
	}
	
	public void setAllied(Power ally) {
		this.ally = ally;
	}
	
	public boolean isAllied() {
		return ally != null;
	}
	
	public Power getAllied() {
		return ally;
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
}

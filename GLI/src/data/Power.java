package data;

import java.util.ArrayList;
import data.boxes.*;
import data.resource.*;
import data.building.special.Capital;
import process.management.ResourcesFactory;;

/**
 * <p>Represents a player in the Game.</p>
 * <p>A Power hold resource, that can be spend on construction of {@link data.building.Building Building} or the creation of {@link data.unit.Units Units}.</p>
 * <p>A Power also hold a {@link data.building.special.Capital Capital}, and letting it be destroyed mean losing the game.</p>
 * <p>Power are created by a {@link process.management.PowerFactory PowerFactory}.</p>
 * @author Maxence
 */
public class Power {
	
	private String name;
	private ArrayList<Box> territory = new ArrayList<>();
	private Resource[] resources = new Resource[ResourceTypes.NUMBER_PLAYER_RESOURCES];
	private Power ally;
	private Capital capital;
	
	public Power(String name) {
		this.name = name;
		resources[ResourceTypes.RESOURCE_ACTIONS - 1] = ResourcesFactory.createAction();
		resources[ResourceTypes.RESOURCE_FOOD - 1] = ResourcesFactory.createFood();
		resources[ResourceTypes.RESOURCE_GOLD - 1] = ResourcesFactory.createGold();
		resources[ResourceTypes.RESOURCE_WOOD - 1] = ResourcesFactory.createWood();
		resources[ResourceTypes.RESOURCE_STONE - 1] = ResourcesFactory.createStone();
		resources[ResourceTypes.RESOURCE_SCORE - 1] = ResourcesFactory.createScore();
	}
	
	public Power(String name, Capital capital) {
		this(name);
		this.capital = capital;
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
	
	/**
	 * @return true is Power has an Ally
	 */
	public boolean isAllied() {
		return ally != null;
	}
	
	/**
	 * @return	<ul>
	 * 				<li>the Ally of Power</li>
	 * 				<li>null if Power isn't Allied</li>
	 * 			</ul>
	 * @see isAllied()
	 */
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
		resources[resourceType-1].subProductionPerTurn(amount);
	}
	
	public void applyProductionOfTurn() {
		resources[ResourceTypes.RESOURCE_ACTIONS - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_FOOD - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_GOLD - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_WOOD - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_STONE - 1].productionOfTurn();
	}

	public void setAlly(Power ally) {
		this.ally = ally;
	}
	
	public void removeAlly() {
		this.ally = null;
	}
	
	public void removeActionPoint() {
		resources[ResourceTypes.RESOURCE_ACTIONS - 1].addValue(-1);
	}
	
	public boolean canPlay() {
		return resources[ResourceTypes.RESOURCE_ACTIONS - 1].getAmount() > 0;
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
	
	public Capital getCapital() {
		return this.capital;
	}
	
	public String toString() {
		return "Power "+name+": ";
	}
}

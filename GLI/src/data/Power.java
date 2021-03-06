package data;

import java.io.Serializable;
import java.util.ArrayList;
import data.boxes.*;
import data.resource.*;
import data.building.special.Capital;
import process.management.ResourcesFactory;;

/**
 * <p>Represents a player in the {@link process.game.GameLoop Game}.</p>
 * <p>A Power hold {@link data.resource.Resource Resource}, that can be spend on construction of {@link data.building.Building Building} or the creation of {@link data.unit.Units Units}.</p>
 * <p>A Power also hold a {@link data.building.special.Capital Capital}, and letting it be destroyed mean losing the game.</p>
 * <p>Power are created by a {@link process.management.PowerFactory PowerFactory}, and made verification in {@link process.management.PowerManager PowerManager}</p>
 * @author Maxence
 */
public class Power implements Serializable {
	
	private String name;
	private ArrayList<Box> territory = new ArrayList<>();
	private Resource[] resources = new Resource[ResourceTypes.NUMBER_PLAYER_RESOURCES];
	private Power ally;
	private Capital capital;
	private boolean isAlive;
	
	private int aiLevel;
	
	private int territorySize;
	private int numberUnits;
	private int numberBuildings;
	
	public Power(String name, int aiLevel, Capital capital) {
		this.name = name;
		this.capital = capital;
		this.aiLevel = aiLevel;
		this.isAlive = true;
		createResources();
	}
	
	/**
	 * @return true if {@link data.Power Power} can still make Action
	 */
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Set false the {@link data.Power#isAlive() isAlive} boolean, making the {@link data.Power Power} unable to act.
	 */
	public void die() {
		isAlive = false;
	}

	public int getTerritorySize() {
		return territorySize;
	}

	public int getNumberUnits() {
		return numberUnits;
	}

	public int getNumberBuildings() {
		return numberBuildings;
	}

	public void setTerritorySize(int territorySize) {
		this.territorySize = territorySize;
	}

	public void setNumberUnits(int numberUnits) {
		this.numberUnits = numberUnits;
	}

	public void setNumberBuildings(int numberBuildings) {
		this.numberBuildings = numberBuildings;
	}
	
	private void createResources() {
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
	
	public int getAILevel() {
		return aiLevel;
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

	public void subResourcesProductionPerTurn(int resourceType, int amount) {
		resources[resourceType-1].subProductionPerTurn(amount);
	}
	
	public void addScore(int amount) {
		resources[ResourceTypes.RESOURCE_SCORE-1].addValue(amount);
	}
	public void subScore(int amount) {
		resources[ResourceTypes.RESOURCE_SCORE-1].subValue(amount);
	}
	
	/**
	 * Make {@link data.Power Power} gain the {@link data.building.product.BuildingProduct Production} from all his {@link data.resource.Resource Resources}
	 */
	public void applyProductionOfTurn() {
		resources[ResourceTypes.RESOURCE_ACTIONS - 1].productionOfTurn();
		if (resources[ResourceTypes.RESOURCE_ACTIONS - 1].getAmount() > ((ActionPoints) resources[ResourceTypes.RESOURCE_ACTIONS - 1]).getMaxActions()) {
			int max = ((ActionPoints) resources[ResourceTypes.RESOURCE_ACTIONS - 1]).getMaxActions();
			int actual = resources[ResourceTypes.RESOURCE_ACTIONS - 1].getAmount();
			int excess = actual - max;
			resources[ResourceTypes.RESOURCE_ACTIONS - 1].subValue(excess);
		}
		resources[ResourceTypes.RESOURCE_FOOD - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_GOLD - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_WOOD - 1].productionOfTurn();
		resources[ResourceTypes.RESOURCE_STONE - 1].productionOfTurn();
		//special production for score, we add as value the size of territory
		resources[ResourceTypes.RESOURCE_SCORE - 1].addValue(getTerritorySize());
	}

	public void setAlly(Power ally) {
		this.ally = ally;
	}
	
	public void removeAlly() {
		this.ally = null;
	}
	
	/**
	 * Make the {@link data.Power Power} lose an {@link data.resource.ActionPoints ActionPoints},
	 * <br>this method should be called once an {@link data.action.Action Action} has been added to {@link process.game.GameLoop GameLoop}
	 */
	public void removeActionPoint() {
		resources[ResourceTypes.RESOURCE_ACTIONS - 1].subValue(1);
	}
	
	/**
	 * Make the {@link data.Power Power} lose several {@link data.resource.ActionPoints ActionPoints}
	 * @param number of ActionPoints to lose
	 * @see {@link data.Power#removeActionPoint() removeActionPoint}
	 */
	public void removeActionPoints (int number) {
		resources[ResourceTypes.RESOURCE_ACTIONS - 1].subValue(number);
	}
	
	/**
	 * <p>Unique to the {@link data.Power Player}, make sure he has enough {@link data.resource.ActionPoints ActionPoints}.
	 * <br>AI are limited in different ways, so they can always play.</p>
	 * @return true if Player can play or if it's an AI
	 * @see {@link data.Power#isAI() isAI()}
	 */
	public boolean canPlay() {
		if (!isAI()) {
			return resources[ResourceTypes.RESOURCE_ACTIONS - 1].getAmount() > 0;
		}
		else {
			return true;
		}
	}
	
	/**
	 * @return true if {@link data.power Power} is an AI
	 */
	public boolean isAI() {
		return this.aiLevel >= 0;
	}
	
	public void addBox(Box box) {
		this.territory.add(box);
	}
	
	public void removeBox(Box box) throws IllegalArgumentException{
		if(! this.territory.remove(box))
			throw new IllegalArgumentException("La case n'appartient pas � " + getName());
	}
	
	public ArrayList<Box> getTerritory() {
		return territory;
	}
	
	/**
	 * @return Capital that the Power hold
	 */
	public Capital getCapital() {
		return this.capital;
	}
	
	/**
	 * @return true if Capital of the Power is destroid
	 */
	public boolean hasLost() {
		return this.capital.isDestroyed();
	}
	
	public String toString() {
		return "Puissance : "+name+" ";
	}

}

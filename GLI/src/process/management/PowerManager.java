package process.management;

import data.building.special.Capital;
import data.resource.ResourceTypes;
import data.unit.Boat;
import data.unit.Units;

import java.util.ArrayList;
import java.util.Iterator;

import data.GameConstants;
import data.GameMap;
import data.Power;
import data.ScoreValue;
import data.boxes.Box;
import data.boxes.GroundBox;
import log.LoggerUtility;
import org.apache.log4j.Logger;

/**
 * Manager of 
 * @author Maxence
 */
public class PowerManager {
	private static PowerManager instance = new PowerManager();

	public static PowerManager getInstance() {
		return instance;
	}

	private static Logger Logger = LoggerUtility.getLogger(PowerManager.class, GameConstants.LOG_TYPE);

	/**
	 * Refreshes power's number buildings, units & territory size
	 * 
	 * @param powerConcerned the power who needs to be refreshed
	 * @param powers         all other powers in the game
	 * @param map
	 */
	public void refreshPowerStats(Power powerConcerned, GameMap map) {
		int mapSize = map.getSize();
		int numberBuildings = 0, numberUnits = 0, territorySize = 0;
		// we will iterate through all map and increase values depending on which box we
		// are
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				Box box = map.getBox(i, j);

				// check if there is any unit on the box
				if (box.hasUnit()) {
					// check who own those units
					Units units = box.getUnit();
					if (powerConcerned == units.getOwner()) {
						numberUnits += units.getNumber();
						if(units instanceof Boat) { //boats can have units inside
							Boat boat = (Boat)units;
							if(boat.hasContainedUnits()) {
								Units containedUnits = boat.getContainedUnits();
								numberUnits += containedUnits.getNumber();
							}
							
						}
					}
				}

				// check if powerConcerned owns this box
				if (box.getOwner() != null) {
					if (powerConcerned == box.getOwner()) {
						territorySize++;
						// we also check if this box has a building : if yes, it belongs to powerConcerned
						if (box instanceof GroundBox && ((GroundBox) box).hasBuilding())
							numberBuildings++;
					}
				}
			}
		}

		// we refresh values for powerConcerned
		powerConcerned.setNumberBuildings(numberBuildings);
		powerConcerned.setNumberUnits(numberUnits);
		powerConcerned.setTerritorySize(territorySize);
	}

	/**
	 * Create an alliance with those 2 powers, making attack and conquer of
	 * territory unavaible
	 * 
	 * @param power1 the power that want to launch the alliance
	 * @param power2 the power that will become allied
	 */
	public void makeAlliance(Power power1, Power power2) {
		power1.setAlly(power2);
		power2.setAlly(power1);
		Logger.info(power1.getName() + " is now allied with " + power2);
	}

	/**
	 * Break the alliance set by those 2 powers, making attack and conquer avaible
	 * again
	 * 
	 * @param power that doesn't want to be allied anymore
	 */
	public void breakAlliance(Power power) {
		Power power2 = power.getAlly();
		power.removeAlly();
		power2.removeAlly();
		Logger.info(power.getName() + " is no longer allied with " + power2);

		ArrayList<Box> boxToGain = new ArrayList<Box>();

		for (Iterator<Box> i = power.getTerritory().iterator(); i.hasNext();) {
			Box visitBox = i.next();
			if (visitBox.hasUnit()) {
				if (visitBox.getUnit().getOwner() == power2) {
					boxToGain.add(visitBox);
				}
			}
		}
		for (Iterator<Box> i = boxToGain.iterator(); i.hasNext();) {
			Box visitBox = i.next();
			visitBox.setOwner(power2);
			power2.addBox(visitBox);
			power.removeBox(visitBox);
		}
		Logger.info(power2.getName() + " gain " + boxToGain.size() + "Box from breaking the alliance");

		boxToGain.clear();

		for (Iterator<Box> i = power2.getTerritory().iterator(); i.hasNext();) {
			Box visitBox = i.next();
			if (visitBox.hasUnit()) {
				if (visitBox.getUnit().getOwner() == power) {
					boxToGain.add(visitBox);
				}
			}
		}
		for (Iterator<Box> i = boxToGain.iterator(); i.hasNext();) {
			Box visitBox = i.next();
			visitBox.setOwner(power);
			power.addBox(visitBox);
			power2.removeBox(visitBox);
		}
		Logger.info(power.getName() + " gain " + boxToGain.size() + "Box from breaking the alliance");
	}

	public void regainFood(Power powerConcerned) {
		// get the actual production
		int foodProd = powerConcerned.getResource(ResourceTypes.RESOURCE_FOOD).getProductionPerTurn();
		// iterate through all his territory to find a unit to delete
		Iterator<Box> i = powerConcerned.getTerritory().iterator();
		while ((i.hasNext()) && (foodProd < 0)) {
			Box visitBox = i.next();
			if (visitBox.hasUnit()) {
				int unitToRemove = - ((foodProd / visitBox.getUnit().getFoodCost()) + 5);
				UnitManager.getInstance().removeUnits(powerConcerned, visitBox, unitToRemove);
				foodProd = powerConcerned.getResource(ResourceTypes.RESOURCE_FOOD).getProductionPerTurn();
			}
		}
		// As the hunger ends, we regain some food
		powerConcerned.getResource(ResourceTypes.RESOURCE_FOOD).productionOfTurn();
	}

	/**
	 * As the conquer grow, only the strongest remain
	 * 
	 * @param killer, the one who will prevail
	 * @param killed, those who have failed
	 */
	public void killPower(Power killer, Power killed) {
		// get all territory of the defeated
		killer.getTerritory().addAll(killed.getTerritory());
		for (Iterator<Box> i = killed.getTerritory().iterator(); i.hasNext();) {
			Box visitBox = i.next();
			visitBox.setOwner(killer);
		}
		killed.getTerritory().clear();
		// receive all production
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD,
				killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD));
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_WOOD,
				killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_WOOD));
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_GOLD,
				killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_GOLD));
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_STONE,
				killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_STONE));
		// reveive half of his current ressource
		killer.getResource(ResourceTypes.RESOURCE_FOOD)
				.addValue(killed.getResource(ResourceTypes.RESOURCE_FOOD).getAmount() / 2);
		killer.getResource(ResourceTypes.RESOURCE_WOOD)
				.addValue(killed.getResource(ResourceTypes.RESOURCE_WOOD).getAmount() / 2);
		killer.getResource(ResourceTypes.RESOURCE_GOLD)
				.addValue(killed.getResource(ResourceTypes.RESOURCE_GOLD).getAmount() / 2);
		killer.getResource(ResourceTypes.RESOURCE_STONE)
				.addValue(killed.getResource(ResourceTypes.RESOURCE_STONE).getAmount() / 2);
		// Receive score from the dead
		killer.addScore(killed.getResource(ResourceTypes.RESOURCE_ACTIONS).getAmount() / 4);
		killer.addScore(ScoreValue.SCORE_VALUE_POWER);
		Logger.info(killer.getName()+" has kill "+killed.getName());
		Logger.info("== "+killed.getName()+" is DEAD ==");


		killed.die();
	}
	
	public Power[] recreatePowerList(Power[] powerList) {
		int numberPower = 0;
		int i;
		for (i = 0; i < powerList.length; i++) {
			if (!powerList[i].hasLost()) {
				numberPower++;
			}
		}
		i = 0;
		String namePowerLeft = "";
		Power[] newPowerList = new Power[numberPower];
		while ((numberPower > 0) && (i < powerList.length)) {
			if (!powerList[i].hasLost()) {
				newPowerList[numberPower-1] = powerList[i];
				namePowerLeft += powerList[i].getName()+" ";
				numberPower--;
			}
			i++;
		}
		Logger.info(namePowerLeft);
		return newPowerList;
	}

}

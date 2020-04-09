package process.management;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import data.GameConstants;
import data.GameMap;
import data.Position;
import data.Power;
import data.actions.Action;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.BuildingArmy;
import data.resource.Resource;
import data.resource.ResourceTypes;
import data.unit.BatteringRam;
import data.unit.Boat;
import data.unit.Infantry;
import data.unit.UnitTypes;
import data.unit.Units;
import exceptions.WrongActionException;
import log.LoggerUtility;

/**
 * Manages and decide behavior of powers who are non-human, depending of their
 * difficulty
 * 
 * @author Aldric Vitali Silvestre
 * @see Power
 */
public class AIManager {

	private static Logger logger = LoggerUtility.getLogger(AIManager.class, GameConstants.LOG_TYPE);

	private GameMap map;
	private Power[] powers;
	private ActionValidator actionValidator;
	private Random random = new Random();

	// change those percentage if necessary
	// ALL ACTIONS CHANCES MUST BE == 100
	private final int ACTION_ATTACK_CHANCE = 23;
	private final int ACTION_CONSTRUCT_CHANCE = 25;
	private final int ACTION_CREATE_UNIT_CHANCE = 20;
	private final int ACTION_MOVE_CHANCE = 30;
	private final int ACTION_MAKE_ALLIANCE_CHANCE = 2;

	// never touch that
	private final int ATTACK_THRESHOLD = ACTION_ATTACK_CHANCE;
	private final int CONSTRUCT_THRESHOLD = ATTACK_THRESHOLD + ACTION_CONSTRUCT_CHANCE;
	private final int CREATE_UNIT_THRESHOLD = CONSTRUCT_THRESHOLD + ACTION_CREATE_UNIT_CHANCE;
	private final int MOVE_THRESHOLD = CREATE_UNIT_THRESHOLD + ACTION_MOVE_CHANCE;
	private final int MAKE_ALLIANCE_THRESHOLD = MOVE_THRESHOLD + ACTION_MAKE_ALLIANCE_CHANCE;

	/**
	 * Creates a new AIManager, and links the GameMap
	 * 
	 * @param map the map of the game
	 */
	public AIManager(GameMap map, Power powers[]) {
		this.map = map;
		this.powers = powers;
		actionValidator = new ActionValidator(map);
	}

	/**
	 * Create actions that Power can do, depending of his AI level
	 * 
	 * @param power the power who will do those actions
	 */
	public void doActions(Power power) {
		logger.info("AI " + power.getName() + " starts creating actions");

		// get all units
		ArrayList<Units> unitsList = getUnits(power);

		// get all buildings
		ArrayList<Building> buildingList = getBuildings(power);

		// get all territory
		ArrayList<Box> territory = power.getTerritory();

		// actions that power will do will be there
		Resource actionResource = power.getResource(ResourceTypes.RESOURCE_ACTIONS);
		int numberActions = actionResource.getAmount();

		// we will try a lot more actions than really do them
		int numberActionsToTry = numberActions * 5;

		Action triedActions[] = tryActions(power, numberActionsToTry, unitsList, buildingList, territory);

		// finally, get actions that power will really do
		Action turnActions[] = getDesiredActions(power, triedActions, numberActions, unitsList,
				buildingList, territory);
	}

	/**
	 * It will try to do actions, and keep those that are valid, until he has had
	 * enough.
	 * 
	 * @param power              the power who will do actions
	 * 
	 * @param numberActionsToTry the number of actions to return in the
	 *                           {@code Action} array
	 * @param territory          power territory
	 * @param buildingList       power's buildings
	 * @param unitsList          power's units
	 * @return actions that passed the {@code ActionValidator} test
	 */
	private Action[] tryActions(Power power, int numberActionsToTry, ArrayList<Units> unitsList,
			ArrayList<Building> buildingList, ArrayList<Box> territory) {

		Action actionsTried[] = new Action[numberActionsToTry];

		int numberActionsTried = 0;
		Action action;

		while (numberActionsTried < numberActionsToTry) { // until actionsTried not completed
			// first, we want to know which action can he try
			int actionThreshold = random.nextInt(MAKE_ALLIANCE_THRESHOLD + 1);

			try {
				if (actionThreshold < ATTACK_THRESHOLD) {
					action = tryCreateActionAttack(power, unitsList, buildingList, territory);
				} else if (actionThreshold < CONSTRUCT_THRESHOLD) {
					action = tryCreateActionConstruct(power, unitsList, buildingList, territory);
				} else if (actionThreshold < CREATE_UNIT_THRESHOLD) {
					action = tryCreateActionCreateUnit(power, unitsList, buildingList, territory);
				} else if (actionThreshold < MOVE_THRESHOLD) {
					action = tryCreateActionMove(power, unitsList, buildingList, territory);
				} else {
					action = tryCreateActionMakeAlliance(power, unitsList, buildingList, territory);
				}
				logger.info("Action " + action.getClass().getSimpleName() + " created");
			} catch (WrongActionException e) {
				logger.warn("Action denied : " + e.getMessage());
			}
		}
		return actionsTried;
	}

	private Action tryCreateActionMakeAlliance(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		
		//if has already an allied, no need to continue
		if(power.getAlly() != null)
			throw new WrongActionException("this powre is already in alliance");
		
		//if it is a 2 players game, no alliance is allowed
		int numberPlayers = powers.length;
		if(numberPlayers <= 2)
			throw new WrongActionException("only 2 powers are fighting");
		
		//depending on the AI level, choice will be different
		int aiLevel = power.getAILevel();
		switch (aiLevel) {
		case GameConstants.AI_EASY:
			//easy ai won't do any alliance
			throw new WrongActionException("easy AI don't make alliance");

		case GameConstants.AI_NORMAL:
			//normal ai can do alliance with anyone
			//so we have to chose randomly one of others 
			int potentialPowerIndex = random.nextInt(numberPlayers);
			Power potentialAlly = powers[potentialPowerIndex];
			try {
				return actionValidator.createActionMakeAlliance(power, potentialAlly);
			}catch(IllegalArgumentException e){
				throw new WrongActionException("invalid alliance");
			}
			
		case GameConstants.AI_HARD:
			//hard ai want to ally only with the best player (with the biggest score)
			//so we have to find it
			Power bestPower = powers[0];
			int bestScore = bestPower.getResourceAmount(ResourceTypes.RESOURCE_SCORE);
			int tmpScore;
			for(int i = 1; i < numberPlayers; i++) {
				tmpScore = powers[i].getResourceAmount(ResourceTypes.RESOURCE_SCORE);
				if(tmpScore > bestScore) {// if equals, we let player before be "the best" (in order to favor the real player AKA powers[0])
					bestScore = tmpScore;
					bestPower = powers[i];
				}
			}
			
			try {
				return actionValidator.createActionMakeAlliance(power, bestPower);
			}catch(IllegalArgumentException e){
				throw new WrongActionException("invalid alliance");
			}	
			
		default:
			throw new WrongActionException("invlaid ai level");
		}
		
	}

	private Action tryCreateActionMove(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		// TODO Auto-generated method stub
		return null;
	}

	private Action tryCreateActionCreateUnit(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		int aiLevel = power.getAILevel();
		
		
		ArrayList<BuildingArmy> armyBuildingList = new ArrayList<>();
		if(armyBuildingList.isEmpty())
			throw new WrongActionException("No army building");
		
		
		for(Building building : buildingList){
			if(building instanceof BuildingArmy)
				armyBuildingList.add((BuildingArmy)building);
		}
		
		int numberBuilding = armyBuildingList.size();
		int buildingIndex = random.nextInt(numberBuilding);
		BuildingArmy buildingSelected = armyBuildingList.get(buildingIndex);
		Position buildingPosition = getBuildingPosition(buildingSelected);
		if(buildingPosition == null)
			throw new WrongActionException("building Position can't be retrieved");
		
		int unitsType = UnitTypes.UNIT_INFANTRY;
		int numberUnits = 0;
		
		switch (buildingSelected.getType()) {
		case BuildingTypes.BUILDING_BARRACK:
			if(aiLevel == GameConstants.AI_EASY) {
				unitsType = UnitTypes.UNIT_INFANTRY;
				numberUnits = Infantry.NUMBER_MAX_UNITS;
			}else {
				unitsType = random.nextInt(UnitTypes.UNITS_IN_BARRACK);
			}
			break;

		case BuildingTypes.BUILDING_DOCK:
			if(aiLevel == GameConstants.AI_EASY)
				throw new WrongActionException("easy AI don't create boats");
			else {
				unitsType = UnitTypes.UNIT_BOAT;
				numberUnits = Boat.NUMBER_MAX_UNITS;
			}
			break;
			
		case BuildingTypes.BUILDING_WORKSHOP:
			if(aiLevel == GameConstants.AI_EASY) {
				unitsType = UnitTypes.UNIT_BATTERING_RAM;
				numberUnits = BatteringRam.NUMBER_MAX_UNITS;
			}else {
				unitsType = random.nextInt(UnitTypes.UNITS_IN_DOCK - UnitTypes.UNITS_IN_DOCK + 1) + UnitTypes.UNITS_IN_DOCK;
			}
			break;
		default:
			throw new WrongActionException("invalid building type");
		}

		try {
			return actionValidator.createActionCreateUnit(power, unitsType, numberUnits, buildingPosition);
		}catch (IllegalArgumentException e) {
			throw new WrongActionException("invalid unit creation");
		}
	}

	private Position getBuildingPosition(BuildingArmy buildingSelected) {
		for(int i = 0; i < map.getSize(); i++){
			for(int j = 0; j < map.getSize(); j++){
				Box tmpBox = map.getBox(i, j);
				if(tmpBox instanceof GroundBox) {
					GroundBox gBox = (GroundBox)tmpBox;
					if(gBox.hasBuilding() && gBox.getBuilding() == buildingSelected)
						return new Position(i, j);
				}
			}
		}
		return null;
	}

	private Action tryCreateActionConstruct(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		// TODO Auto-generated method stub
		return null;
	}

	private Action tryCreateActionAttack(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * actions passed will be rated, and only part of them will be returned,
	 * depending on power ai level.
	 * 
	 * @param power         the power who will do actions
	 * 
	 * @param triedActions  Actions that are valid
	 * @param numberActions the number of actions returned
	 * @param territory     power territory
	 * @param buildingList  power's buildings
	 * @param unitsList     power's units
	 * @return an array of actions
	 */
	private Action[] getDesiredActions(Power power, Action[] triedActions, int numberActions, ArrayList<Units> unitsList, ArrayList<Building> buildingList, ArrayList<Box> territory) {
		// we have some actions to prioritize in order to have a legit intelligence, so,
		// we check here if some things are done before
		return null;
	}

	/**
	 * Recover all {@linkplain data.unit.Units} that Power have
	 * 
	 * @param power
	 * @return the Units list
	 */
	private ArrayList<Units> getUnits(Power power) {
		ArrayList<Units> unitsList = new ArrayList<>();

		int mapSize = map.getSize();
		// check through map to recover all units
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				Box box = map.getBox(i, j);
				if (box.hasUnit()) {
					Units units = box.getUnit();
					if (units.getOwner() == power)
						unitsList.add(units);
				}
			}
		}

		return unitsList;
	}

	/**
	 * Recover all {@linkplain data.building.Building} that Power have
	 * 
	 * @param power
	 * @return the Building list
	 */
	private ArrayList<Building> getBuildings(Power power) {
		ArrayList<Building> buildingList = new ArrayList<>();

		int mapSize = map.getSize();
		// check through map to recover all units
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				Box box = map.getBox(i, j);
				// we must check if box is a ground box (no building can be on water box)
				if (box instanceof GroundBox) {
					GroundBox groundBox = (GroundBox) box;
					if (groundBox.hasBuilding() && groundBox.getOwner() == power) { // if has building, he have owner
						Building building = groundBox.getBuilding();
						buildingList.add(building);
					}
				}
			}
		}

		return buildingList;
	}

}

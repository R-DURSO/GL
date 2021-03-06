package process.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import data.GameConstants;
import data.GameMap;
import data.Position;
import data.Power;
import data.boxes.*;
import data.actions.*;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.*;
import data.building.product.*;
import data.building.special.*;
import data.resource.*;
import data.unit.*;

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
	private final int ACTION_ATTACK_CHANCE = 25;
	private final int ACTION_CONSTRUCT_CHANCE = 15;
	private final int ACTION_CREATE_UNIT_CHANCE = 20;
	private final int ACTION_MOVE_CHANCE = 38;
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
	 * @return 
	 */
	public Action[] doActions(Power power) {
		logger.info("AI " + power.getName() + " starts creating actions");

		// get all units
		ArrayList<Units> unitsList = getUnits(power);

		// get all buildings
		ArrayList<Building> buildingList = getBuildings(power);

		// get all territory
		ArrayList<Box> territory = power.getTerritory();
		
		// we will try a lot more actions than really do them
		int numberActionsPossible = power.getResourceAmount(ResourceTypes.RESOURCE_ACTIONS);
		int numberActionsToTry = numberActionsPossible * (2 + power.getCapital().getLevel());
		Action triedActions[] = null;
		triedActions = tryActions(power, numberActionsToTry, unitsList, buildingList, territory);
		
		// finally, get actions that are valid
		Action turnActions[] = new Action[numberActionsPossible];
		int numberValidAction = 0;
		
		for (int i = 0; ((i < triedActions.length)
						&& (numberValidAction < numberActionsPossible)); i++) {
			if (triedActions[i] != null) {
				//ad the Action to be done
				turnActions[numberValidAction] = triedActions[i];
				numberValidAction++;
			}
		}
		//lose those Action Points
		power.removeActionPoints(numberValidAction);
		logger.info(power.getName()+" use "+numberValidAction+" ActionPoints this turn\n");
		return turnActions;
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
		
		//we first want to know if power can upgrade his capital
		action = tryCreateActionUpgradeCapital(power);
		//if action == null, power can't upgrade
		//else, it means that already 1 action is set
		if(action != null) {
			actionsTried[numberActionsTried] = action;
			numberActionsTried++;
		}

		while (numberActionsTried < numberActionsToTry) { // until actionsTried not completed
			// first, we want to know which action can he try
			int actionThreshold = random.nextInt(MAKE_ALLIANCE_THRESHOLD) + 1;

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
				logger.debug("Action " + action.getClass().getSimpleName() + " created");
				actionsTried[numberActionsTried] = action;
			} catch (WrongActionException e) {
				logger.debug("Action denied : " + e.getMessage());
			}
			numberActionsTried++;
		}
		return actionsTried;
	}

	private Action tryCreateActionUpgradeCapital(Power power) {
		try {
			return actionValidator.createActionUpgradeCapital(power);
		} catch (IllegalArgumentException e) {
			logger.debug("can't upgrade capital : " + e.getMessage());
			return null;
		}
	}

	private Action tryCreateActionMakeAlliance(Power power, ArrayList<Units> unitsList,
			ArrayList<Building> buildingList, ArrayList<Box> territory) throws WrongActionException {

		// if has already an allied, no need to continue
		if (power.getAlly() != null)
			throw new WrongActionException("this powre is already in alliance");

		// if it is a 2 players game, no alliance is allowed
		int numberPlayers = powers.length;
		if (numberPlayers <= 2)
			throw new WrongActionException("only 2 powers are fighting");

		// depending on the AI level, choice will be different
		int aiLevel = power.getAILevel();
		switch (aiLevel) {
		case GameConstants.AI_EASY:
			// easy ai won't do any alliance
			throw new WrongActionException("easy AI don't make alliance");

		case GameConstants.AI_NORMAL:
			// normal ai can do alliance with anyone
			// so we have to chose randomly one of others
			int potentialPowerIndex = random.nextInt(numberPlayers);
			Power potentialAlly = powers[potentialPowerIndex];
			try {
				return actionValidator.createActionMakeAlliance(power, potentialAlly);
			} catch (IllegalArgumentException e) {
				throw new WrongActionException("invalid alliance");
			}

		case GameConstants.AI_HARD:
			// hard ai want to ally only with the best player (with the biggest score)
			// so we have to find it
			Power bestPower = powers[0];
			int bestScore = bestPower.getResourceAmount(ResourceTypes.RESOURCE_SCORE);
			int tmpScore;
			for (int i = 1; i < numberPlayers; i++) {
				tmpScore = powers[i].getResourceAmount(ResourceTypes.RESOURCE_SCORE);
				if (tmpScore > bestScore) {// if equals, we let player before be "the best" (in order to favor the real
											// player AKA powers[0])
					bestScore = tmpScore;
					bestPower = powers[i];
				}
			}

			try {
				return actionValidator.createActionMakeAlliance(power, bestPower);
			} catch (IllegalArgumentException e) {
				throw new WrongActionException("invalid alliance");
			}

		default:
			throw new WrongActionException("invlaid ai level");
		}

	}

	private Action tryCreateActionCreateUnit(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		int aiLevel = power.getAILevel();

		// we have to count each buildingArmy that power owns
		if (buildingList.isEmpty())
			throw new WrongActionException(power.getName() + " doesn't have any building");

		ArrayList<BuildingArmy> armyBuildingList = new ArrayList<>();
		for (Building building : buildingList) {
			if (building instanceof BuildingArmy)
				armyBuildingList.add((BuildingArmy) building);
		}

		// if empty, power can't create units
		if (armyBuildingList.isEmpty())
			throw new WrongActionException("No army building");

		int numberBuilding = armyBuildingList.size();
		int buildingIndex = random.nextInt(numberBuilding);
		BuildingArmy buildingSelected = armyBuildingList.get(buildingIndex);
		Position buildingPosition = getBuildingPosition(buildingSelected);
		if (buildingPosition == null)
			throw new WrongActionException("building Position can't be retrieved");

		int unitsType = UnitTypes.UNIT_INFANTRY;
		int numberUnits = 0;

		// depending on building type, actions will change
		switch (buildingSelected.getType()) {
		// easy ai levels have very limited options
		case BuildingTypes.BUILDING_BARRACK:
			if (aiLevel == GameConstants.AI_EASY) {
				unitsType = UnitTypes.UNIT_INFANTRY;
				numberUnits = Infantry.NUMBER_MAX_UNITS;
			} else {
				unitsType = random.nextInt(UnitTypes.UNITS_IN_BARRACK - 1) + 1;
				// will find number of units to be created depending on ai level and Units
				numberUnits = findNumberUnits(power, unitsType, aiLevel);
			}
			break;

		case BuildingTypes.BUILDING_DOCK:
			if (aiLevel == GameConstants.AI_EASY)// easy ai won't create docks, so this check is just here to be safe
				throw new WrongActionException("easy AI don't create boats");
			else {
				unitsType = UnitTypes.UNIT_BOAT;
				// we always have 1 boat, and boat is important enough to skip cost constrains
				numberUnits = Boat.NUMBER_MAX_UNITS;
			}
			break;

		case BuildingTypes.BUILDING_WORKSHOP:
			if (aiLevel == GameConstants.AI_EASY) {
				unitsType = UnitTypes.UNIT_BATTERING_RAM;
				numberUnits = BatteringRam.NUMBER_MAX_UNITS;
			} else {
				unitsType = random.nextInt(UnitTypes.UNITS_IN_WORKSHOP - UnitTypes.UNITS_IN_BARRACK)
						+ UnitTypes.UNITS_IN_BARRACK;
				numberUnits = findNumberUnits(power, unitsType, aiLevel);
			}
			break;
		default:
			throw new WrongActionException("invalid building type");
		}

		try {
			return actionValidator.createActionCreateUnit(power, unitsType, numberUnits, buildingPosition);
		} catch (IllegalArgumentException e) {
			throw new WrongActionException("unitCreationFailed");
		}
	}

	/**
	 * Returns number of units that power wants to create, depending on ai level.
	 * Returns -1 if problem occurs
	 * 
	 * @param power     the power concerned
	 * @param unitsType the type of units
	 * @param aiLevel   the power ai level
	 * @return a number of units
	 * @see data.unit.UnitTypes
	 */
	private int findNumberUnits(Power power, int unitsType, int aiLevel) {
		// first, we have to find which unit it is and what are his costs
		int numberMaxUnits = Units.getNumberMaxUnits(unitsType);
		int unitsCost = Units.getUnitCost(unitsType);
		int unitsCostPerTurn = Units.getUnitCostPerTurn(unitsType);

		// we can already calculate cost of units
		int numberUnitsCreated = numberMaxUnits;
		int unitsCostMax = numberMaxUnits * unitsCost;
		int unitsCostPerTurnMax = numberMaxUnits * unitsCostPerTurn;

		// food production and amount will be important to decide number units
		int foodAmount = power.getResourceAmount(ResourceTypes.RESOURCE_FOOD);
		int foodProdPerTurn = power.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD);

		// now we have 2 differents behaviors : if ai is normal or hard (easy is not
		// counted here)
		if (aiLevel == GameConstants.AI_NORMAL) {
			/*
			 * Normal AI always want to create the maximum number of units allowed, it will
			 * count also the production per turn (he don't want to be in negative)
			 */

			while ((unitsCostMax <= foodAmount && unitsCostPerTurnMax <= foodProdPerTurn) && numberUnitsCreated > 1) {
				numberUnitsCreated--;
				unitsCostMax = numberMaxUnits * unitsCost;
				unitsCostPerTurnMax = numberMaxUnits * unitsCostPerTurn;
			}

			return numberUnitsCreated;
		} else if (aiLevel == GameConstants.AI_HARD) {
			/*
			 * Hard ai will want to create either all units, or half, or 3. production per
			 * turn can be negative (he will seek for producing food after), food production
			 * per turn after buying units should just be over -Windmill.PROD_PER_TURN and
			 * obvoiusly, he don't want to buy more units than he can
			 */

			int choiceNumberUnits = random.nextInt(3);

			switch (choiceNumberUnits) {
			case 0: // wants to create all units
				// numberUnitsCreated is already set to numberMaxUnits
				break;
			case 1: // wants to create half of units
				numberUnitsCreated = numberMaxUnits <= 2 ? 1 : numberMaxUnits / 2;
				unitsCostMax = numberMaxUnits * unitsCost;
				unitsCostPerTurnMax = numberMaxUnits * unitsCostPerTurn;
				break;
			case 2: // wants to create 3 units (if possible)
				numberUnitsCreated = numberMaxUnits < 3 ? 1 : 3;
				unitsCostMax = numberMaxUnits * unitsCost;
				unitsCostPerTurnMax = numberMaxUnits * unitsCostPerTurn;
				break;
			}

			// almost the same verification rule as normal ai, except for the foodPerTurn

			int targetedProductionPerTurn = foodProdPerTurn + Windmill.PRODUCTION_PER_TURN;
			while ((unitsCostMax <= foodAmount && unitsCostPerTurnMax <= targetedProductionPerTurn)
					&& numberUnitsCreated > 2) {
				numberUnitsCreated--;
				unitsCostMax = numberMaxUnits * unitsCost;
				unitsCostPerTurnMax = numberMaxUnits * unitsCostPerTurn;
			}

			return numberUnitsCreated;
		}

		return 0;
	}

	private Action tryCreateActionConstruct(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		int aiLevel = power.getAILevel();
		
		//for all AIs (except easy), if can construct temple, will do it
		if(aiLevel != GameConstants.AI_EASY) {
			int centerCoord = map.getSize() / 2;
			Position centerPos = new Position(centerCoord, centerCoord);
			Box templeBox = map.getBox(centerPos);
			if (templeBox.getOwner() == power) {
				int stoneAmount = power.getResourceAmount(ResourceTypes.RESOURCE_STONE);
				if (stoneAmount >= Temple.COST) {
					try {
						return actionValidator.createActionConstruct(power, BuildingTypes.BUILDING_TEMPLE, centerPos);
					} catch (IllegalArgumentException e) {
						throw new WrongActionException("can't create temple");
					}
				}
			}
		}

		// if ai is in hard level, try to create a windmill if food prod per turn < 0
		if (aiLevel == GameConstants.AI_HARD) {
			int foodProductionPerTurn = power.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD);
			if (foodProductionPerTurn < 0) {
				// we have to find if power have a box with no building and food resource
				Box foodBox = getBoxWithResource(territory, ResourceTypes.RESOURCE_FOOD);
				if (foodBox != null) {
					Position buildingPosition = getBoxPosition(foodBox);
					try {
						return actionValidator.createActionConstruct(power, BuildingTypes.BUILDING_WINDMILL,
								buildingPosition);
					} catch (IllegalArgumentException e) {
						throw new WrongActionException("can't create windmill");
					}
				}
			}
		}

		// else, choose a box where to construct (we need a ground Box)
		Box box;
		do {
			int boxIndex = random.nextInt(territory.size());
			box = territory.get(boxIndex);
		} while (box instanceof WaterBox);
		
		GroundBox groundBox = (GroundBox)box;
		
		Position boxPosition = getBoxPosition(groundBox);

		// and create a building randmomly (for easy ai),
		int buildingType = BuildingTypes.BUILDING_BARRACK; // barrack by default
		if (aiLevel == GameConstants.AI_EASY) {
			do {
				buildingType = random.nextInt(BuildingTypes.NUMBER_BUILDINGS);
			} while (buildingType == BuildingTypes.BUILDING_DOCK || buildingType == BuildingTypes.BUILDING_WALL || buildingType == BuildingTypes.BUILDING_DOOR); // easy ai don't do boat

		} else { // or depending on his infos on it (for normal and hard ai)
			// add random choice : will choose to construct a BuildingArmy/BuildingSpecial,
			// or a BuildingProduct
			int choice = random.nextInt(3);
			
			//before, check if has resource on this box ==> else, set choice to 0
			if(groundBox.getResourceType() == ResourceTypes.NO_RESOURCE)
				choice = 0;
			
			switch (choice) {
			case 0: // buildingArmy or Special
				boolean alreadyChose = false;
				// if hard ai, will try in priority to create a dock if boxPosition is near
				// water and have no other docks
				if (aiLevel == GameConstants.AI_HARD) {
					if (!haveThisBuildingType(buildingList, BuildingTypes.BUILDING_DOCK)) {
						if (map.isNearWater(boxPosition)) {
							buildingType = BuildingTypes.BUILDING_DOCK;
							alreadyChose = true;
						}
					}

					if (!alreadyChose) {
						//choose a random building (not a BuildingProduct)
						do {
							buildingType = random.nextInt(BuildingTypes.NUMBER_BUILDINGS);
						} while (buildingType > BuildingTypes.BUILDING_ARMY && buildingType < BuildingTypes.BUILDING_SPECIAL);
					}
				}
				break;
			case 1: //66% chance of creating building product
			case 2: // buildingProduct
				// check which product is on the box, and choose the correct BuildingProduct
				int resourceType = groundBox.getResourceType();
				buildingType = getBuildingFromResource(resourceType);
				break;
			}
		}

		try {
			return actionValidator.createActionConstruct(power, buildingType, boxPosition);
		} catch (IllegalArgumentException e) {
			throw new WrongActionException("Building construct failed");
		}
	}

	private Action tryConstructTemple() {
		// TODO Auto-generated method stub
		return null;
	}

	private int getBuildingFromResource(int resourceType) {
		switch (resourceType) {
		case ResourceTypes.RESOURCE_FOOD:
			return BuildingTypes.BUILDING_WINDMILL;
		case ResourceTypes.RESOURCE_GOLD:
			return BuildingTypes.BUILDING_MINE;
		case ResourceTypes.RESOURCE_STONE:
			return BuildingTypes.BUILDING_QUARRY;
		case ResourceTypes.RESOURCE_WOOD:
			return BuildingTypes.BUILDING_SAWMILL;
		default:
			return -1;
		}
	}

	/**
	 * Will try to get a box with specified resource.
	 * <ul>
	 * <li>If no box found, will returns null</li>
	 * <li>Will try to return a Box with no building, or null if no one found</li>
	 * </ul>
	 * 
	 * @param territory
	 * @param resourceType
	 * @return a box where food can be produced, else null
	 */
	private Box getBoxWithResource(ArrayList<Box> territory, int resourceType) {
		for (Box box : territory) {
			if (box instanceof GroundBox) {

			} else {// no need to check further
				continue;
			}
		}
		return null;
	}

	private Action tryCreateActionMove(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		
		int aiLevel = power.getAILevel();
		// if AI have units
		if (unitsList.isEmpty()) {
			throw new WrongActionException(power.getName() + " doesn't have any units");
		}

		// get a random units
		int numberUnits = unitsList.size();
		int unitsIndex = random.nextInt(numberUnits);
		Units unitSelected = unitsList.get(unitsIndex);
		
		//if unit is moving, cancel action
		if (unitSelected.getIsMoving()) {
			throw new WrongActionException("This Unit is Already moving");
		}
		
		Position unitPosition = getUnitsPosition(unitSelected);
		
		if (unitPosition == null) {
			throw new WrongActionException("Couldn't retrieve unit Position");
		}

		// We check all near Box that we can possibly go to
		ArrayList<Position> nearbyPosition = new ArrayList<Position>();

		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				Position boxPosition = new Position(i, j);
				if (actionValidator.isUnitsOnRange(unitPosition, unitSelected.getMovement(), boxPosition)) {
					nearbyPosition.add(boxPosition);
				}
			}
		}

		// We only take Box that we can go to
		ArrayList<Position> validPosition = new ArrayList<Position>();

		if (unitSelected.getTypes() == UnitTypes.UNIT_BOAT) {
			// Cast as a Boat
			Boat boatSelected = (Boat) unitSelected;
			// select only WaterBox, or GroundBox if hasContainedUnit
			for (Iterator<Position> i = nearbyPosition.iterator(); i.hasNext();) {
				Position visitPosition = i.next();
				Box visitBox = map.getBox(visitPosition);
				if (visitBox instanceof WaterBox) {
					validPosition.add(visitPosition);
				} else {
					// implicit GroundBox
					if (boatSelected.hasContainedUnits()) {
						// we do not check if its near Water, PathFinding will do it
						validPosition.add(visitPosition);
					}
				}
			}
		} else {
			// select only GroundBox, or WaterBox only with Boat
			for (Iterator<Position> i = nearbyPosition.iterator(); i.hasNext();) {
				Position visitPosition = i.next();
				Box visitBox = map.getBox(visitPosition);
				if (visitBox instanceof GroundBox) {
					validPosition.add(visitPosition);
				} else {
					// implicit WaterBox
					if (visitBox.hasUnit()) {
						if (visitBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
							validPosition.add(visitPosition);
						}
					}
				}
			}
		}
		
		//we will move according to our most precious Building, the capital
		Position ourCapitalPosition = getBuildingPosition(power.getCapital());
		//declaration of our Iterator
		Iterator<Position> it;
		//declaration for checking all Position
		Position visitPosition;
		//Position for quick change/verification
		Position checkPosition;
		//associated Box
		Box visitBox;
		Box checkBox;
		//declaration for score to give
		int scoreGivenToPosition = 0;
		int highestScore = scoreGivenToPosition;
		
		switch (aiLevel) {
			default: //make the default case the easy
			case GameConstants.AI_EASY:
				
				//we go straight to his Capital
				Position toGoCapital = ourCapitalPosition;
				//search for the most scored player
				for (int i = 0; i < powers.length; i++) {
					if (powers[i].getResourceAmount(ResourceTypes.RESOURCE_ACTIONS) >
							map.getBox(toGoCapital).getOwner().getResourceAmount(ResourceTypes.RESOURCE_ACTIONS)) {
						 toGoCapital = getBuildingPosition(powers[i].getCapital());
					}
				}
				
				//We will score each Position, only to keep the most important
				ArrayList<Position> toTryPosition = new ArrayList<Position>();
				highestScore = scoreGivenToPosition;
				
				for (it = validPosition.iterator(); it.hasNext(); ) {
					visitPosition = it.next();
					scoreGivenToPosition = ((50 + ((15 + map.getSize()) * map.getSize())) - (map.getDistance(visitPosition, toGoCapital) * 15));
					if (scoreGivenToPosition > highestScore) {
						//More interesting Position is here
						highestScore = scoreGivenToPosition;
						//keep some of previous seen Box, for various movement
						int maxToKeep = toTryPosition.size() / 2;
						ArrayList<Position> keepingPosition = new ArrayList<Position>();
						for (int i = 0; i < maxToKeep; i++) {
							keepingPosition.add(toTryPosition.get(i));
						}
						toTryPosition.clear();
						toTryPosition.add(visitPosition);
						toTryPosition.addAll(keepingPosition);
					}
					else if (scoreGivenToPosition == highestScore) {
						toTryPosition.add(visitPosition);
					}
					//if lower, idc
				}
				
				it = toTryPosition.iterator();
				while (it.hasNext()) {
					visitPosition = it.next();
					try {
						return actionValidator.createActionMove(power, unitPosition, visitPosition);
					}
					catch (IllegalArgumentException e) {
						//lets try until the end
					}
				}
				//if we end here, movement has Failed
				throw new WrongActionException("invalid movement unit");
			case GameConstants.AI_NORMAL:
				//HashMap that score a List of Position
				HashMap<Integer, ArrayList<Position>> listToTryPosition = new HashMap<Integer, ArrayList<Position>>();
				for (it = validPosition.iterator(); it.hasNext(); ) {
					visitPosition = it.next();
					visitBox = map.getBox(visitPosition);
					//initial score
					scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) + (map.getDistance(visitPosition, ourCapitalPosition) * 5) );
					//add bonus if we go near a Capital
					for (int i = 0; i < powers.length; i++) {
						if (powers[i].isAlive()) {
							checkPosition = getBuildingPosition(powers[i].getCapital());
							scoreGivenToPosition += ( (20 * (powers[i].getResourceAmount(ResourceTypes.RESOURCE_SCORE) / 100))
														- (map.getDistance(checkPosition, visitPosition) * 2) );
						}
					}
					//less malus if there is a Unit nearby
					if (visitBox.hasUnit()) {
						scoreGivenToPosition -= (map.getBox(visitPosition).getUnit().getNumber() * 5);
					}
					else {
						scoreGivenToPosition -= 100;
					}
					//will search for conquer
					if (visitBox.hasOwner()) {
						if (visitBox.getOwner() != power) {
							scoreGivenToPosition += 40;
						}
					}
					else {
						scoreGivenToPosition += 120;
					}
					//avoid being near water
					if (map.isNearWater(visitPosition)) {
						scoreGivenToPosition -= 20;
					}
					//Added Bonus if there is a Building
					if (visitBox instanceof GroundBox) {
						if (((GroundBox)visitBox).hasBuilding()) {
							scoreGivenToPosition += 20;
							if (((GroundBox)visitBox).getBuilding() instanceof BuildingSpecial) {
								scoreGivenToPosition += 40;
							}
						}
					}
					//now that the scoring is done, add it to the HashMap
					if (!listToTryPosition.containsKey(scoreGivenToPosition)) {
						listToTryPosition.put(scoreGivenToPosition, new ArrayList<Position>());
					}
					listToTryPosition.get(scoreGivenToPosition).add(visitPosition);
				}
				
				//try to move to all position by order of score
				while (!listToTryPosition.isEmpty()) {
					//get the highest score stored
					highestScore = 0;
					for (Iterator<Integer> ite = listToTryPosition.keySet().iterator(); ite.hasNext(); ) {
						int checkingScore = ite.next();
						if (checkingScore > highestScore) {
							highestScore = checkingScore;
						}
					}
					//we have the highest score stored
					if (highestScore <= 0) {
						listToTryPosition.clear();
					}
					if (listToTryPosition.containsKey(highestScore)) {
						for (it = listToTryPosition.get(highestScore).iterator(); it.hasNext(); ) {
							visitPosition = it.next();
							//we try to move to this position
							for (int d=0; d<=4; d++) {
								switch(d) {
								case 0:
									checkPosition = visitPosition;
									break;
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									if (validPosition.contains(checkPosition)) {
										try {
											return actionValidator.createActionMove(power, unitPosition, checkPosition);
										} catch (IllegalArgumentException e) {
											logger.warn("IA tried to move to an invalid position");
											validPosition.remove(checkPosition);
										}
									}
								}
							}
						}
						//remove highest score
						listToTryPosition.remove(highestScore);
					}
				}
				//at this Point, stop
				throw new WrongActionException("invalid unit movement");
			case GameConstants.AI_HARD:
				//HashMap that score a List of Position
				HashMap<Integer, ArrayList<Position>> listToGoPosition = new HashMap<Integer, ArrayList<Position>>();
				for (it = validPosition.iterator(); it.hasNext(); ) {
					visitPosition = it.next();
					visitBox = map.getBox(visitPosition);
					//score change depending on Units we try to move
					switch (unitSelected.getTypes()) {
						case UnitTypes.UNIT_INFANTRY:
							//stay near the Capitale
							scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) - (map.getDistance(visitPosition, ourCapitalPosition) * 60) );
							if (scoreGivenToPosition > 0) {
								//we are near Capital, check for nearby ennemy
								for (int d=0; d<=4; d++) {
									switch(d) {
									case 0:
										checkPosition = visitPosition;
										break;
									case 1:
										checkPosition = map.getUpPos(visitPosition);
										break;
									case 2:
										checkPosition = map.getLeftPos(visitPosition);
										break;
									case 3:
										checkPosition = map.getRightPos(visitPosition);
										break;
									case 4:
										checkPosition = map.getDownPos(visitPosition);
										break;
									default:
										checkPosition = null;
										break;
									}
									if (checkPosition != null) {
										checkBox = map.getBox(checkPosition);
										if (checkBox.hasUnit()) {
											//defend our Capital, dont chicken out
											scoreGivenToPosition += ((unitSelected.getHealth() * unitSelected.getNumber() * 5) - (checkBox.getUnit().getHealth() * checkBox.getUnit().getNumber() * 2));
										}
									}
								}
							}
							else {
								//we need to go closer to Capital
								scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) - (map.getDistance(visitPosition, ourCapitalPosition) * 12) );
							}
							break;
						case UnitTypes.UNIT_ARCHER:
							//stay far from other, only to attack if it's profitable
							scoreGivenToPosition = ( (((15 + map.getSize()) * map.getSize()) * (20 + map.getSize())) / (map.getDistance(visitPosition, ourCapitalPosition) + 1) );
							for (int d=0; d<=4; d++) {
								switch(d) {
								case 0:
									checkPosition = visitPosition;
									break;
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									checkBox = map.getBox(checkPosition);
									if (checkBox.hasUnit()) {
										//total health divide by distance
										scoreGivenToPosition -= ( (checkBox.getUnit().getHealth() * checkBox.getUnit().getNumber() * 5)
												/ ((map.getDistance(checkPosition, visitPosition) * 2) + 1) );
									}
								}
							}
							if (scoreGivenToPosition > 0) {
								//positive scoring, we are safe, try to see if we can attack
								if (visitBox.hasUnit()) {
									try {
										//force to take only this Unit
										ArrayList<Units> forceArrayUnit = new ArrayList<Units>();
										forceArrayUnit.add(unitSelected);
										return actionValidator.createActionAttack(power, unitPosition, visitPosition);
									}
									catch (IllegalArgumentException e) {
										//If it didn't work, forget it
									}
								}
							}
							break;
						case UnitTypes.UNIT_PIKEMAN:
							//act the same as Cavalry
						case UnitTypes.UNIT_CAVALRY:
							//they will go and conquer
							scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) + (map.getDistance(visitPosition, ourCapitalPosition) * 8) );
							for (int d=1; d<=4; d++) {
								switch(d) {
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									checkBox = map.getBox(checkPosition);
									if (checkBox.hasOwner()) {
										if (checkBox.hasUnit()) {
											//minus score if there are strong unit, but for those more frail
											scoreGivenToPosition -= checkBox.getUnit().getNumber() * (2 + (checkBox.getUnit().getDefense() - 2));
										}
									}
									else {
										//there is nearby Box that aren't controlled
										scoreGivenToPosition += 50;
									}
									if (checkBox instanceof GroundBox) {
										GroundBox checkGBox = (GroundBox)checkBox;
										if (checkGBox.getResourceType() != ResourceTypes.NO_RESOURCE) {
											//Resource are on this Box
											scoreGivenToPosition += 50;
											if (checkGBox.getResourceType() == ResourceTypes.RESOURCE_ARTIFACT) {
												//The most important objective is nearby
												scoreGivenToPosition += 500 - (50 * map.getDistance(checkPosition, visitPosition));
											}
										}
									}
								}
							}
							break;
						case UnitTypes.UNIT_TREBUCHET:
							//stay at bay, and far from war to provide maximum efficiency
							scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) - (map.getDistance(visitPosition, ourCapitalPosition) * 8) );
							for (int d=0; d<=4; d++) {
								switch(d) {
								case 0:
									checkPosition = visitPosition;
									break;
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									checkBox = map.getBox(checkPosition);
									if (checkBox.hasUnit()) {
										//Strong unit are scary (total health)
										scoreGivenToPosition -= (checkBox.getUnit().getHealth() * checkBox.getUnit().getNumber() * 5);
									}
									if (checkBox instanceof GroundBox) {
										GroundBox checkGBox = (GroundBox)checkBox;
										if (checkGBox.hasBuilding()) {
											scoreGivenToPosition += 50;
											if (checkGBox.getBuilding() instanceof BuildingSpecial) {
												scoreGivenToPosition += 200;
											}
										}
									}
									else {
										//Near water, there is less danger
										scoreGivenToPosition += 20;
									}
								}
							}
							if (visitPosition.equals(unitPosition)) {
								//Position where Trebuchet is, try to transform 
								if ((scoreGivenToPosition > 0) && (((Trebuchet)unitSelected).getState() == Trebuchet.STATE_MOVING)) {
									try {
										return actionValidator.createActionMove(power, unitPosition, visitPosition);
									}
									catch (IllegalArgumentException e) {
										//If it didn't work, log it
										logger.error(power.getName()+" try to transform his Trebuchet, only to fail");
									}
								}
							}
							break;
						case UnitTypes.UNIT_BATTERING_RAM:
							//go attack, trebuchet will do the same if a power is frail
							scoreGivenToPosition = ( (((15 + map.getSize()) * map.getSize()) * (20 + map.getSize())) / (map.getDistance(visitPosition, ourCapitalPosition) + 1) );
							for (int d=0; d<=4; d++) {
								switch(d) {
								case 0:
									checkPosition = visitPosition;
									break;
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									checkBox = map.getBox(checkPosition);
									if (checkBox.hasUnit()) {
										//total health divide by distance
										scoreGivenToPosition -= ( (checkBox.getUnit().getHealth() * checkBox.getUnit().getNumber() * 5)
												/ ( (map.getDistance(checkPosition, visitPosition) * 2) + 1) );
									}
									if (checkBox instanceof GroundBox) {
										GroundBox checkGBox = (GroundBox)checkBox;
										if (checkGBox.hasBuilding()) {
											scoreGivenToPosition +=20;
											if (checkGBox.getBuilding() instanceof BuildingSpecial) {
												scoreGivenToPosition += 50;
											}
										}
									}
									else {
										//Near water, there is less danger
										scoreGivenToPosition += 20;
									}
								}
							}
							break;
						case UnitTypes.UNIT_BOAT:
							//like to be near Water
							scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) + (map.getDistance(visitPosition, ourCapitalPosition) * 5) );
							//malus if there is a Unit nearby, Boat dont search to fight
							if (visitBox.hasUnit()) {
								scoreGivenToPosition -= (map.getBox(visitPosition).getUnit().getNumber() * 5);
							}
							if (visitBox.hasOwner()) {
								if (visitBox.getOwner() != power) {
									scoreGivenToPosition += 40;
								}
							}
							else {
								scoreGivenToPosition += 120;
							}
							//like being near water
							if (map.isNearWater(visitPosition)) {
								scoreGivenToPosition += 20;
							}
							break;
						default:
							//if it hasn't been resolved, act like a Normal AI
							scoreGivenToPosition = ( ((20 + map.getSize()) * map.getSize()) + (map.getDistance(visitPosition, ourCapitalPosition) * 5) );
							//less malus if there is a Unit nearby
							if (visitBox.hasUnit()) {
								scoreGivenToPosition -= (map.getBox(visitPosition).getUnit().getNumber() * 5);
							}
							else {
								scoreGivenToPosition -= 100;
							}
							//will search for conquer
							if (visitBox.hasOwner()) {
								if (visitBox.getOwner() != power) {
									scoreGivenToPosition += 40;
								}
							}
							else {
								scoreGivenToPosition += 120;
							}
							//avoid being near water
							if (map.isNearWater(visitPosition)) {
								scoreGivenToPosition -= 20;
							}
							//Added Bonus if there is a Building
							if (visitBox instanceof GroundBox) {
								if (((GroundBox)visitBox).hasBuilding()) {
									scoreGivenToPosition += 20;
									if (((GroundBox)visitBox).getBuilding() instanceof BuildingSpecial) {
										scoreGivenToPosition += 40;
									}
								}
							}
							break;
					}
					//more bonus if BuildingProduct or Artefact
					if (visitBox instanceof GroundBox) {
						GroundBox visitGBox = (GroundBox) visitBox;
						if (visitGBox.hasBuilding()) {
							if (visitGBox.getBuilding() instanceof BuildingProduct) {
								scoreGivenToPosition += 40;
							}
							else if (visitGBox.getBuilding() instanceof BuildingSpecial) {
								scoreGivenToPosition += 80;
							}
						}
						if (visitGBox.getResourceType() == ResourceTypes.RESOURCE_ARTIFACT) {
							scoreGivenToPosition += 200;
						}
					}
					//a note, Hard AI can try other action and return it if succesful, making sure to do the most effecient choice
					
					
					//now that the scoring is done, add it to the HashMap
					if (!listToGoPosition.containsKey(scoreGivenToPosition)) {
						listToGoPosition.put(scoreGivenToPosition, new ArrayList<Position>());
					}
					listToGoPosition.get(scoreGivenToPosition).add(visitPosition);
				}
				
				while (!listToGoPosition.isEmpty()) {
					//get the highest score stored
					highestScore = 0;
					for (Iterator<Integer> ite = listToGoPosition.keySet().iterator(); ite.hasNext(); ) {
						int checkingScore = ite.next();
						if (checkingScore > highestScore) {
							highestScore = checkingScore;
						}
					}
					//we have the highest score stored
					if (highestScore <= 0) {
						listToGoPosition.clear();
					}
					if (listToGoPosition.containsKey(highestScore)) {
						for (it = listToGoPosition.get(highestScore).iterator(); it.hasNext(); ) {
							visitPosition = it.next();
							//we try to move to this position
							if (validPosition.contains(visitPosition)) {
								try {
									return actionValidator.createActionMove(power, unitPosition, visitPosition);
								} catch (IllegalArgumentException e) {
									logger.warn("IA tried to move to an invalid position");
									validPosition.remove(visitPosition);
								}
							}
						}
						//remove highest score
						listToGoPosition.remove(highestScore);
					}
				}
				
				//at this Point, stop
				throw new WrongActionException("invalid unit movement");
				
		}
		
	}

	private Action tryCreateActionAttack(Power power, ArrayList<Units> unitsList, ArrayList<Building> buildingList,
			ArrayList<Box> territory) throws WrongActionException {
		
		int aiLevel = power.getAILevel();
		if (unitsList.isEmpty()) {
			throw new WrongActionException(power.getName() + " doesn't have any units");
		}

		// get a random units
		int numberUnits = unitsList.size();
		int unitsIndex = random.nextInt(numberUnits);
		Units unitSelected = unitsList.get(unitsIndex);
		
		if (unitSelected.getIsMoving()) {
			throw new WrongActionException("This Unit is Already moving");
		}
		
		Position unitPosition = getUnitsPosition(unitSelected);
		if (unitPosition == null) {
			throw new WrongActionException("Couldn't retrieve unit Position");
		}

		// We check all near Box that we can possibly go to
		ArrayList<Position> nearbyPosition = new ArrayList<Position>();

		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				Position boxPosition = new Position(i, j);
				if (actionValidator.isUnitsOnRange(unitPosition, unitSelected.getRange(), boxPosition)) {
					nearbyPosition.add(boxPosition);
				}
			}
		}

		// We only take Box that we can go to
		ArrayList<Position> validPosition = new ArrayList<Position>();

		for (Iterator<Position> i = nearbyPosition.iterator(); i.hasNext();) {
			Position visitPosition = i.next();
			Box visitBox = map.getBox(visitPosition);
			if (visitBox instanceof GroundBox) {
				GroundBox visitGBox = (GroundBox) visitBox;
				if (visitGBox.hasUnit()) {
					validPosition.add(visitPosition);
				}
				else if (visitGBox.hasBuilding()) {
					// A bit of Intelligence here, only attack SpecialBuilding
					if (visitGBox.getBuilding() instanceof BuildingSpecial) {
						validPosition.add(visitPosition);
					}
					else if (aiLevel == GameConstants.AI_EASY) {
						validPosition.add(visitPosition);
					}
				}
			}
			else {
				// implicit WaterBox
				if (visitBox.hasUnit()) {
					// Nearby Boat
					validPosition.add(visitPosition);
				}
			}
		}
		
		//declaration of our Iterator
		Iterator<Position> it;
		//declaration for checking all Position
		Position visitPosition;
		//Position for quick change/verification
		Position checkPosition;
		//associated Box
		Box visitBox;
		Box checkBox;
		//declaration for score to give
		int scoreGivenToPosition = 0;
		int highestScore = scoreGivenToPosition;
		
		switch (aiLevel) {
			default: //make the default case the easy
			case GameConstants.AI_EASY:
				//We will score each Position, only to keep the most important
				ArrayList<Position> toTryPosition = new ArrayList<Position>();
				
				for (it = validPosition.iterator(); it.hasNext(); ) {
					visitPosition = it.next();
					visitBox = map.getBox(visitPosition);
					scoreGivenToPosition = (unitSelected.getNumber());
					if (visitBox.hasUnit()) {
						scoreGivenToPosition += visitBox.getUnit().getHealth() * 2;
					}
					if (visitBox instanceof GroundBox) {
						if (((GroundBox) visitBox).hasBuilding()) {
							scoreGivenToPosition += 10;
						}
					}
					if (scoreGivenToPosition > highestScore) {
						//More interesting Position is here
						highestScore = scoreGivenToPosition;
						//keep some of previous seen Box
						int maxToKeep = toTryPosition.size() / 2;
						ArrayList<Position> keepingPosition = new ArrayList<Position>();
						for (int i = 0; i < maxToKeep; i++) {
							keepingPosition.add(toTryPosition.get(i));
						}
						toTryPosition.clear();
						toTryPosition.add(visitPosition);
						toTryPosition.addAll(keepingPosition);
					}
					else if (scoreGivenToPosition == highestScore) {
						toTryPosition.add(visitPosition);
					}
					//if lower, idc
				}
				
				it = toTryPosition.iterator();
				while (it.hasNext()) {
					visitPosition = it.next();
					try {
						return actionValidator.createActionAttack(power, unitPosition, visitPosition);
					}
					catch (IllegalArgumentException e) {
						logger.warn("IA tried to attack an invalid position");
					}
				}
				//if we end here, attack has Failed
				throw new WrongActionException("invalid attack");
			case GameConstants.AI_NORMAL:
				HashMap<Integer, ArrayList<Position>> listToTryPosition = new HashMap<Integer, ArrayList<Position>>();
				for (it = validPosition.iterator(); it.hasNext(); ) {
					visitPosition = it.next();
					visitBox = map.getBox(visitPosition);
					//initial score
					scoreGivenToPosition = ((20 + map.getSize()) * map.getSize());
					//less malus if there is a Unit nearby
					if (visitBox.hasUnit()) {
						scoreGivenToPosition += (map.getBox(visitPosition).getUnit().getNumber() * 5);
					}
					else {
						scoreGivenToPosition += 20;
					}
					//avoid being near water
					if (map.isNearWater(visitPosition)) {
						scoreGivenToPosition -= 10;
					}
					//Added Bonus if there is a Building
					if (visitBox instanceof GroundBox) {
						if (((GroundBox)visitBox).hasBuilding()) {
							scoreGivenToPosition += 20;
							if (((GroundBox)visitBox).getBuilding() instanceof BuildingSpecial) {
								scoreGivenToPosition += 30;
							}
						}
					}
					//now that the scoring is done, add it to the HashMap
					if (!listToTryPosition.containsKey(scoreGivenToPosition)) {
						listToTryPosition.put(scoreGivenToPosition, new ArrayList<Position>());
					}
					listToTryPosition.get(scoreGivenToPosition).add(visitPosition);
				}
				
				//try to move to all position by order of score
				while (!listToTryPosition.isEmpty()) {
					//get the highest score stored
					highestScore = 0;
					for (Iterator<Integer> ite = listToTryPosition.keySet().iterator(); ite.hasNext(); ) {
						int checkingScore = ite.next();
						if (checkingScore > highestScore) {
							highestScore = checkingScore;
						}
					}
					//we have the highest score stored
					if (highestScore <= 0) {
						listToTryPosition.clear();
					}
					if (listToTryPosition.containsKey(highestScore)) {
						for (it = listToTryPosition.get(highestScore).iterator(); it.hasNext(); ) {
							visitPosition = it.next();
							//we try to move to this position
							for (int d=0; d<=4; d++) {
								switch(d) {
								case 0:
									checkPosition = visitPosition;
									break;
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									if (validPosition.contains(checkPosition)) {
										try {
											return actionValidator.createActionMove(power, unitPosition, checkPosition);
										} catch (IllegalArgumentException e) {
											logger.warn("IA tried to attack to an invalid position");
											validPosition.remove(checkPosition);
										}
									}
								}
							}
						}
						//remove highest score
						listToTryPosition.remove(highestScore);
					}
				}
				//at this Point, stop
				throw new WrongActionException("invalid unit attack");
			case GameConstants.AI_HARD:
				//HashMap that score a List of Position
				HashMap<Integer, ArrayList<Position>> listToGoPosition = new HashMap<Integer, ArrayList<Position>>();
				for (it = validPosition.iterator(); it.hasNext(); ) {
					visitPosition = it.next();
					visitBox = map.getBox(visitPosition);
					//score change depending on Units we try to move
					//initial score
					scoreGivenToPosition = (10 + map.getSize());
					switch (unitSelected.getTypes()) {
						case UnitTypes.UNIT_INFANTRY:
							//check all attaquable Box, if there is someone attack, else move
							if (!visitBox.hasUnit()) {
								scoreGivenToPosition = 0;
							}
							break;
						case UnitTypes.UNIT_ARCHER:
							//re-check if attack is profitable, else move out of here
							scoreGivenToPosition = ((15 + map.getSize()) * map.getSize());
							for (int d=0; d<=4; d++) {
								switch(d) {
								case 0:
									checkPosition = visitPosition;
									break;
								case 1:
									checkPosition = map.getUpPos(visitPosition);
									break;
								case 2:
									checkPosition = map.getLeftPos(visitPosition);
									break;
								case 3:
									checkPosition = map.getRightPos(visitPosition);
									break;
								case 4:
									checkPosition = map.getDownPos(visitPosition);
									break;
								default:
									checkPosition = null;
									break;
								}
								if (checkPosition != null) {
									checkBox = map.getBox(checkPosition);
									if (checkBox.hasUnit()) {
										//total health divide by distance
										scoreGivenToPosition -= ( (checkBox.getUnit().getHealth() * checkBox.getUnit().getNumber() * 5)
												/ ( (map.getDistance(checkPosition, visitPosition) * 2) + 1) );
									}
								}
							}
							if (scoreGivenToPosition > 0) {
								//positive scoring, we are safe, let's attack
								if (visitBox.hasUnit()) {
									try {
										return actionValidator.createActionAttack(power, unitPosition, visitPosition);
									} catch (IllegalArgumentException e) {
										logger.warn("IA tried to attack from a good position, only to fail");
									}
								}
							}
							break;
						case UnitTypes.UNIT_PIKEMAN:
							//act the same as Cavalry
						case UnitTypes.UNIT_CAVALRY:
							//attack to conquer
								if (visitBox instanceof GroundBox) {
									GroundBox visitGBox = (GroundBox)visitBox;
									if (visitGBox.hasBuilding()) {
										if (visitGBox.getBuilding() instanceof BuildingArmy) {
											scoreGivenToPosition += 25;
										}
										else if (visitGBox.getBuilding() instanceof BuildingSpecial) {
											scoreGivenToPosition += 50;
										}
									}
								}
								if (visitBox.hasUnit()) {
									//score for attack
									scoreGivenToPosition += (50 - visitBox.getUnit().getNumber() * (2 + (visitBox.getUnit().getDefense() - 2)));
								}
							break;
						case UnitTypes.UNIT_TREBUCHET:
							//try to snipe other
							if (((Trebuchet)unitSelected).getState() == Trebuchet.STATE_INSTALLED) {
								if (visitBox.hasUnit()) {
									scoreGivenToPosition += (map.getDistance(visitPosition, visitPosition) * 10) - visitBox.getUnit().getNumber();
								}
								if (visitBox instanceof GroundBox) {
									GroundBox visitGBox = (GroundBox)visitBox;
									if (visitGBox.hasBuilding()) {
										scoreGivenToPosition += 100 - (visitGBox.getBuilding().getHealth() / 5);
									}
								}
							}
							else {
								try {
									return actionValidator.createActionMove(power, unitPosition, unitPosition);
								} catch (IllegalArgumentException e) {
									logger.warn("IA tried to change Trebuchet state");
									validPosition.remove(visitPosition);
								}
							}
							break;
						case UnitTypes.UNIT_BATTERING_RAM:
							//go toward others Capital, to blow them out (that pun is intended)
							if (visitBox instanceof GroundBox) {
								GroundBox visitGBox = (GroundBox)visitBox;
								if (visitGBox.hasBuilding()) {
									
								}
							}
							break;
						default:
							//if it hasn't been resolved, act like a Normal AI
							//less malus if there is a Unit nearby
							if (visitBox.hasUnit()) {
								scoreGivenToPosition += (map.getBox(visitPosition).getUnit().getNumber() * 2);
							}
							else {
								scoreGivenToPosition += 20;
							}
							//avoid being near water
							if (map.isNearWater(visitPosition)) {
								scoreGivenToPosition -= 10;
							}
							break;
					}
					
					//now that the scoring is done, add it to the HashMap
					if (!listToGoPosition.containsKey(scoreGivenToPosition)) {
						listToGoPosition.put(scoreGivenToPosition, new ArrayList<Position>());
					}
					listToGoPosition.get(scoreGivenToPosition).add(visitPosition);
				}
				
				while (!listToGoPosition.isEmpty()) {
					//get the highest score stored
					highestScore = 0;
					for (Iterator<Integer> ite = listToGoPosition.keySet().iterator(); ite.hasNext(); ) {
						int checkingScore = ite.next();
						if (checkingScore > highestScore) {
							highestScore = checkingScore;
						}
					}
					//we have the highest score stored
					if (highestScore <= 0) {
						listToGoPosition.clear();
					}
					if (listToGoPosition.containsKey(highestScore)) {
						for (it = listToGoPosition.get(highestScore).iterator(); it.hasNext(); ) {
							visitPosition = it.next();
							//we try to move to this position
							if (validPosition.contains(visitPosition)) {
								try {
									return actionValidator.createActionAttack(power, unitPosition, visitPosition);
								} catch (IllegalArgumentException e) {
									logger.warn("IA tried to move to an invalid position");
									validPosition.remove(visitPosition);
								}
							}
						}
						//remove highest score
						listToGoPosition.remove(highestScore);
					}
				}

				//at this Point, stop & try to move instead
				//force to take only this Unit
				ArrayList<Units> forceArrayUnit = new ArrayList<Units>();
				forceArrayUnit.add(unitSelected);
				return this.tryCreateActionMove(power, forceArrayUnit, buildingList, territory);
		}
	}

	/**
	 * Recover all {@linkplain data.unit.Units Units} that Power have
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
	 * Recover all {@linkplain data.building.Building Buildings} that Power have
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

	private Position getBuildingPosition(Building buildingSelected) {
		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				Box tmpBox = map.getBox(i, j);
				if (tmpBox instanceof GroundBox) {
					GroundBox gBox = (GroundBox) tmpBox;
					if (gBox.hasBuilding() && gBox.getBuilding() == buildingSelected)
						return new Position(i, j);
				}
			}
		}
		return null;
	}

	private Position getUnitsPosition(Units unitsSelected) {
		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				Box tmpBox = map.getBox(i, j);
				if (tmpBox.hasUnit() && tmpBox.getUnit() == unitsSelected) {
					return new Position(i, j);
				}
			}
		}
		return null;
	}

	private Position getBoxPosition(Box boxSelected) {
		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				Box tmpBox = map.getBox(i, j);
				if (tmpBox == boxSelected) {
					return new Position(i, j);
				}
			}
		}
		return null;
	}

	/**
	 * Checks if a unitsList contains at least one unit of a specific unitType
	 * 
	 * @param unitsList
	 * @param unitType
	 * @return true if have one or more units of that type, else false
	 */
	private boolean haveThisUnitType(ArrayList<Units> unitsList, int unitType) {
		for (Units units : unitsList) {
			if (units.getTypes() == unitType)
				return true;
		}
		return false;
	}

	/**
	 * Checks if a buildingList contains at least one buildings of a specific
	 * buildingType
	 * 
	 * @param buildingList
	 * @param buildingType
	 * @return true if have one or more units of that type, else false
	 */
	private boolean haveThisBuildingType(ArrayList<Building> buildingList, int buildingType) {
		for (Building building : buildingList) {
			if (building.getType() == buildingType)
				return true;
		}
		return false;
	}

	/**
	 * Checks if powerAsked would want to do an alliance with powerAsking
	 * @param powerAsking
	 * @param powerAsked
	 * @return true if alliance accepted, false else
	 */
	public boolean checkAlliance(Power powerAsking, Power powerAsked) {
		int aiLevel = powerAsked.getAILevel();
		
		switch (aiLevel) {
		default:
		case GameConstants.AI_EASY:
			//refuses all alliances
			return false;
		case GameConstants.AI_NORMAL:
			//accepts an alliance 4 times out of 5
			int choice = random.nextInt(5);
			return choice < 4;
		case GameConstants.AI_HARD:
			//accepts only if powerAsking have more score
			int powerAskingScore = powerAsking.getResourceAmount(ResourceTypes.RESOURCE_SCORE);
			int powerAskedScore = powerAsked.getResourceAmount(ResourceTypes.RESOURCE_SCORE);
			return powerAskedScore <= powerAskingScore;
		}
	}
}

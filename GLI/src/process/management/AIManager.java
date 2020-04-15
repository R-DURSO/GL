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
			 * Hard ai will want to create either all units, or half, or 1. production per
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
			case 2: // wants to create a single unit
				numberUnitsCreated = 1;
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
				boolean canMove = false;
				while (it.hasNext() && !canMove) {
					visitPosition = it.next();
					canMove = true;
					try {
						return actionValidator.createActionMove(power, unitPosition, visitPosition);
					}
					catch (IllegalArgumentException e) {
						canMove = false;
					}
				}
				//if we end here, movement has Failed
				throw new WrongActionException("invalid movement unit");
		case GameConstants.AI_NORMAL:
			//feel the need to conquer
			HashMap<Integer, ArrayList<Position>> listToTryPosition = new HashMap<Integer, ArrayList<Position>>();
			for (it = validPosition.iterator(); it.hasNext(); ) {
				visitPosition = it.next();
				scoreGivenToPosition = ((4 * (map.getSize() / 10)) + (map.getDistance(visitPosition, ourCapitalPosition) * 15));
				Box visitBox = map.getBox(visitPosition);
				//less malus if there is a Unit nearby
				if (visitBox.hasUnit()) {
					scoreGivenToPosition -= (map.getBox(visitPosition).getUnit().getNumber() * 10);
				}
				else {
					scoreGivenToPosition -= 60;
				}
				//Added Bonus if there is a Building
				if (visitBox instanceof GroundBox) {
					if (((GroundBox)visitBox).hasBuilding()) {
						scoreGivenToPosition += 10;
						if (((GroundBox)visitBox).getBuilding() instanceof BuildingSpecial) {
							scoreGivenToPosition += 20;
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
			Position checkPosition;
			while (!listToTryPosition.isEmpty()) {
				//get the highest score stored
				for (Iterator<Integer> ite = listToTryPosition.keySet().iterator(); ite.hasNext(); ) {
					int checkingScore = ite.next();
					if (checkingScore > highestScore) {
						highestScore = checkingScore;
					}
				}
				//we have the highest score stored
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
									validPosition.remove(checkPosition);
								}
							}
						}
					}
				}
				//remove highest score
				listToTryPosition.remove(highestScore);
			}
			//at this Point, stop
			throw new WrongActionException("invalid unit movement");
			case GameConstants.AI_HARD:
				/*
				 * keep Infantry near Capital, intercept nearby ennemy
				 * send cavalry & pikeman conquer territory
				 * if there is Artefact, try to control it and defend it
				 * 
				 * 
				 */
		}
		
		
		
		//unless error, unreachable code
		// for now, add a random Box
		int numberValidBox = validPosition.size();
		Position positionSelected;
		if (numberValidBox < 1) {
			throw new WrongActionException("Designed Unit doesn't have any Box to move to");
		}
		else {
			int BoxIndex = random.nextInt(numberValidBox);
			positionSelected = validPosition.get(BoxIndex);
		}
		
		try {
			return actionValidator.createActionMove(power, unitPosition, positionSelected);
		} catch (IllegalArgumentException e) {
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
		
		
		/*
			switch (aiLevel) {
			default: //make the default case the easy
			case GameConstants.AI_EASY:
				check all Position
				take the best one
				try it
			case GameConstants.AI_NORMAL:
				optimal, make his unit move in group
					if unit is under attack, search nearby to regourp
					calcul visotory => number vs allied nearby
				regroup his unit for attack (making his action attack possible with only 3+ actionpoints
				
			case GameConstants.AI_HARD:
				only ai to know how to use trebuchet
				infantry for defense, pikeman & cavalry in attack
				make sure to have ranged unit behind other allied unit or wall
				take advantage of water?
		}
		*/
		
		// for now, add a random Box
		int numberValidBox = validPosition.size();
		Position positionSelected;
		if (numberValidBox < 1) {
			throw new WrongActionException("Designed Unit doesn't have any Box to attack");
		}
		else {
			int BoxIndex = random.nextInt(numberValidBox);
			positionSelected = validPosition.get(BoxIndex);
		}

		try {
			return actionValidator.createActionAttack(power, unitPosition, positionSelected);
		} catch (IllegalArgumentException e) {
			throw new WrongActionException("invalid unit movement");
		}

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
	private Action[] getDesiredActions(Power power, Action[] triedActions, int numberActions,
			ArrayList<Units> unitsList, ArrayList<Building> buildingList, ArrayList<Box> territory) {
		// we have some actions to prioritize in order to have a legit intelligence, so,
		// we check here if some things are done before
		return null;
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
}

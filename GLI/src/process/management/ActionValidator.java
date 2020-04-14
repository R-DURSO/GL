package process.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.GameConstants;
import data.GameMap;
import data.Position;
import data.Power;
import data.actions.*;
import data.boxes.*;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.*;
import data.building.product.*;
import data.building.special.*;
import data.resource.ResourceCost;
import data.resource.ResourceTypes;
import data.unit.*;
import log.LoggerUtility;
import org.apache.log4j.*;
/**
 * <p>This class ensures that every action the player does is possible
 * <br>and allows to create Actions to be performed at the end of the turn in the game.</p>
 * <p>No data class is modified here (except for resource values, including actions points)</p>
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @see {@link data.actions.Action Action}
 */
public class ActionValidator {
	private GameMap map;
	private static Logger logger = LoggerUtility.getLogger(ActionValidator.class, GameConstants.LOG_TYPE);
	/**
	 * ActionManager just needs to have access to the game map
	 * @param map The game map
	 */
	public ActionValidator(GameMap map) {
		this.map = map;
	}
	
	/**
	 * Check if one of the two powers has already gone to someone before returning the ActionMakeAlliance
	 * 
	 * @param powerConcerned The player who in hand
	 * @param potentialAlly The power which the player wants to ally with
	 * @return ActionMakeAlliance
	 * @see data.actions.ActionMakeAlliance
	 * @throws IllegalArgumentException If the conditions are not met 
	 */
	public ActionMakeAlliance createActionMakeAlliance(Power powerConcerned, Power potentialAlly) throws IllegalArgumentException{
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		//check if one of players has already an allied
		if (powerConcerned.isAllied() || potentialAlly.isAllied()) {
			logger.warn(powerConcerned.getName()+" or "+potentialAlly.getName()+" already have an ally ");
			throw new IllegalArgumentException("Une des puissances est deja en alliance");
		}
		
		//check if powerConcerned and potentialAlly are the same power
		if(powerConcerned == potentialAlly) {
			logger.warn(powerConcerned.getName()+" and "+potentialAlly.getName()+" are the same person ");
			throw new IllegalArgumentException("On ne peut pas s'allier avec soi-même");
		}
		
		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		logger.info(powerConcerned.getName()+" do ActionMakeAlliance with "+potentialAlly.getName());
		return new ActionMakeAlliance(powerConcerned, potentialAlly);
	}
	
	/**
	 * Check if powers are allied before returning the ActionBreakAlliace
	 * @param powerConcerned The player who in hand
	 * @param formerAlly
	 * @return ActionBreakAlliance
	 * @see data.actions.ActionBreakAlliance
	 * @throws IllegalArgumentException If the conditions are not met 
	 */
	public ActionBreakAlliance createActionBreakAlliance(Power powerConcerned, Power formerAlly) throws IllegalArgumentException{
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		//check if player has ally and if this ally is the right Power
		if(!powerConcerned.isAllied()) {
			logger.warn(powerConcerned.getName()+" don't have an ally ");
			throw new IllegalArgumentException(powerConcerned.getName() + " n'a pas d'allie");
		}
		if(powerConcerned.getAlly() != formerAlly) {
			logger.warn(powerConcerned.getName()+" not in alliance with "+formerAlly.getName());
			throw new IllegalArgumentException(powerConcerned.getName() + " n'est pas en alliance avec " + formerAlly.getName());
		}

		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		logger.info(powerConcerned.getName()+" do ActionBreakAlliance with "+formerAlly.getName());
		return new ActionBreakAlliance(powerConcerned);
	}
	
	/**
	 * Check if:
	 * <ul>
	 * 	<li>from position belongs to powerConcerned</li>
	 * 	<li>from position has any unit to attack with</li>
	 * 	<li>units in from position are in range to attack (they can't move diagonally)</li>
	 * 	<li>target position has ennemy units to attack</li>
	 * </ul>
	 * Before returning the ActionAttack
	 * @param powerConcerned The player who's taking the Action
	 * @param from The position where player's units will attack
	 * @param target Where player's units will attack
	 * @return ActionAttack
	 * @see data.actions.ActionAttack
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionAttack createActionAttack(Power powerConcerned, Position from,  Position target) throws IllegalArgumentException{
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		if (from.equals(target)) {
			logger.warn(powerConcerned.getName()+" try to attack himself");
			throw new IllegalArgumentException("On ne peut pas attaquer sur sa propre case");
		}
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if there is any unit on the fromBox
		if (!fromBox.hasUnit()) {
			logger.warn(powerConcerned.getName()+" try to launch an attack without unit");
			throw new IllegalArgumentException("Il n'y a pas d'unite pour lancer l'attaque");
		}
		
		Units unitsAtt = fromBox.getUnit();
		
		//check if from Box is powerConcerned's
		if (unitsAtt.getOwner() != powerConcerned) {
			logger.warn(powerConcerned.getName()+" try to laucnh an attack with unit that doesn't belong to him");
			throw new IllegalArgumentException("Ces unites n'appartiennent pas a " + powerConcerned.getName());
		}
		
		//check if units can be used
		if (unitsAtt.getIsMoving()) {
			logger.warn(powerConcerned.getName()+" try to move already moving unit");
			throw new IllegalArgumentException("Vous bougez des unites qui sont en train de se deplacer");
		}
		
		//check if units are on range
		if (!isUnitsOnRange(from, unitsAtt.getRange(), target)) {
			logger.warn(powerConcerned.getName()+" try to launch an attack with faraway units");
			throw new IllegalArgumentException("Les unites sont trop loin de la cible");
		}
		
		
		//Verification to be done if there is a Unit
		if (targetBox.hasUnit()) {
			//check if it's a PhantomUnit
			if (targetBox.getUnit().getTypes() < 0) {
				logger.warn(powerConcerned.getName()+" try to attack a ghost");
				throw new IllegalArgumentException(powerConcerned.getName()+" attaque un Phantom");
			}

			//check if there is units on target, in this case, check the owner of those units
			//if player himself or his ally, no attack
			if (targetBox.getUnit().getOwner() == powerConcerned) {
				logger.warn(powerConcerned.getName()+" try to attack his own units");
				throw new IllegalArgumentException("Vous attaquez vos propres unites");
			}
			else if (powerConcerned.isAllied()) {
				if (targetBox.getUnit().getOwner() == powerConcerned.getAlly()) {
					logger.warn(powerConcerned.getName()+" try attack allied units");
					throw new IllegalArgumentException("Vous attaquez les unites de votre allie");
				}
			}
		}
		else {
			//Pas d'unite a attaquer, mais peut-etre un batiment
			if (targetBox instanceof WaterBox) {
				logger.warn(powerConcerned.getName()+" try to attack water ");
				throw new IllegalArgumentException("Vous attaquez une case d'eau vide");
			}
			else {
				if (!( (GroundBox)targetBox).hasBuilding()) {
					logger.warn(powerConcerned.getName()+" try to attack grass");
					throw new IllegalArgumentException("Il n'y a pas d'unite ou de batiment a attaquer");
				}
			}
		}
		
		
		//check now if player try to attack the territory of himself or his ally
		if (targetBox.getOwner() == powerConcerned) {
			logger.warn(powerConcerned.getName()+" try to attack his own territory");
			throw new IllegalArgumentException("Vous attaquez vos propres cases");
		}
		else if (powerConcerned.isAllied()) {
			if (targetBox.getOwner() == powerConcerned.getAlly()) {
				logger.warn(powerConcerned.getName()+" try attack allied territory");
				throw new IllegalArgumentException("Vous attaquez le territoire de votre allie");
			}
		}

		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		//the unit has done an action this turn
		unitsAtt.setIsMoving();
		logger.info(powerConcerned.getName()+" create an ActionAttack");
		return new ActionAttack(powerConcerned, from, target);
	}


	/**
	 * Check if:
	 * <ul>
	 * 	<li>from position belongs to powerConcerned</li>
	 * 	<li>from position has any unit to move</li>
	 * 	<li>units in from position are in range to move (they can't move diagonally)</li>
	 * 	<li>target position can go on the target position (infantry can't go in water or on boxes where there is Archers for instance)</li>
	 * </ul>
	 * Before returning the ActionMove
	 * @param powerConcerned The player who's taking the Action
	 * @param from The position where player's units will move
	 * @param target Where player's units will move
	 * @return ActionMove
	 * @see {@link data.actions.ActionMove ActionMove}
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionMove createActionMove (Power powerConcerned, Position from, Position target) throws IllegalArgumentException {
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if there is any Units on the fromBox
		if (!fromBox.hasUnit()) {
			logger.warn(powerConcerned.getName()+" try to move without unit");
			throw new IllegalArgumentException("Il n'y a pas d'unite a deplacer ici");
		}
		
		Units movingUnits = fromBox.getUnit();
		Power UnitsOwner = movingUnits.getOwner();
		
		//Check if you want to move to the same Box
		//Trebuchet are the only one allowed to move where they stand, as it let them change state
		if (movingUnits.getTypes() != UnitTypes.UNIT_TREBUCHET) {
			if (from.equals(target)) {
				logger.warn(powerConcerned.getName()+" try to move where he is standing");
				throw new IllegalArgumentException("On ne peut pas se deplacer la ou on est deja");
			}
		}
		
		if (UnitsOwner != powerConcerned) {
			logger.warn(powerConcerned.getName()+" try to move ennemy unit");
			throw new IllegalArgumentException("Vous essayer de bouger des unites qui ne vous appartiennent pas");
		}
		
		if (movingUnits.getIsMoving()) {
			logger.warn(powerConcerned.getName()+" try to move already moving unit");
			throw new IllegalArgumentException("Vous bougez des unites qui sont en train de se deplacer");
		}
		
		//check if units are on range
		if (!isUnitsOnRange(from, movingUnits.getMovement(), target)) {
			logger.warn(powerConcerned.getName()+" try to move units too far");
			throw new IllegalArgumentException("Les unites sont trop loin de la cible");
		}

		//check if there is "obstacle" on target : either wall / ennemy door, or units
		String pathFinding = pathFinding(from, movingUnits, target);
		if (pathFinding == null) {
			logger.warn(powerConcerned.getName()+" cant find path to his destination");
			throw new IllegalArgumentException("Impossible de determiner un chemin jusqu'a la destination");
		}
		
		//check if there isn't any ennemy Unit or Different UnitTypes
		if (targetBox.hasUnit()) {
			Units unitsOnTarget = targetBox.getUnit();
			//check if units already want to go to target, which means checking if a PhantomUnit is on targetBox
			if(unitsOnTarget.getTypes() < 0) {
				logger.warn(powerConcerned.getName()+" move an unit where another unit is already going");
				throw new IllegalArgumentException("Une unite compte se rendre a cette position");
			}
			else if (unitsOnTarget.getOwner() == powerConcerned) {
				if (unitsOnTarget.getTypes() == UnitTypes.UNIT_BOAT) {
					//moves Units on the boat
					Boat boatTarget = ((Boat)unitsOnTarget);
					if (boatTarget.hasContainedUnits()) {
						Units containedUnit = boatTarget.getContainedUnits();
						//You can re-group same Units Types
						if (containedUnit.getTypes() != movingUnits.getTypes()) {
							logger.warn(powerConcerned.getName()+" try to move unit in a boat where there is a different type of unit");
							throw new IllegalArgumentException("Des Unites de types differents sont dans ce bateau");
						}
						//But, make sure you dont exceed the limit
						else if (containedUnit.getNumber() + movingUnits.getNumber() > movingUnits.getMaxNumber()) {
							logger.warn(powerConcerned.getName()+" try to break the maximum capabilities of a boat");
							throw new IllegalArgumentException("Le deplacement fait depasser la limite d'unite");
						}
					}
				}
				else if (movingUnits.getTypes() != unitsOnTarget.getTypes()) {
					logger.warn(powerConcerned.getName()+" try to fuse different unit type");
					throw new IllegalArgumentException("Des unites d'un type different sont sur le lieu cible");
				}
				//But, make sure you dont exceed the limit
				else if (movingUnits.getNumber() + unitsOnTarget.getNumber() > movingUnits.getMaxNumber()) {
					logger.warn(powerConcerned.getName()+" try to overload an unit with more unit");
					throw new IllegalArgumentException("Le deplacement fait depasser la limite d'unite");
				}
			}
			else {
				logger.warn(powerConcerned.getName()+" try to move where another powers' units are");
				throw new IllegalArgumentException("Il y a des unites d'un autre joueur sur la case cible");
			}
		}
		
		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		//set the unit in movement
		movingUnits.setIsMoving();
		//add phantom unit on the target box, to ensure that no other unit could go there
		if (!targetBox.hasUnit()) {
			targetBox.setUnit(new PhantomUnit(powerConcerned, movingUnits.getTypes()));
		}
		//If it's a Boat, add a PhantomUnit so that nobody else come aboard
		else if (targetBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
			Boat targetBoat = (Boat)targetBox.getUnit();
			if (!targetBoat.hasContainedUnits()) {
				targetBoat.setContainedUnits(new PhantomUnit(powerConcerned, movingUnits.getTypes()));
			}
		}
		Box[] ListBox = convertPathToBoxArray(pathFinding, from);
		logger.info(powerConcerned.getName()+" create an ActionMove");
		return new ActionMove(powerConcerned, ListBox);
	}
	
	/**
	 * Determine the PathFinding an Unit will take to go to a target.
	 * Units will conquer all passed territory.
	 * @param from Starting Box
	 * @param unitsMovement Movement Point of the Unit
	 * @param target Box where the Units wants to go
	 * @return true if Units have a path
	 */
	public String pathFinding(Position from, Units units, Position target) {
		//Get the Power from the Starting Point
		Power powerConcerned = units.getOwner();
		//Get the Allied Power (if there is one)
		boolean Allied = false;
		Power Ally = null;
		if (powerConcerned.isAllied()) {
			Ally = powerConcerned.getAlly();
			Allied = true;
		}
		
		//How far the Unit can go
		int unitsMovement = units.getMovement();
		//HashMap created, with the next Boxes to go
		HashMap<String,Position> visiting = new HashMap<String,Position>(8*unitsMovement);
		//HashMap created which contains the next Boxes to visit
		HashMap<String,Position> toVisit = new HashMap<String,Position>(8*unitsMovement);
		//ArrayList created, to stock visited Boxes
		ArrayList<Position> visited = new ArrayList<Position>(4*unitsMovement);
		//Adding the Starting Box
		toVisit.put("0", from);
		
		//Boolean that will check if we are moving a Boat
		boolean checkBoat = false;
		//Boolean that will check if there is Unit blocking the path
		boolean checkUnit = false;
		//Boolean that will check if we can visit adjacent Boxes
		boolean canVisit = false;

		//We search as far as we can (while there is Box toVisit)
		while (!toVisit.isEmpty()) {
			//Clear visiting list, and add those we need to visit next
			visiting.clear();
			visiting.putAll(toVisit);
			toVisit.clear();
			
			//Iterator trough all visiting Boxes
			for (Iterator<String> i = visiting.keySet().iterator(); i.hasNext(); ) {
				//Get some data from the received Box
				//path is the path taken to get here
				String path = i.next();
				//if path is too long, enough Position have been visited, can't go any further
				if (path.length() <= unitsMovement + 1) {
				
					//visitPosition get the actual Position
					Position visitPosition = visiting.get(path);
					//visitBox get the Box on the Map
					Box visitBox = map.getBox(visitPosition);
					
					//if we haven't visited the Position
					if (!visited.contains(visitPosition)) {
						//reset checkBoat, checkUnit and canVisit
						checkBoat = false;
						checkUnit = false;
						canVisit = false;
						
						//First Verification is about Boxes, making sure we can go through
						if (visitBox instanceof WaterBox) {
							//On the sea
							if (units.getTypes() == UnitTypes.UNIT_BOAT) {
								//A boat on the sea
								checkBoat = true;
							}
							else if (visitBox.hasUnit()) {
								//check if there is a boat you can move on
								if (visitBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
									//there is a Boat
									if (visitBox.getUnit().getOwner() == powerConcerned) {
										//You can go on your own boat
										checkBoat = true;
									}
									//Boat is controlled by another power
								}
							}
							//Not a Boat and there isn't a Unit, can't go through Water
						}
						else {
							//implicit GroundBox
							GroundBox visitGBox = (GroundBox) visitBox;
							//we check for Building
							if (visitGBox.hasBuilding()) {
								int buildingType = visitGBox.getBuilding().getType();
								if (buildingType == BuildingTypes.BUILDING_WALL) {
									//Cannot go through Wall
								}
								else if (buildingType == BuildingTypes.BUILDING_DOOR || buildingType == BuildingTypes.BUILDING_TEMPLE) {
									//Does the Door / temple belong to us
									if (visitGBox.getOwner() == powerConcerned) {
										checkBoat = true;
									}
									//Do we have an Ally
									else if (Allied) {
										//And does the Door belong to Ally
										if (visitGBox.getOwner() == powerConcerned.getAlly()) {
											checkBoat = true;
										}
									}
									//else, cannot go through ennemy Door
								}
								else if (buildingType == BuildingTypes.BUILDING_CAPITAL) {
									//does Capital belong to us
									if (visitGBox.getOwner() == powerConcerned) {
										checkBoat = true;
									}
								}
								else if (buildingType == BuildingTypes.BUILDING_TEMPLE) {
									//does Temple belong to us
									if (visitGBox.getOwner() == powerConcerned) {
										checkBoat = true;
									}
								}
								else {
									//Not a Special Building
									checkBoat = true;
								}
							}
							else {
								//No Building
								checkBoat = true;
							}
							
						}
						
						
						//Special Verification about the Boat, pass trhough if units isn't a Boat
						if (checkBoat) {
							if (units.getTypes() == UnitTypes.UNIT_BOAT) {
								Boat unitsBoat = (Boat)units;
								//Make sure that the Boat is on or near Water
								if (map.isNearWater(visitPosition)) {
									//Now, 2 cases:   -WaterBox   -Coast (GroundBox near Water)
									if (visitBox instanceof WaterBox) {
										//Boat is on Water
										if (!visitBox.hasUnit()) {
											//WaterBox without unit
											if (visitPosition.equals(target)) {
												return path;
											}
										}
										//there is unit where we want to go
										checkUnit = true;
									}
									else {
										//implicit Coast Box
										if (unitsBoat.hasContainedUnits()) {
											if (visitBox.hasUnit()) {
												//there is unit where we want to go
												if (visitBox.getUnit().getOwner() == powerConcerned) {
													//our unit on coast
													if (visitBox.getUnit().getTypes() == unitsBoat.getContainedUnitsTypes()) {
														//Same type, so we can deposit
														if (visitPosition.equals(target)) {
															return path;
														}
													}
													//different type, dont go there
												}
												//not our unit, cannot unload the boat
											}
											//Boat goes to GroundBox without unit to deposit stocked one
											else if (visitPosition.equals(target)) {
												return path;
											}
											//cannot visit nearby Box, you're a boat
										}
										//Boat doesn't have unit inside, only visit the Coast
										checkUnit = true;
									}
								}
								//else, dont go where there isn't water nearby
							}
							else {
								//Not moving a Boat, pass through
								checkUnit = true;
							}
						}
						
						
						//checkUnit allow to check if we can go on the Box if there is a Unit
						if (checkUnit) {
							//Only verification, make sure you dont walk on other unit
							if (visitBox.hasUnit()) {
								Units visitUnit = visitBox.getUnit();
								//checking if we own the unit in the path
								if (visitBox.getUnit().getOwner() != powerConcerned) {
									if (Allied) {
										if (visitBox.getUnit().getOwner() == Ally) {
											//there is an Ally unit, we can continue our visit, but can't stop here
											canVisit = true;
										}
										//Not our Ally
									}
									//No Ally, there is only ennemy on this map
								}
								else {
									//Our own Unit
									if (visitUnit.getTypes() == UnitTypes.UNIT_BOAT) {
										//Our own Boat
										Boat visitBoat = (Boat)visitUnit;
										//check if there is Unit inside (Phantom) or if we can go with if same type
										if (visitBoat.hasContainedUnits()) {
											if (visitBoat.getContainedUnitsTypes() < 0) {
												//Phantom, dont go in
												canVisit = true;
											}
											else if (visitBoat.getContainedUnitsTypes() == units.getTypes()) {
												//Same type, we can go in
												if (visitPosition.equals(target)) {
													return path;
												}
												canVisit = true;
											}
											else {
												//Not the same Type, but we can go through
												canVisit = true;
											}
										}
										//No unit inside, we can go in
										//other implicit cases: creation of Boat
										else {
											if (visitPosition.equals(target)) {
												return path;
											}
											canVisit = true;
										}
									}
									else if (visitUnit.getTypes() == units.getTypes()) {
										//Same UnitTypes, we can go there
										if (visitPosition.equals(target)) {
											return path;
										}
										canVisit = true;
									}
									else if (visitUnit.getTypes() == -units.getTypes()) {
										//Phantom of Same UnitTypes, we can go there
										//but we dont visit to avoid unvoluntary regroupment
										if (visitPosition.equals(target)) {
											return path;
										}
									}
									else {
										//Nor a Boat or Same UnitTypes, we can only visit
										canVisit = true;
									}
								}
							}
							else {
								//no Unit here
								if (visitPosition.equals(target)) {
									return path;
								}
								canVisit = true;
							}
						}
						
						
						//Verification completed, checking if we can visit nearby Box
						if (canVisit) {
							Position dataToAdd;
							for (int d=1 ; d<=4 ; d++) {
								switch(d) {
								case 1:
									dataToAdd = map.getUpPos(visitPosition);
									break;
								case 2:
									dataToAdd = map.getLeftPos(visitPosition);
									break;
								case 3:
									dataToAdd = map.getRightPos(visitPosition);
									break;
								case 4:
									dataToAdd = map.getDownPos(visitPosition);
									break;
								default:
									dataToAdd = null;
									break;
								}
								if (dataToAdd != null) {
									toVisit.put(path+Integer.toString(d),dataToAdd);
								}
							}
						//Added all near Boxes
						}
						//Added the Position to Visited 
						visited.add(visitPosition);
						
						//end of the visit
					}
					//visited has already seen visitPosition
				}
				//too far to reach
			}
			//No more visiting Boxes
		}
		//La node stock un code pour retrouver son chemin (String path)
		//Si on atteins ce point du code, le chemin n'a pas ete trouve
		return null;
	}
	
	/**
	 * Create a Array of Box to use from a Array of Position
	 * @param pathFinding, the String generated by Pathfinding
	 * @param StartingPos the Position where to Start
	 * @return An Array of Box
	 */
	private Box[] convertPathToBoxArray(String pathFinding, Position StartingPos) {
		//startingPosition
		Position checkPos = StartingPos;
		//Array of Position
		Position[] pathPos = new Position[pathFinding.length()];
		for(int i=0; i<pathFinding.length(); i++) {
			char d = pathFinding.charAt(i);
			switch(d) {
			case '0':
				break;
			case '1':
				checkPos = map.getUpPos(checkPos);
				break;
			case '2':
				checkPos = map.getLeftPos(checkPos);
				break;
			case '3':
				checkPos = map.getRightPos(checkPos);
				break;
			case '4':
				checkPos = map.getDownPos(checkPos);
				break;
			default:
				checkPos = null;
				break;
			}
			pathPos[i] = checkPos;
		}
		//convert Position to Box
		Box[] pathBox = new Box[pathPos.length];
		for(int i=0; i<pathPos.length; i++) {
			pathBox[i] = map.getBox(pathPos[i]);
		}
		return pathBox;
	}
	
	/**
	 * check all adjacent Boxes on a set Range
	 * @param from Starting Box
	 * @param unitsMoveRange Integer that limit the range
	 * @param target Box where you want to go
	 * @return if target is at the range of from 
	 */
	public boolean isUnitsOnRange(Position from, int unitsMoveRange, Position target) {
		return (map.getDistance(target, from) <= unitsMoveRange);
	}
	
	/**
	 * Check if:
	 * <ul>
	 * 	<li>target position belongs to powerConcerned</li>
	 *  <li>powerConcerned has enough resources to build</li>
	 * 	<li>target position is not a water box</li>
	 * 	<li>target position has no building yet</li>
	 * </ul>
	 * Before returning the ActionConstruct
	 * @param powerConcerned The player who in hand
	 * @param buildingType The ID of the wanted building (can be found as constant in BuildingTypes)
	 * @param target Where player want to construct
	 * @return ActionConstruct
	 * @see data.actions.ActionConstruct
	 * @see data.building.BuildingTypes
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionConstruct createActionConstruct(Power powerConcerned, int buildingType, Position target) throws IllegalArgumentException, NullPointerException{
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			logger.warn(powerConcerned.getName()+" try to construct in other power case ");
			throw new IllegalArgumentException("Impossible de construire sur une case etrangere");
		}
		
		//check if targetBox is a WaterBox or not
		if(targetBox instanceof WaterBox) {
			logger.warn(powerConcerned.getName()+" try to construct in water case ");
			throw new IllegalArgumentException("Impossible de construire sur une case d'eau");
		}
		//check if have enough resources to build
		ResourceCost neededResource = getBuildingCost(buildingType); 
		if(!checkPrice(powerConcerned.getResourceAmount(neededResource.getType()), neededResource.getCost())) {
			logger.warn(powerConcerned.getName()+" try to construct without enough Resources");
			throw new IllegalArgumentException("Pas assez de ressources pour construire ceci");
		}
		GroundBox groundBox = (GroundBox) targetBox;
		
		
		
		//check if there is already a building on this box
		if(groundBox.hasBuilding()) {
			//check if a building is going to be builded there, that is to say if instance of PhantomBuilding is on the targetBox
			if(groundBox.getBuilding() instanceof PhantomBuilding) {
				logger.warn(powerConcerned.getName()+" try to build on a construction site");
				throw new IllegalArgumentException("Un autre batiment va etre construit sur cette case");
			}
			logger.warn(powerConcerned.getName()+" try to build on top of another building");
			throw new IllegalArgumentException("Impossible de construire sur une case qui possede deja un batiment"); 
		}
		
		if (buildingType == BuildingTypes.BUILDING_DOCK) {
			if (!map.isNearWater(target)) {
				logger.warn(powerConcerned.getName()+" think dock doesn't belong near Water");
				throw new IllegalArgumentException("Le port n'est pas a poximite de case d'eau"); 
			}
		}
		
		if (buildingType == BuildingTypes.BUILDING_TEMPLE) {
			if (groundBox.getResourceType() != ResourceTypes.RESOURCE_ARTIFACT) {
				logger.warn(powerConcerned.getName()+" try to win without the Artifact");
				throw new IllegalArgumentException("La case ne possede pas l'Artefact"); 
			}
		}
		
		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		//remove building cost
		powerConcerned.getResource(neededResource.getType()).subValue(neededResource.getCost());
		//add phantom building on the target box, to ensure that no other building could be constructed on targetBox
		groundBox.setBuilding(new PhantomBuilding());
		logger.info(powerConcerned.getName()+" create an ActionConstruct");
		return new ActionConstruct(powerConcerned, buildingType, target);
	}
	
	/**
	 * Used in CreateActionConstruct
	 * @param buildingType representing which Building you want to construct
	 * @return the ResourceCost of the construction, with the Type of Resource Needed
	 */
	private ResourceCost getBuildingCost(int buildingType) {
		switch(buildingType) {
		case BuildingTypes.BUILDING_BARRACK:
			return new ResourceCost(Barrack.COST, ResourceTypes.RESOURCE_WOOD);
		case BuildingTypes.BUILDING_WORKSHOP:
			return new ResourceCost(Workshop.COST, ResourceTypes.RESOURCE_STONE);
		case BuildingTypes.BUILDING_DOCK:
			return new ResourceCost(Dock.COST, ResourceTypes.RESOURCE_WOOD);
		case BuildingTypes.BUILDING_MINE:
			return new ResourceCost(Mine.COST, ResourceTypes.RESOURCE_WOOD);
		case BuildingTypes.BUILDING_SAWMILL:
			return new ResourceCost(Sawmill.COST, ResourceTypes.RESOURCE_WOOD);
		case BuildingTypes.BUILDING_WINDMILL:
			return new ResourceCost(Windmill.COST, ResourceTypes.RESOURCE_WOOD);
		case BuildingTypes.BUILDING_QUARRY:
			return new ResourceCost(Quarry.COST, ResourceTypes.RESOURCE_WOOD);
		case BuildingTypes.BUILDING_DOOR:
			return new ResourceCost(Door.COST, ResourceTypes.RESOURCE_STONE);
		case BuildingTypes.BUILDING_TEMPLE:
			return new ResourceCost(Temple.COST, ResourceTypes.RESOURCE_STONE);
		case BuildingTypes.BUILDING_WALL:			
			return new ResourceCost(Wall.COST, ResourceTypes.RESOURCE_STONE);
		}
		return new ResourceCost(0, ResourceTypes.RESOURCE_WOOD);
	}

	private boolean checkPrice(int resourceAmount, int cost) {
		return resourceAmount >= cost;
	}

	/**
	 * Check if:
	 * <ul>
	 * 	<li>target position belongs to powerConcerned</li>
	 * 	<li>target position is not a water box</li>
	 * 	<li>target position has the right building to create units (for exemple, Archer can be created only in Barracks</li>
	 * </ul>
	 * Before returning the ActionCreateUnit
	 * @param powerConcerned The player who in hand
	 * @param unitType The ID of the wanted unit type (can be found as constant in UnitTypes)
	 * @param numberUnits the number of units the player want to create
	 * @param target Where player want to create units
	 * @return ActionCreateUnit
	 * @see data.actions.ActionCreateUnit
	 * @see data.unit.UnitTypes
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	
	public ActionCreateUnit createActionCreateUnit(Power powerConcerned, int unitType, int numberUnits, Position target) throws IllegalArgumentException{
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			logger.warn(powerConcerned.getName()+" can't create unit in other power Box");
			throw new IllegalArgumentException("Impossible de creer des unites sur une case etrangere");
		}
		
		//check if have enough gold to create units 
		ResourceCost neededResource = getUnitCost(powerConcerned, unitType, numberUnits);
		if(!checkPrice(powerConcerned.getResourceAmount(neededResource.getType()), neededResource.getCost())) {
			logger.warn(powerConcerned.getName()+" not ressource for buy unit ");
			throw new IllegalArgumentException("Pas assez de ressources pour creer ces unites");
		}
				
		//check if not in water
		if(targetBox instanceof WaterBox) {
			logger.warn(powerConcerned.getName()+" try to create unit from water");
			throw new IllegalArgumentException("Impossible de creer des unites sur une case d'eau");
		}
		GroundBox groundBox = (GroundBox) targetBox;
		boolean createPhantom = true;
		//check if there is unit
		if (groundBox.hasUnit()) {
			//check if same unitType (for grouping)
			if (groundBox.getUnit().getTypes() != unitType) {
				logger.warn(powerConcerned.getName()+" try to fuse different unit type");
				throw new IllegalArgumentException("des unites differentes sont presents sur la case");
			}
			else {
				createPhantom = false;
			}
		}
		//check if this box has a building
		if(!groundBox.hasBuilding()) {
			logger.warn(powerConcerned.getName()+" try to create unit without building");
			throw new IllegalArgumentException("La creation d'unites demande la presence d'un batiment specifique");
		}
		Building building = groundBox.getBuilding();
		
		//check if thoses units can be created here
		if(! (building instanceof BuildingArmy)) {
			//exception, Capitale can create Infantry to avoid getting stuck
			if (powerConcerned.getCapital() == building) {
				//only accept Infantry
				if (unitType != UnitTypes.UNIT_INFANTRY) {
					logger.warn(powerConcerned.getName()+" try to create invalid unit in his Capital");
					throw new IllegalArgumentException("Seul les Infantries sont autorisés dans la Capitale");
				}
			}
			else {
				logger.warn(powerConcerned.getName()+" try to create unit in the wrong building");
				throw new IllegalArgumentException("Impossible de creer des unites dans ce batiment");
			}
		}
		else {
			//we need to know if this building is enabled (or if Building#buildTime == 0)
			if(building.getBuildTime() > 0) {
				logger.warn(powerConcerned.getName()+" use a construction site to create unit");
				throw new IllegalArgumentException("Ce batiment n'est pas encore utilisable");
			}
			switch(building.getType()) {
			case BuildingTypes.BUILDING_BARRACK:
				if(unitType >= UnitTypes.UNITS_IN_BARRACK || unitType < 0) {
					logger.warn(powerConcerned.getName()+" create wrong unit in Barrack");
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des casernes");
				}
				break;
			case BuildingTypes.BUILDING_WORKSHOP:
				if(unitType < UnitTypes.UNITS_IN_BARRACK || unitType >= UnitTypes.UNITS_IN_WORKSHOP) {
					logger.warn(powerConcerned.getName()+" create wrong unit in Workshop");
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des ateliers");
				}
				break;
			case BuildingTypes.BUILDING_DOCK:
				if(unitType < UnitTypes.UNITS_IN_WORKSHOP) {
					logger.warn(powerConcerned.getName()+" create wrong unit in Dock");
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des ports");
				}
				break;
			default:
				logger.warn(powerConcerned.getName()+" try to create an unknown unit");
				throw new IllegalArgumentException("Il n'y a pas d'unite de ce type");	
			}
		}

		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		if (createPhantom) {
			targetBox.setUnit(new PhantomUnit(powerConcerned, unitType));
		}
		powerConcerned.getResource(neededResource.getType()).subValue(neededResource.getCost());
		logger.info(powerConcerned.getName()+" create an ActionCreateUnit");
		return new ActionCreateUnit(powerConcerned, unitType, numberUnits, target);
	}
	
	private ResourceCost getUnitCost(Power powerConcerned, int unitType, int numberUnits) {
		switch (unitType) {
		case UnitTypes.UNIT_ARCHER:
			return new ResourceCost(Archer.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		case UnitTypes.UNIT_INFANTRY:
			return new ResourceCost(Infantry.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		case UnitTypes.UNIT_CAVALRY:
			return new ResourceCost(Cavalry.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		case UnitTypes.UNIT_PIKEMAN:
			return new ResourceCost(Pikeman.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		case UnitTypes.UNIT_BATTERING_RAM:
			return new ResourceCost(BatteringRam.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		case UnitTypes.UNIT_TREBUCHET:
			return new ResourceCost(Trebuchet.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		case UnitTypes.UNIT_BOAT:
			return new ResourceCost(Boat.COST * numberUnits, ResourceTypes.RESOURCE_GOLD);
		default:
			return new ResourceCost(0, ResourceTypes.RESOURCE_GOLD);
		}
	}

	/**
	 * Check if:
	 * <ul>
	 * 	<li>target position belongs to powerConcerned</li>
	 * 	<li>target position is not a water box</li>
	 * 	<li>target position has a building to destroy</li>
	 * </ul>
	 * Before returning the ActionDestroyBuilding
	 * @param powerConcerned The player who in hand
	 * @param target Where player want to destroy a building
	 * @return ActionDestroyBuilding
	 * @see data.actions.ActionDestroyBuilding
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionDestroyBuilding createActionDestroyBuilding(Power powerConcerned, Position target) throws IllegalArgumentException{
		/*
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			Logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		*/
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			logger.warn(powerConcerned.getName()+" try to remove building from another power");
			throw new IllegalArgumentException("Impossible de faire une destruction sur une case qui ne nous appartient pas");
		}
		
		//check if there is indeed a building here
		//we first need to know the type of the box
		if(targetBox instanceof WaterBox) {
			logger.warn(powerConcerned.getName()+" try to destroy Water");
			throw new IllegalArgumentException("Il n'y a rien a detruire sur une case d'eau");
		}
		else {
			GroundBox groundBox = (GroundBox) targetBox;
			if(!groundBox.hasBuilding()) {
				logger.warn(powerConcerned.getName()+" try to destroy nothing");
				throw new IllegalArgumentException("Il n'y a pas de building sur cette case");
			}
			//check if the building is the Capital (we don't allow player to destroy his own Capital)
			Building building = groundBox.getBuilding();
			if(building instanceof Capital) {
				logger.warn(powerConcerned.getName()+" try to destroy hiw own Capital");
				throw new IllegalArgumentException("Vous ne pouvez pas detruire votre capitale");
			}
		}
		/*
		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		*/
		logger.info(powerConcerned.getName()+" create an ActionDestroyBuilding");
		return new ActionDestroyBuilding(powerConcerned, target);
	}
	
	/**
	 * Check if:
	 * <ul>
	 * 	<li>target position belongs to powerConcerned</li>
	 * 	<li>target position has units to destroy</li>
	 * </ul>
	 * Before returning the ActionDestroyUnits
	 * @param powerConcerned The player who in hand
	 * @param target Where player want to destroy units
	 * @return ActionDestroyUnits
	 * @see data.actions.ActionDestroyUnits
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionDestroyUnits createActionDestroyUnits(Power powerConcerned, Position target) throws IllegalArgumentException{
		/*
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			Logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		*/
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		Units units = targetBox.getUnit();
		//check if powerConcerned owns those units
		if(units.getOwner() != powerConcerned) {
			logger.warn(powerConcerned.getName()+" try to destroy ennemy unit");
			throw new IllegalArgumentException("Ces unites ne vous appartiennent pas");
		}
		//check if there is any unit on this box
		if(!targetBox.hasUnit()) {
			logger.warn(powerConcerned.getName()+" try to destroy nothing");
			throw new IllegalArgumentException("Il n'y a pas d'unites a supprimer ici");
		}
		/*
		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		*/
		logger.info(powerConcerned.getName()+" create an ActionDestroyUnit");
		return new ActionDestroyUnits(powerConcerned, target);
	}
	
	private Box getBoxFromMap(Position position) {
		return map.getBox(position.getX(), position.getY());
	}
	
	
	/**
	 * <p>The Action to upgrade your Capital,</p>
	 *  Before adding the Action, check if:
	 * <ul>
	 * 	<li>Capital is not already level max,</li>
	 * 	<li>powerConcerned have enough resources,</li>
	 * </ul>
	 * @param powerConcerned The player who's in hand
	 * @return the concerned Action, ActionUpgradeCapital
	 * @see data.actions.ActionUpgradeCapital
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	
	public ActionUpgradeCapital createActionUpgradeCapital (Power powerConcerned) throws IllegalArgumentException {
		//check if Power have ActionPoints to use
		if (!powerConcerned.canPlay()) {
			logger.warn(powerConcerned.getName()+" dont have any ActionPoints left");
			throw new IllegalArgumentException("On ne possede plus aucun point d'action");
		}
		
		Capital capital = powerConcerned.getCapital();
		
		//check if capital is already level max
		if (capital.getLevel() == Capital.MAX_LEVEL) {
			logger.warn(powerConcerned.getName()+" try to go beyong his Capital limit");
			throw new IllegalArgumentException("La capitale est deja au niveau maximal");
		}
		
		//check if have enough resources
		int goldCost = capital.getUpgradeCost();
		if(powerConcerned.getResourceAmount(ResourceTypes.RESOURCE_GOLD) < goldCost) {
			logger.warn(powerConcerned.getName()+" try to upgrade his Capital without resource");
			throw new IllegalArgumentException("Pas assez de ressources pour ameliorer la capitale (cout : "+goldCost+")");
		}
		
		//conditions are met, so we can remove action cost and create the ActionUpgradeCapital
		powerConcerned.getResource(ResourceTypes.RESOURCE_GOLD).subValue(goldCost);

		if (!powerConcerned.isAI()) {
			powerConcerned.removeActionPoint();
		}
		logger.info(powerConcerned.getName()+" create an ActionUpgradeCapitale");
		//Logger.info(powerConcerned.getName()+" level capital"); pareil, dans sa fonction approprie
		return new ActionUpgradeCapital(powerConcerned);
	}
}


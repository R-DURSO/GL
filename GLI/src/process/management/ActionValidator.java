package process.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
 * and allows to create Actions to be performed at the end of the turn in the game.</p>
 * <p>No data class is modified here (except for resource values, including actions points)</p>
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @see {@link data.actions.Action Action}
 */
public class ActionValidator {
	private GameMap map;
	private static Logger Logger = LoggerUtility.getLogger(ActionValidator.class, "text");
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
		//check if one of players has already an allied
		if (powerConcerned.isAllied() || potentialAlly.isAllied()) {
			Logger.warn(" one power have already ally ");
			throw new IllegalArgumentException("Une des puissances est deja en alliance");
		}
		
		powerConcerned.removeActionPoint();
		Logger.info(powerConcerned.getName()+" remove action point ");
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
		//check if player has ally and if this ally is the right Power
		if(!powerConcerned.isAllied()) {
			Logger.warn(powerConcerned.getName()+" don't have ally ");
			throw new IllegalArgumentException(powerConcerned.getName() + " n'a pas d'allie");
		}
		if(powerConcerned.getAlly() != formerAlly) {
			Logger.warn(powerConcerned.getName()+" not alliance with "+formerAlly.getName());
			throw new IllegalArgumentException(powerConcerned.getName() + " n'est pas en alliance avec " + formerAlly.getName());
		}
		
		powerConcerned.removeActionPoint();
		Logger.info(powerConcerned.getName()+" remove action point ");
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
		if(from.equals(target)) {			
			Logger.warn(powerConcerned.getName()+" don't attack because you are case onwer's ");
			throw new IllegalArgumentException("On ne peut pas attaquer sur sa propre case");
		}
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if there is any unit on the fromBox
		if (!fromBox.hasUnit()) {
			Logger.warn(powerConcerned.getName()+" not unit for attack ");
			throw new IllegalArgumentException("Il n'y a pas d'unite pour lancer l'attaque");
		}
		
		Units unitsAtt = fromBox.getUnit();
		
		if (!targetBox.hasUnit()) {
			//Pas d'unite a attaquer, mais peut-etre un batiment
			if (targetBox instanceof WaterBox) {
				Logger.warn(powerConcerned.getName()+" attack in water case ");
				throw new IllegalArgumentException("Vous attaquez une case d'eau vide");
			}
			else {
				if (!( (GroundBox)fromBox).hasBuilding()) {
					Logger.warn(powerConcerned.getName()+" not construct or unit for fight ");
					throw new IllegalArgumentException("Il n'y a pas d'unite ou de batiment a attaquer");
				}
			}
		}
		
		//check if from Box is powerConcerned's
		if (unitsAtt.getOwner() != powerConcerned) {
			Logger.warn(powerConcerned.getName()+" is not here units ");
			throw new IllegalArgumentException("Ces unites n'appartiennent pas a " + powerConcerned.getName());
		}
		//check if units are on range
		if (!isUnitsOnRange(from, unitsAtt.getRange(), target)) {
			Logger.warn(powerConcerned.getName()+" units is out of range ");
			throw new IllegalArgumentException("Les unites sont trop loin de la cible");
		}
		//check if there is units on target, in this case, check the owner of those units
		//if player himself or his ally, no attack
		if ((targetBox.getOwner() == powerConcerned) || (targetBox.getOwner() == powerConcerned.getAlly())) {
			Logger.warn(powerConcerned.getName()+" don't attack your territory ");
			throw new IllegalArgumentException("Vous ne pouvez pas attaquer votre terrain ou celui d'un allie");
		}
		
		powerConcerned.removeActionPoint();
		Logger.info(powerConcerned.getName()+" remove action point ");
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
		//Check if you want to move to the same Box
		if (from.equals(target)) {
			Logger.warn(powerConcerned.getName()+" units is here ");
			throw new IllegalArgumentException("On ne peut pas se deplacer la ou on est deja");
		}
		
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if there is any Units on the fromBox
		if (!fromBox.hasUnit()) {
			Logger.warn(powerConcerned.getName()+" not units here");
			throw new IllegalArgumentException("Il n'y a pas d'unite a deplacer ici");
		}
		
		Units movingUnits = fromBox.getUnit();
		Power UnitsOwner = movingUnits.getOwner();
		
		if (UnitsOwner != powerConcerned) {
<<<<<<< HEAD
			Logger.warn(powerConcerned.getName()+" test move enemy units");
			throw new IllegalArgumentException("Vous essayer de bouger des unites qui ne vous appartiennent pas");
=======
			Logger.warn(powerConcerned.getName()+"test move enemy units");
			throw new IllegalArgumentException("Vous essayez de bouger des unites qui ne vous appartiennent pas");
		}
		
		if (movingUnits.getIsMoving()) {
			throw new IllegalArgumentException("Vous bougez des unites qui sont en train de se deplacer");
>>>>>>> branch 'master' of https://github.com/R-DURSO/GL
		}
		
		//check if units are on range
		if (!isUnitsOnRange(from, movingUnits.getMovement(), target)) {
			Logger.warn(powerConcerned.getName()+" units is over range of cible");
			throw new IllegalArgumentException("Les unites sont trop loin de la cible");
		}

		//check if there is "obstacle" on target : either wall / ennemy door, or units
		String pathFinding = pathFinding(from, movingUnits, target);
		if (pathFinding == null) {
			Logger.warn(powerConcerned.getName()+" impossible found path to destination");
			throw new IllegalArgumentException("Impossible de determiner un chemin jusqu'a la destination");
		}
		
		//check if there isn't any ennemy Unit or Different UnitTypes
		if (targetBox.hasUnit()) {
			//check if units already want to go to target, ergo if a PhantomUnit is on targetBox
			if(targetBox.getUnit() instanceof PhantomUnit) {
				Logger.warn(powerConcerned.getName()+" units could go here ");
				throw new IllegalArgumentException("Une unité compte se rendre à cette position");
			}
			if (targetBox.getOwner() == powerConcerned || targetBox.getOwner() == powerConcerned.getAlly()) {
				Units unitsOnTarget = targetBox.getUnit();
				if (unitsOnTarget.getTypes() == UnitTypes.UNIT_BOAT) {
					//moves Units on the boat
					Boat boatTarget = ((Boat)unitsOnTarget);
					if (boatTarget.hasContainedUnits()) {
						Units containedUnit = boatTarget.getContainedUnits();
						//You can re-group same Units Types
						if (containedUnit.getTypes() != movingUnits.getTypes()) {
							Logger.warn(powerConcerned.getName()+" diffent unit in this boat");
							throw new IllegalArgumentException("Des Unités de types différents sont dans ce bateau");
						}
						//But, make sure you dont exceed the limit
						else if (containedUnit.getNumber() + movingUnits.getNumber() > movingUnits.getMaxNumber()) {
							Logger.warn(powerConcerned.getName()+" don't move cause maxunit possibility");
							throw new IllegalArgumentException("Le déplacement fait dépasser la limite d'unite");
						}
					}
				}
				else if (movingUnits.getTypes() != unitsOnTarget.getTypes()) {
					Logger.warn(powerConcerned.getName()+" is different type of unit ");
					throw new IllegalArgumentException("Des unites d'un type different sont sur le lieu cible");
				}
				//But, make sure you dont exceed the limit
				else if (movingUnits.getNumber() + unitsOnTarget.getNumber() > movingUnits.getTypes()) {
					Logger.warn(powerConcerned.getName()+" move cause maxunit possibility");
					throw new IllegalArgumentException("Le déplacement fait dépasser la limite d'unite");
				}
			}
			else {
				Logger.warn(powerConcerned.getName()+" he have enemis units in is select case ");
				throw new IllegalArgumentException("Il y a des unites ennemies sur la case cible");
			}
		}
		
		powerConcerned.removeActionPoint();
		//set the unit in movement
		movingUnits.setIsMoving();
		//add phantom unit on the target box, to ensure that no other unit could go there
		targetBox.setUnit(new PhantomUnit(powerConcerned, movingUnits.getTypes()));
		Box[] ListBox = convertPathToBoxArray(pathFinding, from);
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
		boolean Allied;
		Power Ally = null;
		if (powerConcerned.isAllied()) {
			Ally = powerConcerned.getAlly();
			Allied = true;
		}
		else {
			Allied = false;
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
						//reset canVisit and checkUnit, we will see if we can go further
						checkUnit = false;
						canVisit = false;
						
						
						if (visitBox instanceof WaterBox) {
							//On the sea
							if (units.getTypes() == UnitTypes.UNIT_BOAT) {
								//A boat on the sea
								checkUnit = true;
							}
							else if (visitBox.hasUnit()) {
								//check if there is a boat you can move on
								if (visitBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
									//there is a Boat
									if (visitBox.getUnit().getOwner() == powerConcerned) {
										//You can go on your own boat
										checkUnit = true;
									}
									//Boat is controlled by another power
								}
							}
							//Not a Boat and there isn't a Unit, can't go through Water
						}
						else if ((visitBox instanceof GroundBox) && (units.getTypes() != UnitTypes.UNIT_BOAT)) {
							//A man on land
							GroundBox visitGBox = (GroundBox) visitBox;
							//if there is a Building, it can be a Wall or Capital
							if (visitGBox.hasBuilding()) {
								if (visitGBox.getBuilding().getType() != BuildingTypes.BUILDING_WALL) {
									//if it isn't a Wall, is it a Capital
									if (visitGBox.getBuilding().getType() != BuildingTypes.BUILDING_CAPITAL) {
										//if it isn't a Wall, is it a Door that we can go through
										if (visitGBox.getBuilding().getType() == BuildingTypes.BUILDING_DOOR) {
											//Does the Door belong to us
											if (visitGBox.getOwner() == powerConcerned) {
												checkUnit = true;
											}
											//Do we have an Ally
											else if (Allied) {
												//And does the Door belong to Ally
												if (visitGBox.getOwner() == powerConcerned.getAlly()) {
													checkUnit = true;
												}
											}
											//Cannot go trough enemy Door
										}
										else {
											//canVisit is true because it isn't a Wall, Door or Capital
											checkUnit = true;
										}
									}
									else {
										//does Capital belong to us
										if (visitGBox.getOwner() == powerConcerned) {
											checkUnit = true;
										}
									}
								}
								//canVisit stay false because it is a Wall blocking the path
							}
							//GroundBox without Building
							else {
								checkUnit = true;
							}
						}
						else if ((visitBox instanceof GroundBox) && (units.getTypes() == UnitTypes.UNIT_BOAT)) {
							//Boat that go to a coast to deposit Unit
							if (isNearWater(visitPosition)) {
								//Coast near Water you can go on
								checkUnit = true;
							}
						}
						
						if (checkUnit) {
							if (visitBox.hasUnit()) {
								Units visitUnit = visitBox.getUnit();
								if (visitBox.getUnit().getOwner() != powerConcerned) {
									if (Allied) {
										if (visitBox.getUnit().getOwner() == Ally) {
											//there is Ally unit, we can continue our visit, but can't stop here
											canVisit = true;
										}
										//Not our Ally
									}
									//There is only ennemy on this map
								}
								else {
									//Our own Box, with our Unit
									if (visitUnit.getTypes() == UnitTypes.UNIT_BOAT) {
										//Our own Boat
										if (visitPosition.equals(target)) {
											return path;
										}
										else {
											canVisit = true;
										}
									}
									else if (visitUnit.getTypes() == units.getTypes()) {
										//Same UnitTypes, we can go there
										if (visitPosition.equals(target)) {
											return path;
										}
										else {
											canVisit = true;
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
								//verification about the Box made earlier
								if (visitPosition.equals(target)) {
									return path;
								}
								else {
									canVisit = true;
								}
							}
							
							//check if the Box visited can be walk upon
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
							//end of the visit
						}
						//Added the Position to Visited and Remove it in toVisit 
						visited.add(visitPosition);
					}
					//visited has already seen visitPosition
				}
				//too far to reach
			}
			//No more visiting Boxes
		}
		//La node stock un code pour retrouver son chemin (String path)
		//Si on atteins ce point du code, le chemin n'a pas été trouvé
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
	private boolean isUnitsOnRange(Position from, int unitsMoveRange, Position target) {
		int aX = from.getX();
		int aY = from.getY();
		int bX = target.getX();
		int bY = target.getY();
		return ((getDifference(aX,bX) + getDifference(aY,bY)) <= unitsMoveRange);
	}
	
	/**
	 * @param a Entier
	 * @param b Entier
	 * @return La difference entre ces 2 entiers
	 */
	private int getDifference (int a, int b) {
		return Math.abs(a - b);
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
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			Logger.warn(powerConcerned.getName()+" don't construct in other power case ");
			throw new IllegalArgumentException("Impossible de construire sur une case etrangere");
		}
		
		//check if targetBox is a WaterBox or not
		if(targetBox instanceof WaterBox) {
			Logger.warn(powerConcerned.getName()+" don't construct in water case ");
			throw new IllegalArgumentException("Impossible de construire sur une case d'eau");
		}
		//check if have enough resources to build
		ResourceCost neededResource = getBuildingCost(buildingType); 
		if(!checkPrice(powerConcerned.getResourceAmount(neededResource.getType()), neededResource.getCost())) {
			Logger.warn(powerConcerned.getName()+" not this type units");
			throw new IllegalArgumentException("Pas assez de ressources pour construire ceci");
		}
		GroundBox groundBox = (GroundBox) targetBox;
		
		
		
		//check if there is already a building on this box
		if(groundBox.hasBuilding()) {
			//check if a building is going to be builded there, that is to say if instance of PhantomBuilding is on the targetBox
			if(groundBox.getBuilding() instanceof PhantomBuilding) {
				Logger.warn(powerConcerned.getName()+" other constrcut here ");
				throw new IllegalArgumentException("Un autre batiment va être construit sur cette case");
			}
			Logger.warn(powerConcerned.getName()+" one constrcut is here already");
			throw new IllegalArgumentException("Impossible de construire sur une case qui possede deja un batiment"); 
		}
		
		if (buildingType == BuildingTypes.BUILDING_DOCK) {
			if (!isNearWater(target)) {
				Logger.warn(powerConcerned.getName()+" dock is not beside water");
				throw new IllegalArgumentException("Le port n'est pas a poximite de case d'eau"); 
			}
		}
		
		if (buildingType == BuildingTypes.BUILDING_TEMPLE) {
			if (groundBox.getResourceType() != ResourceTypes.RESOURCE_ARTIFACT) {
				Logger.warn(powerConcerned.getName()+" this case doesn't have temple");
				throw new IllegalArgumentException("La case ne possede pas l'Artefact"); 
			}
		}
		
		powerConcerned.removeActionPoint();
		//remove building cost
		Logger.info(powerConcerned.getName()+" remove 1 actionpoint");
		powerConcerned.getResource(neededResource.getType()).subValue(neededResource.getCost());
		//add phantom building on the target box, to ensure that no other building could be constructed on targetBox
		groundBox.setBuilding(new PhantomBuilding());
		return new ActionConstruct(powerConcerned, buildingType, target);
	}
	
	/**
	 * check if nearby Boxes are made of Water
	 * @param target
	 * @return true if target is near Water
	 */
	private boolean isNearWater(Position target) {
		Box wBox = null;
		for (int d=0 ; d<=4 ; d++) {
			switch(d) {
			case 0:
				wBox = map.getBox(target);
				break;
			case 1:
				wBox = map.getBox(map.getUpPos(target));
				break;
			case 2:
				wBox = map.getBox(map.getLeftPos(target));
				break;
			case 3:
				wBox = map.getBox(map.getRightPos(target));
				break;
			case 4:
				wBox = map.getBox(map.getDownPos(target));
				break;
			default:
				wBox = null;
				break;
			}
			if (wBox != null) {
				if (wBox instanceof WaterBox) {
					return true;
				}
			}
		}
		return false;
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
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			Logger.warn(powerConcerned.getName()+" don't create unit in other power case");
			throw new IllegalArgumentException("Impossible de creer des unites sur une case etrangere");
		}
		
		//check if have enough gold to create units 
		ResourceCost neededResource = getUnitCost(powerConcerned, unitType, numberUnits);
		if(!checkPrice(powerConcerned.getResourceAmount(neededResource.getType()), neededResource.getCost())) {
			Logger.warn(powerConcerned.getName()+" not ressource for buy unit ");
			throw new IllegalArgumentException("Pas assez de ressources pour creer ces unites");
		}
				
		//check if not in water
		if(targetBox instanceof WaterBox) {
			Logger.warn(powerConcerned.getName()+" not create unit in water's case ");
			throw new IllegalArgumentException("Impossible de creer des unites sur une case d'eau");
		}
		GroundBox groundBox = (GroundBox) targetBox;
		boolean createPhantom = true;
		//check if there is unit
		if (groundBox.hasUnit()) {
			//check if same unitType (for grouping)
			if (groundBox.getUnit().getTypes() != unitType) {
				Logger.warn(powerConcerned.getName()+" different unit in this same case ");
				throw new IllegalArgumentException("des unites differentes sont presents sur la case");
			}
			else {
				createPhantom = false;
			}
		}
		//check if this box has a building
		if(!groundBox.hasBuilding()) {
			Logger.warn(powerConcerned.getName()+" need special building for this units");
			throw new IllegalArgumentException("La creation d'unites demande la presence d'un batiment specifique");
		}
		Building building = groundBox.getBuilding();
		
		//check if thoses units can be created here
		if(! (building instanceof BuildingArmy)) {
			Logger.warn(powerConcerned.getName()+" impossible create unit here batiment ");
			throw new IllegalArgumentException("Impossible de creer des unites dans ce batiment");
		}
		else {
			//we need to know if this building is enabled (or if Building#buildTime == 0)
			if(building.getBuildTime() > 0) {
				Logger.warn(powerConcerned.getName()+" construct is not able");
				throw new IllegalArgumentException("Ce batiment n'est pas encore utilisable");
			}
			switch(building.getType()) {
			case BuildingTypes.BUILDING_BARRACK:
				if(unitType >= UnitTypes.UNITS_IN_BARRACK || unitType < 0) {
					Logger.warn(powerConcerned.getName()+" units is not create in barrack");
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des casernes");
				}
				break;
			case BuildingTypes.BUILDING_WORKSHOP:
				if(unitType < UnitTypes.UNITS_IN_BARRACK || unitType >= UnitTypes.UNITS_IN_WORKSHOP) {
					Logger.warn(powerConcerned.getName()+" unit is not create in workshop");
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des ateliers");
				}
				break;
			case BuildingTypes.BUILDING_DOCK:
				if(unitType < UnitTypes.UNITS_IN_WORKSHOP) {
					Logger.warn(powerConcerned.getName()+" unit is not create in port");
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des ports");
				}
				break;
			default:
				Logger.warn(powerConcerned.getName()+" not this type units");
				throw new IllegalArgumentException("Il n'y a pas d'unite de ce type");	
			}
		}
		
		powerConcerned.removeActionPoint();
		if (createPhantom) {
			targetBox.setUnit(new PhantomUnit(powerConcerned, unitType));
		}
		powerConcerned.getResource(neededResource.getType()).subValue(neededResource.getCost());
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
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			Logger.warn(powerConcerned.getName()+" not case's onwer");
			throw new IllegalArgumentException("Impossible de faire une destruction sur une case qui ne nous appartient pas");
		}
		
		//check if there is indeed a building here
		//we first need to know the type of the box
		if(targetBox instanceof WaterBox) {
			Logger.warn(powerConcerned.getName()+" water case");
			throw new IllegalArgumentException("Il n'y a rien a detruire sur une case d'eau");
		}
		else {
			GroundBox groundBox = (GroundBox) targetBox;
			if(!groundBox.hasBuilding()) {
				Logger.warn(powerConcerned.getName()+" not building here ");
				throw new IllegalArgumentException("Il n'y a pas de building sur cette case");
			}
			//check if the building is the Capital (we don't allow player to destroy his own Capital)
			Building building = groundBox.getBuilding();
			if(building instanceof Capital) {
				Logger.warn(powerConcerned.getName()+" don't destroy Capital");
				throw new IllegalArgumentException("Vous ne pouvez pas détruire votre capitale");
			}
		}
		powerConcerned.removeActionPoint();
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
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		Units units = targetBox.getUnit();
		//check if powerConcerned owns those units
		if(units.getOwner() != powerConcerned) {
			Logger.warn(powerConcerned.getName()+" not unit's onwer");
			throw new IllegalArgumentException("Ces unités ne vous appartiennent pas");
		}
		//check if there is any unit on this box
		if(!targetBox.hasUnit()) {
<<<<<<< HEAD
			Logger.warn(powerConcerned.getName()+" not unit here "+this);
=======
			Logger.warn(powerConcerned.getName()+"not unit here ");
>>>>>>> branch 'master' of https://github.com/R-DURSO/GL
			throw new IllegalArgumentException("Il n'y a pas d'unites a supprimer ici");
		}
		powerConcerned.removeActionPoint();
		Logger.info(powerConcerned.getName()+" remove 1 actionPoint ");
		Logger.info(powerConcerned.getName()+" destoy unit  position x "+target.getX()+"y "+target.getY());
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
		
		Capital capital = powerConcerned.getCapital();
		
		//check if capital is already level max
		if (capital.getLevel() == Capital.MAX_LEVEL) {
			Logger.warn(powerConcerned.getName()+" max level capital");
			throw new IllegalArgumentException("La capitale est deja au niveau maximal");
			
		}
		
		//check if have enough resources
		int goldCost = capital.getUpgradeCost();
		if(powerConcerned.getResourceAmount(ResourceTypes.RESOURCE_GOLD) < goldCost) {
			Logger.warn(powerConcerned.getName()+" max level capital");
			throw new IllegalArgumentException("Pas assez de ressources pour ameliorer la capitale (cout : " + goldCost + ")");
		}
		
		//conditions are met, so we can remove action cost and create the ActionUpgradeCapital
				powerConcerned.getResource(ResourceTypes.RESOURCE_GOLD).subValue(goldCost);
		
		powerConcerned.removeActionPoint();
		Logger.info(powerConcerned.getName()+" remove 1 actionPoint ");
		Logger.info(powerConcerned.getName()+" level capital");
		return new ActionUpgradeCapital(powerConcerned);
	}
}

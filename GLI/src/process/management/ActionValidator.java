package process.management;

import java.util.ArrayList;
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

/**
 * This class ensures that every action the player does is possible 
 * and allows to create Actions to be performed at the end of the turn in the game.
 * No data class is modified here (except for resource values, including actions points)
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @see data.actions.Action
 */
public class ActionValidator {
	private GameMap map;

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
			throw new IllegalArgumentException("Une des puissances est deja en alliance");
		}
		
		powerConcerned.removeActionPoint();
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
		if(!powerConcerned.isAllied())
			throw new IllegalArgumentException(powerConcerned.getName() + " n'a pas d'allie");
		if(powerConcerned.getAlly() != formerAlly) {
			throw new IllegalArgumentException(powerConcerned.getName() + " n'est pas en alliance avec " + formerAlly.getName());
		}
		
		powerConcerned.removeActionPoint();
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
		if(from.equals(target))
			throw new IllegalArgumentException("On ne peut pas attaquer sur sa propre case");
		
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if from Box is powerConcerned's
		if (fromBox.getOwner() != powerConcerned)
			throw new IllegalArgumentException("Cette case n'appartient pas a " + powerConcerned.getName());
		
		//check if there is any unit on the fromBox
		if (!fromBox.hasUnit()) {
			throw new IllegalArgumentException("Il n'y a pas d'unite pour lancer l'attaque");
		}
		//check if units are on range
		if (!isUnitsOnRange(from, fromBox.getUnit().getRange(), target)) {
			throw new IllegalArgumentException("Les unites sont trop loin de la cible");
		}
		//check if there is units on target, in this case, check the owner of those units
		//if player himself or his ally, no attack
		if (!targetBox.hasUnit() || targetBox.getOwner() == powerConcerned || targetBox.getOwner() == powerConcerned.getAlly()) {
			throw new IllegalArgumentException("Vous ne pouvez pas attaquer ici");
		}
		
		powerConcerned.removeActionPoint();
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
	 * @see data.actions.ActionMove
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionMove createActionMove (Power powerConcerned, Position from, Position target) throws IllegalArgumentException {
		//Check if you want to move to the same Box
		if (from.equals(target)) {
			throw new IllegalArgumentException("On ne peut pas se deplacer la ou on est deja");
		}
		
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if from Box is powerConcerned's
		if (fromBox.getOwner() != powerConcerned) {
			throw new IllegalArgumentException("Cette case n'appartient pas a " + powerConcerned.getName());
		}
		//check if there is any Units on the fromBox
		if (!fromBox.hasUnit()) {
			throw new IllegalArgumentException("Il n'y a pas d'unite a deplacer ici");
		}
		
		Units movingUnits = fromBox.getUnit();
		
		//check if units are on range
		if (!isUnitsOnRange(from, movingUnits.getMovement(), target)) {
			throw new IllegalArgumentException("Les unites sont trop loin de la cible");
		}

		//check if there is "obstacle" on target : either wall / ennemy door, or units
		//TODO Pathfinding ne vérifie pas la présence d'unité, donc le survoler des annemis est possible
		if (!pathFinding(from, movingUnits, target)) {
			throw new IllegalArgumentException("Impossible de déterminer un chemin jusqu'à la destination");
		}
		
		//check if there isn't any ennemy Unit or Different UnitTypes
		if (targetBox.hasUnit()) {
			if (targetBox.getOwner() == powerConcerned) {
				Units unitsOnTarget = targetBox.getUnit();
				if (movingUnits.getTypes() != unitsOnTarget.getTypes()) {
					throw new IllegalArgumentException("Des unites d'un type different sont sur le lieu cible");
				}
			}
			else {
				throw new IllegalArgumentException("Il y a des unites ennemies sur la case cible");
			}
		}
		
		powerConcerned.removeActionPoint();
		return new ActionMove(powerConcerned, from, target);
	}
	
	/**
	 * 
	 * @param from Starting Box
	 * @param unitsMovement Movement Point of the Unit
	 * @param target Box where the Units wants to go
	 * @return true if Units have a path
	 */
	private boolean pathFinding(Position from, Units units, Position target) {
		Power power = map.getBox(from).getOwner();
		//TODO int unitsMovement = units.getMovement();
		//2 ArrayList created, to stock the next Box to visit and one for those visited
		ArrayList<Position> toVisit = new ArrayList<Position>();
		boolean hasVisited;
		ArrayList<Position> visited = new ArrayList<Position>();
		//Adding the Starting Box
		toVisit.add(from);
		for (Iterator<Position> i = toVisit.iterator(); i.hasNext(); ) {
			Position data = i.next();
			Box dataMap = map.getBox(data);
			if (!visited.contains(data)) {
				hasVisited = false;
				if ((dataMap instanceof WaterBox) && (units.getTypes() == UnitTypes.UNIT_BOAT)) {
					//A boat on the sea
					if (data.equals(target)) {
						return true;
					}
					else {
						hasVisited = true;
					}
				}
				else if ((dataMap instanceof GroundBox) && (units.getTypes() != UnitTypes.UNIT_BOAT)) {
					//A man on land
					GroundBox gdataMap = (GroundBox) dataMap;
					if (gdataMap.hasBuilding()) {
						if (gdataMap.getBuilding().getType() != BuildingTypes.BUILDING_WALL) {
							if ( (gdataMap.getBuilding().getType() == BuildingTypes.BUILDING_DOOR)
								&& ( (gdataMap.getOwner() == power) || (gdataMap.getOwner() == power.getAlly()) )) {
								if (data.equals(target)) {
									return true;
								}
								else {
									hasVisited = true;
								}
							}
						}
					}
					else if (data.equals(target)) {
						return true;
					}
					else {
						hasVisited = true;
					}
				}
				//check if the Box visited can be walk upon
				if (hasVisited) {
					visited.add(data);
					Position dataToAdd;
					for (int d=1 ; d<=4 ; d++) {
						switch(d) {
						case 1:
							dataToAdd = map.getUpPos(data);
							break;
						case 2:
							dataToAdd = map.getLeftPos(data);
							break;
						case 3:
							dataToAdd = map.getRightPos(data);
							break;
						case 4:
							dataToAdd = map.getDownPos(data);
							break;
						default:
							dataToAdd = null;
							break;
						}
						if (dataToAdd != null) {
							toVisit.add(dataToAdd);
						}
					}
				}
			}
		}
		//TODO l'unité qui passe devrait aussi conquérir le territoire qu'il survole ?
		//Pousser la node à noter un code pour retrouver son chemin ?
		//Garder de côté les cases passés
		return false;
	}
	
	private boolean isUnitsOnRange(Position from, int unitsMoveRange, Position target) {
		int aX = from.getX();
		int aY = from.getX();
		int bX = from.getX();
		int bY = from.getX();
		return ((getDifference(aX,bX) + getDifference(aY,bY)) <= unitsMoveRange);
	}
	
	/**
	 * @param a Entier
	 * @param b Entier
	 * @return La différence entre ces 2 entiers
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
	public ActionConstruct createActionConstruct(Power powerConcerned, int buildingType, Position target) throws IllegalArgumentException{
		//check if target belongs to powerConcerned
		Box targetBox = getBoxFromMap(target);
		if(targetBox.getOwner() != powerConcerned) {
			throw new IllegalArgumentException("Impossible de construire sur une case etrangere");
		}
		
		//check if have enough resources to build
		ResourceCost neededResource = getBuildingCost(buildingType); 
		if(!checkPrice(powerConcerned.getResourceAmount(neededResource.getType()), neededResource.getCost()))
			throw new IllegalArgumentException("Pas assez de ressources pour construire ceci");
		
		//check if targetBox is a WaterBox or not
		if(targetBox instanceof WaterBox)
			throw new IllegalArgumentException("Impossible de construire sur une case d'eau");
		
		GroundBox groundBox = (GroundBox) targetBox;
		//check if there is already a building on this box
		if(groundBox.hasBuilding())
			throw new IllegalArgumentException("Impossible de construire sur une case qui possede deja un batiment"); 
		
		if (buildingType == BuildingTypes.BUILDING_TEMPLE) {
			if (groundBox.getResourceType() != ResourceTypes.RESOURCE_ARTIFACT) {
				throw new IllegalArgumentException("La case ne possede pas l'Artefact"); 
			}
		}
		
		powerConcerned.removeActionPoint();
		//remove building cost
		powerConcerned.getResource(neededResource.getType()).subValue(neededResource.getCost());
		return new ActionConstruct(powerConcerned, buildingType, target);
	}
	
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
			throw new IllegalArgumentException("Impossible de creer des unites sur une case etrangere");
		}
		
		//check if have enough gold to create units 
		ResourceCost neededResource = getUnitCost(powerConcerned, unitType, numberUnits);
		if(!checkPrice(powerConcerned.getResourceAmount(neededResource.getType()), neededResource.getCost()))
			throw new IllegalArgumentException("Pas assez de ressources pour creer ces unites");
				
		
		//check if not in water
		if(targetBox instanceof WaterBox)
			throw new IllegalArgumentException("Impossible de creer des unites sur une case d'eau");
		
		GroundBox groundBox = (GroundBox) targetBox;
		//check if this box has a building
		if(!groundBox.hasBuilding())
			throw new IllegalArgumentException("La creation d'unites demande la presence d'un batiment specifique");
		
		Building building = groundBox.getBuilding();
		
		//check if thoses units can be created here
		if(! (building instanceof BuildingArmy))
			throw new IllegalArgumentException("Impossible de creer des unites dans un batiment comme ceci");
		else
			switch(building.getType()) {
			case BuildingTypes.BUILDING_BARRACK:
				if(unitType >= UnitTypes.UNITS_IN_BARRACK)
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des casernes");
				break;
			case BuildingTypes.BUILDING_WORKSHOP:
				if(unitType < UnitTypes.UNITS_IN_BARRACK || unitType >= UnitTypes.UNITS_IN_WORKSHOP)
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des ateliers");
				break;
			case BuildingTypes.BUILDING_DOCK:
				if(unitType < UnitTypes.UNITS_IN_WORKSHOP)
					throw new IllegalArgumentException("Ces unites ne sont pas creees dans des ports");
				break;
			default:
				throw new IllegalArgumentException("Il n'y a pas d'unitÃ© de ce type");	
			}
		
		
		powerConcerned.removeActionPoint();
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
			throw new IllegalArgumentException("Impossible de faire une destruction sur une case qui ne nous appartient pas");
		}
		
		//check if there is indeed a building here
		//we first need to know the type of the box
		if(targetBox instanceof WaterBox)
			throw new IllegalArgumentException("Il n'y a rien a detruire sur une case d'eau");
		else {
			GroundBox groundBox = (GroundBox) targetBox;
			if(!groundBox.hasBuilding())
				throw new IllegalArgumentException("Il n'y a pas de building sur cette case");
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
		if(targetBox.getOwner() != powerConcerned)
			throw new IllegalArgumentException("Impossible de faire une suppression d'unite sur une case qui ne nous appartient pas");
		
		//check if there is any unit on this box
		if(!targetBox.hasUnit())
			throw new IllegalArgumentException("Il n'y a pas d'unites a supprimer ici");
		
		powerConcerned.removeActionPoint();
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
			throw new IllegalArgumentException("La capitale est deja au niveau maximal");
		}
		
		//check if have enough resources
		int goldCost = capital.getUpgradeCost();
		if(powerConcerned.getResourceAmount(ResourceTypes.RESOURCE_GOLD) < goldCost) {
			throw new IllegalArgumentException("Pas assez de ressources pour ameliorer la capitale (cout : " + goldCost + ")");
		}
		
		//conditions are met, so we can remove action cost and create the ActionUpgradeCapital
				powerConcerned.getResource(ResourceTypes.RESOURCE_GOLD).subValue(goldCost);
		
		powerConcerned.removeActionPoint();
		return new ActionUpgradeCapital(powerConcerned);
	}
}

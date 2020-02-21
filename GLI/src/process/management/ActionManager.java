package process.management;

import data.GameMap;
import data.Position;
import data.Power;
import data.actions.ActionAttack;
import data.actions.ActionBreakAlliance;
import data.actions.ActionConstruct;
import data.actions.ActionCreateUnit;
import data.actions.ActionDestroyBuilding;
import data.actions.ActionDestroyUnits;
import data.actions.ActionMakeAlliance;
import data.actions.ActionMove;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.boxes.WaterBox;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.BuildingArmy;
import data.building.special.Door;
import data.building.special.Wall;
import data.unit.UnitTypes;
import data.unit.Units;

/**
 * This class ensures that every action the player does is possible 
 * and allows to create Actions to be performed at the end of the turn in the game.
 * No data class is modified here.
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @see data.actions.Action
 *
 */
public class ActionManager {
	private GameMap map;

	/**
	 * ActionManager just needs to have access to the game map
	 * @param map The game map
	 */
	public ActionManager(GameMap map) {
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
			throw new IllegalArgumentException("Une des puissances est déjà en alliance");
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
			throw new IllegalArgumentException(powerConcerned.getName() + " n'a pas d'allié");
		if(powerConcerned.getAllied() != formerAlly) {
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
	 * @param powerConcerned The player who in hand
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
		if(fromBox.getOwner() != powerConcerned)
			throw new IllegalArgumentException("Cette case n'appartient pas à " + powerConcerned.getName());
		
		//check if there is any unit on the fromBox
		if(!fromBox.hasUnit())
			throw new IllegalArgumentException("Il n'y a pas d'unité à déplacer ici");
		
		//check if units are on range
		if(!isUnitsOnRange(from, fromBox.getUnit(), target))
			throw new IllegalArgumentException("Les unités sont trop loin de la cible");
		
		//check if there is units on target, in this case, check the owner of those units
		//if player himself or his ally, no attack
		if(targetBox.hasUnit() || targetBox.getOwner() == powerConcerned || targetBox.getOwner() == powerConcerned.getAllied())
			throw new IllegalArgumentException("Vous ne pouvez pas attaquer ici");
		
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
	 * @param powerConcerned The player who in hand
	 * @param from The position where player's units will move
	 * @param target Where player's units will move
	 * @return ActionMove
	 * @see data.actions.ActionMove
	 * @throws IllegalArgumentException If the conditions are not met
	 */
	public ActionMove createActionMove(Power powerConcerned, Position from, Position target) throws IllegalArgumentException{
		if(from.equals(target))
			throw new IllegalArgumentException("On ne peut pas se déplacer la ou on est déjà");
		
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		
		//check if from Box is powerConcerned's
		if(fromBox.getOwner() != powerConcerned)
			throw new IllegalArgumentException("Cette case n'appartient pas à " + powerConcerned.getName());
		
		
		//check if there is any unit on the fromBox
		if(!fromBox.hasUnit())
			throw new IllegalArgumentException("Il n'y a pas d'unité à déplacer ici");
		
		Units movingUnits = fromBox.getUnit();
		
		//check if unit can go on this box (water or ground) 
		//only a boat can go on water
		if(targetBox instanceof WaterBox && movingUnits.getTypes() != UnitTypes.UNIT_BOAT
			|| targetBox instanceof GroundBox && movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
			
			throw new IllegalArgumentException("Cette unité ne peut pas aller sur ce type de case");
		}
		
		//check if units are on range
		if(!isUnitsOnRange(from, movingUnits, target))
			throw new IllegalArgumentException("Les unités sont trop loin de la cible");
		
		
		//check if there is "obstacle" on target : either wall / ennemy door, or units 
		if(targetBox.getOwner() == powerConcerned) {
			if(targetBox.hasUnit()) {
				Units unitsOnTarget = targetBox.getUnit();
				if(movingUnits.getTypes() != unitsOnTarget.getTypes())
					throw new IllegalArgumentException("Des unités d'un type différent sont sur le lieu ciblé");
			}
			
			if(targetBox instanceof GroundBox) {
				GroundBox groundBox = (GroundBox) targetBox;
				if(groundBox.getBuilding() instanceof Wall)
					throw new IllegalArgumentException("Les unités ne peuvent pas aller dans un mur");
			}
		}else {
			if(targetBox.hasUnit())
				throw new IllegalArgumentException("Il y a des unités ennemies sur la case cible");
			if(targetBox instanceof GroundBox) {
				GroundBox groundBox = (GroundBox) targetBox;
				if(groundBox.getBuilding() instanceof Wall)
					throw new IllegalArgumentException("Les unités ne peuvent pas aller dans un mur");
				//units can go through "allied" doors
				if(groundBox.getBuilding() instanceof Door && groundBox.getOwner() == powerConcerned.getAllied())
					throw new IllegalArgumentException("Les unités ne peuvent pas aller dans une porte ennemie");
			}
		}
		
		powerConcerned.removeActionPoint();
		return new ActionMove(powerConcerned, from, target);
	}
	
	private boolean isUnitsOnRange(Position from, Units units, Position target) {
		int aX = from.getX() - units.getRange();
		int aY = from.getY();
		int bX = from.getX();
		int bY = from.getY() - units.getRange();
		int bYPrime = from.getY() + units.getRange();
		int cX = from.getX() + units.getRange();
		int cY = from.getY();
		
		return isInTriangle(aX, aY, bX, bY, cX, cY, target.getX(), target.getY())
				|| isInTriangle(aX, aY, bX, bYPrime, cX, cY, target.getX(), target.getY());
	}
	
	private double calculateArea(int aX, int aY, int bX, int bY, int cX, int cY) {
		return Math.abs((aX*(bY-cY) + bX*(cY-aY) + cX*(aY-bY) ) / 2.0);
	}
	
	private boolean isInTriangle(int aX, int aY, int bX, int bY, int cX, int cY, int posX, int posY) {
		double ABCTriangle = calculateArea(aX, aY, bX, bY, cX, cY);
		double PBCTriangle = calculateArea(posX, posY, bX, bY, cX, cY);
		double APCTriangle = calculateArea(aX, aY, posX, posY, cX, cY);
		double ABPTriangle = calculateArea(aX, aY, bX, bY, posX, posY);
		
		return (ABCTriangle == PBCTriangle + APCTriangle + ABPTriangle);
	}
	
	
	/**
	 * Check if:
	 * <ul>
	 * 	<li>target position belongs to powerConcerned</li>
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
		if(targetBox.getOwner() == powerConcerned) {
			throw new IllegalArgumentException("Impossible de construire sur une case étrangère");
		}
		
		//check if targetBox is a WaterBox or not
		if(targetBox instanceof WaterBox)
			throw new IllegalArgumentException("Impossible de construire sur une case d'eau");
		
		GroundBox groundBox = (GroundBox) targetBox;
		//check if there is already a building on this box
		if(groundBox.hasBuilding())
			throw new IllegalArgumentException("Impossible de construire sur une case qui possède déjà un batiment"); 
		
		powerConcerned.removeActionPoint();
		return new ActionConstruct(powerConcerned, buildingType, target);
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
		if(targetBox.getOwner() == powerConcerned) {
			throw new IllegalArgumentException("Impossible de créer des unités sur une case étrangère");
		}
		
		//check if not in water
		if(targetBox instanceof WaterBox)
			throw new IllegalArgumentException("Impossible de créer des unités sur une case d'eau");
		
		GroundBox groundBox = (GroundBox) targetBox;
		//check if this box has a building
		if(!groundBox.hasBuilding())
			throw new IllegalArgumentException("La création d'unités demande la présence d'un batiment spécifique");
		
		Building building = groundBox.getBuilding();
		
		//check if thoses units can be created here
		if(! (building instanceof BuildingArmy))
			throw new IllegalArgumentException("Impossible de créer des unités dans un batiment comme ceci");
		else
			switch(building.getType()) {
			case BuildingTypes.BUILDING_BARRACK:
				if(unitType >= UnitTypes.UNITS_IN_BARRACK)
					throw new IllegalArgumentException("Ces unités ne sont pas créées dans des casernes");
				break;
			case BuildingTypes.BUILDING_WORKSHOP:
				if(unitType < UnitTypes.UNITS_IN_BARRACK || unitType >= UnitTypes.UNITS_IN_WORKSHOP)
					throw new IllegalArgumentException("Ces unités ne sont pas créées dans des ateliers");
				break;
			case BuildingTypes.BUILDING_DOCK:
				if(unitType < UnitTypes.UNITS_IN_WORKSHOP)
					throw new IllegalArgumentException("Ces unités ne sont pas créées dans des ports");
				break;
			}
		
		
		powerConcerned.removeActionPoint();
		return new ActionCreateUnit(powerConcerned, unitType, numberUnits, target);
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
		if(targetBox.getOwner() == powerConcerned) {
			throw new IllegalArgumentException("Impossible de faire une destruction sur une case qui ne nous appartient pas");
		}
		
		//check if there is indeed a building here
		//we first need to know the type of the box
		if(targetBox instanceof WaterBox)
			throw new IllegalArgumentException("Il n'y a rien à détruire sur une case d'eau");
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
		if(targetBox.getOwner() == powerConcerned)
			throw new IllegalArgumentException("Impossible de faire une suppression d'unité sur une case qui ne nous appartient pas");
		
		//check if there is any unit on this box
		if(!targetBox.hasUnit())
			throw new IllegalArgumentException("Il n'y a pas d'unités à supprimer ici");
		
		powerConcerned.removeActionPoint();
		return new ActionDestroyUnits(powerConcerned, target);
	}
	
	private Box getBoxFromMap(Position position) {
		return map.getBox(position.getX(), position.getY());
	}
}

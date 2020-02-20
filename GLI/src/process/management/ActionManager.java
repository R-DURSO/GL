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
import data.building.BuildingTypes;
import data.building.special.Door;
import data.building.special.Wall;
import data.unit.UnitTypes;
import data.unit.Units;

/**
 * 
 * @author Aldric Vitali Silvestre
 *
 */
public class ActionManager {
	private GameMap map;

	public ActionManager(GameMap map) {
		this.map = map;
	}
	
	public ActionMakeAlliance createActionMakeAlliance(Power powerConcerned, Power potentialAlly) throws IllegalArgumentException{
		//check if one of players has already an allied
		if (powerConcerned.isAllied() || potentialAlly.isAllied()) {
			throw new IllegalArgumentException("Une des puissances est déjà en alliance");
		}
		
		powerConcerned.removeActionPoint();
		return new ActionMakeAlliance(powerConcerned, potentialAlly);
	}
	
	public ActionBreakAlliance createActionBreakAlliance(Power powerConcerned, Power formerAlly) {
		//check if player has ally and if this ally is the right Power
		if(!powerConcerned.isAllied())
			throw new IllegalArgumentException(powerConcerned.getName() + " n'a pas d'allié");
		if(powerConcerned.getAllied() != formerAlly) {
			throw new IllegalArgumentException(powerConcerned.getName() + " n'est pas en alliance avec " + formerAlly.getName());
		}
		
		powerConcerned.removeActionPoint();
		return new ActionBreakAlliance(powerConcerned);
	}
	
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
	
	public ActionCreateUnit createActionCreateUnit(Power powerConcerned, int unitType, int numberUnits, Position target) throws IllegalArgumentException{
		
		powerConcerned.removeActionPoint();
		return new ActionCreateUnit(powerConcerned, unitType, numberUnits, target);
	}
	
	public ActionDestroyBuilding createActionDestroyBuilding(Power powerConcerned, Position target) throws IllegalArgumentException{
		
		powerConcerned.removeActionPoint();
		return new ActionDestroyBuilding(powerConcerned, target);
	}
	
	public ActionDestroyUnits createActionDestroyUnits(Power powerConcerned, Position target) throws IllegalArgumentException{
		
		powerConcerned.removeActionPoint();
		return new ActionDestroyUnits(powerConcerned, target);
	}
	
	private Box getBoxFromMap(Position position) {
		return map.getBox(position.getX(), position.getY());
	}
	
	private Units getUnitFromMap(Position position) {
		return getBoxFromMap(position).getUnit(); 
	}


}

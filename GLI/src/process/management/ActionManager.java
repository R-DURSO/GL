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
		Box fromBox = getBoxFromMap(from);
		Box targetBox = getBoxFromMap(target);
		//check if from Box is powerConcerned's
		if(fromBox.getOwner() != powerConcerned)
			throw new IllegalArgumentException("Cette case n'appartient pas à " + powerConcerned.getName());
		//check if units are on range
		if(!isUnitsOnRange(from, target))
			throw new IllegalArgumentException("Les unités sont trop loin de la cible");
		
		//check if there is units on target, in this case, check the owner of those units
		if(targetBox) {
			
		}
		
		return new ActionAttack(powerConcerned, from, target);
	}


	public ActionMove createActionMove(Power powerConcerned, Position from, Position target) throws IllegalArgumentException{
		
	}
	
	public ActionConstruct createActionConstruct(Power powerConcerned, int buildingType, Position target) throws IllegalArgumentException{
		
	}
	
	public ActionCreateUnit createActionCreateUnit(Power powerConcerned, int unitType, int numberUnits, Position target) throws IllegalArgumentException{
		
	}
	
	public ActionDestroyBuilding createActionDestroyBuilding(Power powerConcerned, Position target) throws IllegalArgumentException{
		
	}
	
	public ActionDestroyUnits createActionDestroyUnits(Power powerConcerned, Position target) throws IllegalArgumentException{
	
	}
	
	private Box getBoxFromMap(Position position) {
		return map.getBox(position.getX(), position.getY());
	}
	
	private Units getUnitFromMap(Position position) {
		return getBoxFromMap(position).getUnit(); 
	}
	
	private boolean isUnitsOnRange(Position from, Position target) {
		return false;
	}

}

package process.management;

import data.GameMap;
import data.Power;
import data.actions.ActionAttack;
import data.actions.ActionBreakAlliance;
import data.actions.ActionConstruct;
import data.actions.ActionCreateUnit;
import data.actions.ActionDestroyBuilding;
import data.actions.ActionDestroyUnits;
import data.actions.ActionMakeAlliance;
import data.actions.ActionMove;

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
	
	public ActionAttack createActionAttack(Power powerConcerned, int positionX, int positionY) throws IllegalArgumentException{
		
	}
	
	public ActionMove createActionMove(Power powerConcerned, int positionX, int positionY) throws IllegalArgumentException{
		
	}
	
	public ActionConstruct createActionConstruct(Power powerConcerned, int buildingType, int positionX, int positionY) throws IllegalArgumentException{
		
	}
	
	public ActionCreateUnit createActionCreateUnit(Power powerConcerned, int unitType, int numberUnits, int positionX, int positionY) throws IllegalArgumentException{
		
	}
	
	public ActionDestroyBuilding createActionDestroyBuilding(Power powerConcerned, int positionX, int positionY) throws IllegalArgumentException{
		
	}
	
	public ActionDestroyUnits createActionDestroyUnits(Power powerConcerned, int positionX, int positionY) throws IllegalArgumentException{
	
	}

}

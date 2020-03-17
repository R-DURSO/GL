package process.game;
import java.util.ArrayList;

import GUI.components.menu.PreferencesPanel;
import data.actions.*;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.building.special.Capital;
import data.GameMap;
import data.Position;
import data.Power;
import data.resource.*;
import process.management.ActionValidator;
import process.management.BuildingManager;
import process.management.MapBuilder;
import process.management.UnitManager;

public class GameLoop {
	
	@SuppressWarnings("unchecked")
	private ArrayList<Action> actions[]= (ArrayList<Action>[]) new ArrayList[ActionTypes.NUMBER_ACTIONS];
	
	// constante temporaire 
	private boolean isPlaying = true;
	private Power powers[];
	GameMap map;
	
	public GameLoop(GameMap map, Power powers[] ) {
		initActionArray();
		this.map = map;
		this.powers = powers;
	}
	
	public void addAction(int actionType, Action action) {
		actions[actionType].add(action);
	}
	

	public void play() {

		//the main game loop, which won't end unitl game is finished, or player wants to quit
		while(isPlaying) {
			
			//for player 1, who is the unique human
			while(canContinueTurn(powers[0].getResource(ResourceTypes.RESOURCE_ACTIONS))) {
				//window.doSomething()
				
			}
		
			//for others non-human players 
			for( int i = 1 ; i < powers.length;  i++) {
				
				//they will do all actions thay can 
				while(canContinueTurn(powers[i].getResource(ResourceTypes.RESOURCE_ACTIONS))) {
					// test des action possible 


				}
			}
		}
	}
	
	public void endTurn() {
		doActions();
	}
	
	public void doActions() {
	
		for(int i = 0; i < ActionTypes.NUMBER_ACTIONS; i++) {
			switch(i) {
			case ActionTypes.ACTION_ATTACK:
				executeActionsAttack(actions[i]);
				break;
			case ActionTypes.ACTION_BREAK_ALLIANCE:
				executeActionsBreakAlliance(actions[i]);
				break;
			case ActionTypes.ACTION_CONSTRUCT:
				executeActionsConstruct(actions[i]);
				break;
			case ActionTypes.ACTION_CREATE_UNITS:
				executeActionsCreateUnits(actions[i]);
				break;
			case ActionTypes.ACTION_DESTROY_BUILDING:
				executeActionsDestroyBuilding(actions[i]);
				break;
			case ActionTypes.ACTION_DESTROY_UNITS:
				executeActionsDestroyUnits(actions[i]);
				break;
			case ActionTypes.ACTION_MAKE_ALLIANCE:
				executeActionsMakeAlliance(actions[i]);
				break;
			case ActionTypes.ACTION_MOVE:
				executeActionsMove(actions[i]);
				break;
			case ActionTypes.ACTION_UPGRADE_CAPITAL:
				executeActionsUpgradeCapital(actions[i]);
				break;
			}
		}
		initActionArray();
	}
	
	private void executeActionsUpgradeCapital(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionUpgradeCapital action = (ActionUpgradeCapital)a;
			Power powerConcerned = action.getPowerConcerned();
			Capital capital = powerConcerned.getCapital();
			capital.upgrade();
		}
	}

	private void executeActionsMove(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionMove action = (ActionMove)a;
			Power powerConcerned = action.getPowerConcerned();
			Box fromBox = map.getBox(action.getFrom());
			Box targetBox = map.getBox(action.getTarget());
			UnitManager.getInstance().moveUnits(powerConcerned, fromBox, targetBox);
		}
	}

	private void executeActionsMakeAlliance(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionMakeAlliance action = (ActionMakeAlliance)a;
			Power powerConcerned = action.getPowerConcerned();
			Power powerAllied = action.getPotentialAllied();
			UnitManager.getInstance().makeAlliance(powerConcerned, powerAllied);
		}
	}

	private void executeActionsDestroyUnits(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionDestroyUnits action = (ActionDestroyUnits)a;
			Power powerConcerned = action.getPowerConcerned();
			Position targetPosition = action.getTarget();
			Box targetBox = map.getBox(targetPosition);
			UnitManager.getInstance().deleteUnits(powerConcerned, targetBox);
		}
	}

	private void executeActionsDestroyBuilding(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionDestroyBuilding action = (ActionDestroyBuilding)a;
			Power powerConcerned = action.getPowerConcerned();
			Position targetPosition = action.getTarget();
			Box targetBox = map.getBox(targetPosition);
			BuildingManager.getInstance().destroyBuilding(powerConcerned, (GroundBox)targetBox);
		}
	}

	private void executeActionsCreateUnits(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionCreateUnit action = (ActionCreateUnit)a;
			Power powerConcerned = action.getPowerConcerned();
			Position targetPosition = action.getTarget();
			Box targetBox = map.getBox(targetPosition);
			int unitsType = action.getUnitType();
			int numberUnits = action.getNumberUnits();
			UnitManager.getInstance().addUnits(powerConcerned, targetBox, unitsType, numberUnits);
		}
	}

	private void executeActionsConstruct(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionConstruct action = (ActionConstruct)a;
			Power powerConcerned = action.getPowerConcerned();
			Box targetBox = map.getBox(action.getTarget());
			int buildingType = action.getBuildingType();

			BuildingManager.getInstance().addNewBuilding(powerConcerned, buildingType, (GroundBox)targetBox);
		}
	}

	private void executeActionsBreakAlliance(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionBreakAlliance action = (ActionBreakAlliance)a;
			Power powerConcerned = action.getPowerConcerned();
			UnitManager.getInstance().breakAlliance(powerConcerned);
		}
	}

	private void executeActionsAttack(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionAttack action = (ActionAttack)a;
			Power powerConcerned = action.getPowerConcerned();
			Box fromBox = map.getBox(action.getFrom());
			Box targetBox = map.getBox(action.getTarget());
			UnitManager.getInstance().attack(powerConcerned, fromBox, targetBox);
		}
	}
	
	private void initActionArray() {
		for (int i = 0; i < ActionTypes.NUMBER_ACTIONS; i++) {
			actions[i]= new ArrayList<Action>();
		}

	}
	public Boolean canContinueTurn(Resource actionPoints) {
		return actionPoints.getAmount() > 0;
	}
	public  void createPower(int number) {
		this.powers =new Power[number];
		for(int i = 0; i < number; i++){
			this.powers[i] = new Power("joueur " + (i+1));
		}
	}

}

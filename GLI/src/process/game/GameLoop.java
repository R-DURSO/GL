package process.game;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.actions.*;
import data.boxes.*;
import data.GameMap;
import data.Position;
import data.Power;
import data.resource.*;
import log.LoggerUtility;
import process.management.BuildingManager;
import process.management.PowerManager;
import process.management.UnitManager;

/**
 * <p>Implementation of the Game.</p>
 * <p>Created by a {@link process.game.Start Start} class</p>
 * <ul>Containts:
 * 		<li>the Array of {@link data.actions.Action Actions} used in {@link process.management.ActionValidator ActionValidator}</li>
 * 		<li>the {@link data.GameMap Map} used in the Game</li>
 * 		<li>Several method to be called between each Turn</li>
 * </ul>
 */
public class GameLoop {
	private static Logger Logger = LoggerUtility.getLogger(GameLoop.class, "text");
	@SuppressWarnings("unchecked")
	private ArrayList<Action> actions[] = (ArrayList<Action>[]) new ArrayList[ActionTypes.NUMBER_ACTIONS];
	
	// constante temporaire 
	private boolean isPlaying = true;
	private Power powers[];
	private GameMap map;
	
	public GameLoop(GameMap map, Power powers[] ) {
		initActionArray();
		this.map = map;
		this.powers = powers;
	}
	
	/**
	 * @return the number of player still in Game 
	 */
	public int getPlayerNumber() {
		return powers.length;
	}
	
	/**
	 * @return the List of {@link data.Power Power}
	 */
	public Power[] getPowers() {
		return powers;
	}
	
	/**
	 * Add an {@link data.actions.Action Action} to be done this {@link #endTurn() Turn}
	 * @param actionType
	 * @param action
	 */
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
				
				//they will do all actions they can 
				while(canContinueTurn(powers[i].getResource(ResourceTypes.RESOURCE_ACTIONS))) {
					// test action possible 


				}
			}
		}
	}
	
	/**
	 * End the current turn and start different actions
	 * <ul>
	 * 		<li>Decrease of BuildTime</li>
	 * 		<li>Application of all {@link data.actions.Action Actions}</li>
	 * 		<li>Checking the status of all {@link data.Power Power}</li>
	 * 		<li>Increase in all {@link data.resource.Resource Resources} from {@link data.building.product.BuildingProduct Production}</li>
	 * 		<li>Refresh the stats of all Powers</li>
	 * </ul>
	 */
	public void endTurn() {
		//decrease build time
		decreaseBuildTime();
		//Apply all stored action
		doActions();
		//check if Power are dead
		checkPower();
		//check Victory conditions
		//add resource from building product
		applyProduction();
		//if no more food, kill some unit
		
		//refresh power stats (number units, buildings & territorySize)
		refreshPowersStats();
		Logger.info("=== END OF TURN ===");
	}
	
	/**
	 * Launch the application of {@link data.actions.Action Action} from the {@link process.management.ActionValidator ActionValidator}
	 */
	public void doActions() {
		Logger.info("Starting the application of Action");
		for(int i = 0; i < ActionTypes.NUMBER_ACTIONS; i++) {
			switch(i) {
			case ActionTypes.ACTION_MAKE_ALLIANCE:
				executeActionsMakeAlliance(actions[i]);
				break;
			case ActionTypes.ACTION_BREAK_ALLIANCE:
				executeActionsBreakAlliance(actions[i]);
				break;
			case ActionTypes.ACTION_ATTACK:
				executeActionsAttack(actions[i]);
				break;
			case ActionTypes.ACTION_MOVE:
				executeActionsMove(actions[i]);
				break;
			case ActionTypes.ACTION_CONSTRUCT:
				executeActionsConstruct(actions[i]);
				break;
			case ActionTypes.ACTION_UPGRADE_CAPITAL:
				executeActionsUpgradeCapital(actions[i]);
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
			}
		}
		initActionArray();
	}
	
	private void executeActionsMakeAlliance(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionMakeAlliance action = (ActionMakeAlliance)a;
			Power powerConcerned = action.getPowerConcerned();
			Power powerAllied = action.getPotentialAllied();
			PowerManager.getInstance().makeAlliance(powerConcerned, powerAllied);
		}
	}
	
	private void executeActionsBreakAlliance(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionBreakAlliance action = (ActionBreakAlliance)a;
			Power powerConcerned = action.getPowerConcerned();
			PowerManager.getInstance().breakAlliance(powerConcerned);
		}
	}
	
	private void executeActionsUpgradeCapital(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionUpgradeCapital action = (ActionUpgradeCapital)a;
			Power powerConcerned = action.getPowerConcerned();
			BuildingManager.getInstance().upgradeCapital(powerConcerned);
		}
	}

	private void executeActionsMove(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionMove action = (ActionMove)a;
			Power powerConcerned = action.getPowerConcerned();
			Box[] path = action.getPath();
			UnitManager.getInstance().moveUnits(powerConcerned, path);
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
			Position targetPosition = action.getTarget();
			Box targetBox = map.getBox(targetPosition);
			BuildingManager.getInstance().destroyBuilding((GroundBox)targetBox);
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
	
	private void executeActionsAttack(ArrayList<Action> arrayList) {
		for(Action a : arrayList) {
			ActionAttack action = (ActionAttack)a;
			Power powerConcerned = action.getPowerConcerned();
			Box fromBox = map.getBox(action.getFrom());
			Box targetBox = map.getBox(action.getTarget());
			UnitManager.getInstance().attack(powerConcerned, fromBox, targetBox);
		}
	}
	
	/**
	 * Initialisation of the ArrayList of {@link data.actions.Action Actions} retieve from {@link process.management.ActionValidator ActionValidator}
	 */
	private void initActionArray() {
		for (int i = 0; i < ActionTypes.NUMBER_ACTIONS; i++) {
			actions[i]= new ArrayList<Action>();
		}
	}
	
	/**
	 * call the {@link data.Power#applyProductionOfTurn applyProduction} method from {@link data.Power Power}
	 */
	private void applyProduction() {
		for (int i = 0; i < getPlayerNumber(); i++) {
			powers[i].applyProductionOfTurn();
		}
	}
	
	/**
	 * Check all {@link data.building.Building Buildings} on the map
	 * <br>and reduce their construction time.
	 */
	private void decreaseBuildTime() {
		for (int i=0; i < map.getSize(); i++) {
			for (int j=0; j < map.getSize(); j++) {
				Box visitBox = map.getBox(i, j);
				if (visitBox instanceof GroundBox) {
					GroundBox visitGBox = (GroundBox)visitBox;
					if (visitGBox.hasBuilding()) {
						if (!visitGBox.getBuilding().isFinish()) {
							BuildingManager.getInstance().decreaseBuildTime(visitGBox.getOwner(), visitGBox.getBuilding());
						}
					}
				}
			}
		}
	}
	
	/**
	 * @param {@link data.resource.ActionPoints actionPoints}
	 * @return true if > 0
	 */
	public Boolean canContinueTurn(Resource actionPoints) {
		return actionPoints.getAmount() > 0;
	}
	
	public void checkPower() {
		for (int i = 0; i < getPlayerNumber(); i++) {
			if (powers[i].hasLost()) {
				this.powers = PowerManager.getInstance().recreatePowerList(powers);
				if (powers.length <= 1) {
					Logger.info("=== "+powers[0].getName()+" is the last Power alive ===");
					this.isPlaying = false;
				}
			}
			else if (powers[i].getResource(ResourceTypes.RESOURCE_FOOD).getAmount() < 0) {
				PowerManager.getInstance().regainFood(powers[i]);
			}
		}
	}
	
	/**
	 * Call the method {@link process.management.PowerManager#refreshPowerStats() refreshPowerStats} for all {@link data.Power Power}
	 */
	private void refreshPowersStats() {
		for(int i = 0; i < powers.length; i++){
			PowerManager.getInstance().refreshPowerStats(powers[i], map);
		}
	}
}

package process.management;

import java.util.ArrayList;
import java.util.Random;

import data.GameMap;
import data.Position;
import data.Power;
import data.actions.Action;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.building.Building;
import data.resource.Resource;
import data.resource.ResourceTypes;
import data.unit.Units;


/**
 * Manages and decide behavior of powe who are non-human, depending of their difficulty
 * @author Aldric Vitali Silvestre
 * @see Power
 */
public class AIManager {
	private GameMap map;
	private ActionValidator actionValidator;
	private Random random = new Random();

	public AIManager(GameMap map) {
		this.map = map;
		actionValidator = new ActionValidator(map);
	}
	
	public void doActions(Power power) {
		//get all units
		ArrayList<Units> unitsList = getUnits(power);
		
		//get all buildings
		ArrayList<Building> buildingList = getBuildings(power);
		
		//get all territory
		ArrayList<Box> territory = power.getTerritory();
		
		//actions that power will do will be there
		Resource actionResource = power.getResource(ResourceTypes.RESOURCE_ACTIONS);
		int numberActions = actionResource.getAmount();
		
		//we will try a lot more actions than really do them
		int numberActionsToTry = numberActions * 5;
		
		Action triedActions[] = tryActions(numberActionsToTry);
		
		//finally, get actions that power will really do
		Action turnActions[] = getDesiredActions(numberActions, power.getAILevel());
	}
	
	/**
	 * It will try to do actions, and keep those that are valid, until he has had enough.
	 * @param numberActionsToTry the number of actions to return in the {@code Action} array
	 * @return actions that passed the {@code ActionValidator} test
	 */
	private Action[] tryActions(int numberActionsToTry) {
		Action actionTried[] = new Action[numberActionsToTry];
		
		
		
		return null;
	}
	
	/**
	 * actions passed will be rated, and only part of them will be returned, depending on power ai level.
	 * @param numberActions the number of actions returned
	 * @param aiLevel the level intelligence of power
	 * @return an array of actions
	 */
	private Action[] getDesiredActions(int numberActions, int aiLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Recover all {@linkplain data.unit.Units} that Power have
	 * @param power
	 * @return the Units list
	 */
	private ArrayList<Units> getUnits(Power power){
		ArrayList<Units> unitsList = new ArrayList<>();
		
		int mapSize = map.getSize();
		//check through map to recover all units
		for(int i = 0; i < mapSize; i++){
			for(int j = 0; j < mapSize; j++){
				Box box = map.getBox(i, j);
				if(box.hasUnit()) {
					Units units = box.getUnit();
					if(units.getOwner() == power)
						unitsList.add(units);
				}
			}
		}
		
		return unitsList;
	}
	
	
	/**
	 * Recover all {@linkplain data.building.Building} that Power have
	 * @param power
	 * @return the Building list
	 */
	private ArrayList<Building> getBuildings(Power power){
		ArrayList<Building> buildingList = new ArrayList<>();
		
		int mapSize = map.getSize();
		//check through map to recover all units
		for(int i = 0; i < mapSize; i++){
			for(int j = 0; j < mapSize; j++){
				Box box = map.getBox(i, j);
				//we must check if box is a ground box (no building can be on water box)
				if(box instanceof GroundBox) {
					GroundBox groundBox = (GroundBox)box;
					if(groundBox.hasBuilding() && groundBox.getOwner() == power) { //if has building, he have owner
						Building building = groundBox.getBuilding();
						buildingList.add(building);
					}
				}
			}
		}
		
		return buildingList;
	}

}

package test.old_tests;

import data.GameMap;
import data.Position;
import data.Power;
import data.actions.ActionMakeAlliance;
import data.boxes.GroundBox;
import data.building.BuildingTypes;
import data.building.army.Barrack;
import data.resource.ResourceTypes;
import data.unit.Archer;
import data.unit.UnitTypes;
import process.management.ActionValidator;
import process.management.MapBuilder;

public class ActionManagerTest {

	public static void main(String[] args) {
		//init variables
		Power powers[] = createPowers();
		GameMap map = null;
		int mapSize = 20;
		int waterAmount = 20;
		//createMapBuilder
		MapBuilder mapBuilder = new MapBuilder(mapSize, waterAmount, powers);
		//mapBuilder.displayMap();
		//init Map
		map = mapBuilder.buildMap();
		System.out.println(map);
		
		/*someTests on ActionManager*/
		
		ActionValidator actionManager = new ActionValidator(map);
		
		//make alliance
		try {
			ActionMakeAlliance ama = actionManager.createActionMakeAlliance(powers[1], powers[2]);
			ama.getPowerConcerned().setAlly(ama.getPotentialAllied());
			ama.getPotentialAllied().setAlly(ama.getPowerConcerned());
			System.out.println("alliance created");
		}catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		//make alliance with other already allied
		try {
			actionManager.createActionMakeAlliance(powers[0], powers[2]);
			System.out.println("alliance created");
		}catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		//create a building (unknown territory)
		try {
			actionManager.createActionConstruct(powers[3], BuildingTypes.BUILDING_MINE, new Position(2,3));
			System.out.println("building created");
		}catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		//create a building (no enough money)
		try {
			System.out.println(powers[3].getResourceAmount(ResourceTypes.RESOURCE_WOOD));
			actionManager.createActionConstruct(powers[3], BuildingTypes.BUILDING_BARRACK, new Position(0,18));
			System.out.println("building created");
		}catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		//create a building (enough resources)
		try {
			powers[0].getResource(ResourceTypes.RESOURCE_WOOD).addValue(3000);
			actionManager.createActionConstruct(powers[0], BuildingTypes.BUILDING_BARRACK, new Position(1,1));
			((GroundBox)map.getBox(1, 1)).setBuilding(new Barrack());
			System.out.println("building created");
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		//create Units (enough resources)
		try {
			int numberUnits = 40;
			powers[0].getResource(ResourceTypes.RESOURCE_GOLD).addValue(3000);
			actionManager.createActionCreateUnit(powers[0], UnitTypes.UNIT_ARCHER, numberUnits, new Position(1,1));
			//to do in another class of course
			map.getBox(1, 1).setUnit(new Archer(numberUnits, powers[0]));
			System.out.println("units created");
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
		
		System.out.println(map);
		
		
		
	}

	private static Power[] createPowers() {
		Power powers[] = new Power[4];
		for(int i = 0; i < 4; i++){
			powers[i] = new Power("joueur " + (i+1));
		}
		return powers;
	}

}

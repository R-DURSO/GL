package test;
import process.management.*;
import data.resource.*;
import data.GameMap;
import data.Position;
import data.Power;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.building.BuildingTypes;
import process.game.Start;

public class TestTurn {
private GameMap map;
private Power power[];
private ActionValidator action;


public TestTurn() {
	init();
	turn();
	endturn();
}
	
	
	
	
	
	
	
	public static void main(String[] args) {
	 new TestTurn() ;	
	}
	private  void init() {	

		Power powers[] = createPowers();
		this.power= powers;
		GameMap map = null;
		
		//createMapBuilder
		MapBuilder mapBuilder = new MapBuilder(15, 4, powers);
		
		map = mapBuilder.buildMap();
		
		mapBuilder.displayMap();
		
		//System.out.println(map);
		
		this.map=map;
		this.action = new ActionValidator(map);
		

	}
	private void turn() {
		for (int i=0; i<3 ; i++) {
			if(i==1) {
				try {
				action.createActionConstruct(power[0], BuildingTypes.BUILDING_SAWMILL, new Position(1,0));
				BuildingManager.getInstance().addNewBuilding(power[0], BuildingTypes.BUILDING_SAWMILL, (GroundBox) map.getBox(1, 0));
				power[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, Z );
				}catch( IllegalArgumentException e) {
					e.getMessage();
				}
			}
		}
	}
	private void endturn() {
		System.out.println(power[0].getResourceAmount(ResourceTypes.RESOURCE_FOOD));
		System.out.println(map.getBox(1, 0));
		System.out.println(power[0].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD));
	}
	
	
	
	
	private static Power[] createPowers() {
		Power powers[] = new Power[2];
		for(int i = 0; i < 2; i++){
			powers[i] = new Power("joueur " + (i+1));
		}
		return powers;
	}
}

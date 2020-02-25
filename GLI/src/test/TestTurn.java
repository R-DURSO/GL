package test;
import process.management.*;
import data.resource.*;
import data.unit.UnitTypes;
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
	for (int i=0; i<3 ; i++) {
		System.out.println("tour  "+i);
		turn(i);
		endturn();
	}
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
	private void turn(int i) {
		
		
			if(i==1) {
				// deux action du joueur 1
				try {
				action.createActionConstruct(power[0], BuildingTypes.BUILDING_MINE, new Position(1,0));
				BuildingManager.getInstance().addNewBuilding(power[0], BuildingTypes.BUILDING_MINE, (GroundBox) map.getBox(1, 0));

				}catch( IllegalArgumentException e) {
					e.getMessage();
				}
				try {
					action.createActionConstruct(power[0], BuildingTypes.BUILDING_BARRACK,new Position(0,1));
					BuildingManager.getInstance().addNewBuilding(power[0], BuildingTypes.BUILDING_BARRACK,(GroundBox) map.getBox(0, 1));
				}catch( IllegalArgumentException e) {
					e.getMessage();
				}
				// action du joueur 2
				try {
					action.createActionMakeAlliance(power[1], power[0]);
				}catch( IllegalArgumentException e) {
					e.getMessage();
				}
			}
			if(i==2) {
				try {
					action.createActionCreateUnit(power[0], UnitTypes.UNIT_INFANTRY, 10, new Position (0,1));
					UnitManager.getInstance().addUnits(power[0], map.getBox(0, 1), UnitTypes.UNIT_PIKEMAN, 10);
				}catch( IllegalArgumentException e) {
					e.getMessage();
				}
			}
		
	}
	private void endturn() {
		for (int a=0 ; a<2; a++) {
			System.out.println("joueur"+a);
			System.out.println("ressource disponible  \n");
			System.out.println("food : "+power[a].getResourceAmount(ResourceTypes.RESOURCE_FOOD));
			System.out.println("gold : "+power[a].getResourceAmount(ResourceTypes.RESOURCE_GOLD));
			System.out.println("stone : "+power[a].getResourceAmount(ResourceTypes.RESOURCE_STONE));
			System.out.println("wood : "+power[a].getResourceAmount(ResourceTypes.RESOURCE_WOOD));
		//  System.out.println("action : "+power[a].getResourceAmount(ResourceTypes.RESOURCE_ACTIONS)+"\n");
			System.out.println("production par tour  \n");
			System.out.println("food "+power[a].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD));
			System.out.println("gold "+power[a].getResourceProductionPerTurn(ResourceTypes.RESOURCE_GOLD));
			System.out.println("stone "+power[a].getResourceProductionPerTurn(ResourceTypes.RESOURCE_STONE));
			System.out.println(" wood "+power[a].getResourceProductionPerTurn(ResourceTypes.RESOURCE_WOOD));
		//	System.out.println("action "+power[a].getResourceProductionPerTurn(ResourceTypes.RESOURCE_ACTIONS)+"\n");
			System.out.println(map.getBox(0, 1).toString());
			System.out.println(map.getBox(1, 0).toString());
			
		}
		System.out.println(power[1].getAlly());
	}
	
	
	
	
	private static Power[] createPowers() {
		Power powers[] = new Power[2];
		for(int i = 0; i < 2; i++){
			powers[i] = new Power("joueur " + (i+1));
		}
		return powers;
	}
}

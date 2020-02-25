package test;

import data.GameMap;
import data.Position;
import data.Power;
import process.game.Start;
import process.management.ActionValidator;
import process.management.MapBuilder;

public class TestTurn {
GameMap map;
Power power[];
ActionValidator action; 
public TestTurn() {
	init();
	power[1].addResourcesProductionPerTurn(3, 50);
	power[1].addResourcesProductionPerTurn(4, 100);
	for (int i=1 ; i<3; i++) {
		System.out.println("tour "+i);
		System.out.println(power[1].getResource(3).getAmount());
		System.out.println(power[1].getResource(4).getAmount());

		
		if(i==1) {
			try {
				action.createActionConstruct(power[0], 1 , new Position(0,0));
			}catch (IllegalArgumentException e){
				System.err.println(e.getMessage());
			}
		i++;
		}
	
		power[1].getResource(3).addValue(50);
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
	private static Power[] createPowers() {
		Power powers[] = new Power[2];
		for(int i = 0; i < 2; i++){
			powers[i] = new Power("joueur " + (i+1));
		}
		return powers;
	}
}

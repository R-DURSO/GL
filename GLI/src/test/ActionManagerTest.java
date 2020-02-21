package test;

import data.GameMap;
import data.Power;
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
		mapBuilder.displayMap();
		//init Map
		map = mapBuilder.buildMap();
		
		/*someTests on ActionManager*/
		
		
		
		
	}

	private static Power[] createPowers() {
		Power powers[] = new Power[4];
		for(int i = 0; i < 4; i++){
			powers[i] = new Power("joueur " + i);
		}
		return powers;
	}

}

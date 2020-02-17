package test;

import data.Map;
import data.Power;
import process.management.MapBuilder;
import process.management.PowerFactory;

public class MapBuilderTest {

	public static void main(String[] args) {
		Power powers[] = new Power[4];
		for(int i = 0; i < 4; i++) {
			powers[i] = PowerFactory.createPower("joueur " + (i + 1));
		}
		
		
		MapBuilder mb = new MapBuilder(20, 20, powers);
		Map map = mb.buildMap();
		
		System.out.println(map);
	}

}

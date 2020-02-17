package test;

import data.Map;
import process.management.MapBuilder;
import data.Power;

public class MapBuilderTest {

	public static void main(String[] args) {
		Power puissance[] = new Power[4];
		puissance[0] = new Power("Alfred");
		puissance[1] = new Power("Bernard");
		puissance[2] = new Power("Charlie");
		puissance[3] = new Power("Damien");
		MapBuilder mapBuilder = new MapBuilder(20, 20, puissance);
		mapBuilder.displayMap();
		
		Map map = mapBuilder.buildMap();
		
		System.out.println(map);
	}

}

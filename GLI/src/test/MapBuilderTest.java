package test;

import data.Map;
import process.management.MapBuilder;

public class MapBuilderTest {

	public static void main(String[] args) {
		MapBuilder mapBuilder = new MapBuilder(20, 20);
		mapBuilder.displayMap();
		
		Map map = mapBuilder.buildMap();
		
		//System.out.println(map);
	}

}

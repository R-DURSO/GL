package test.Unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import data.GameMap;
import data.Power;
import data.boxes.GroundBox;
import data.building.special.Capital;
import data.resource.ResourceTypes;
import process.game.Start;
import process.management.MapBuilder;

/**
 * Unit test of map and mapBuilder.
 * We test the worse scenario concerning bugs: {@link data.GameMap} has 10 boxes per side,
 * there is a lot of {@link data.boxes.WaterBox} and 4 {@link data.Power} are playing on this map
 * @author Aldric Vitali Silvestre
 *
 */
public class TestInitGame {
	private Start start;
	private GameMap map;
	private Power powers[];
	private int mapSize;
	
	@Before
	public void prepareMap() {
		int aiLevels[] = {-1, 1, 2, 3};
		String names[] = {"albert", "bernard", "charlie", "damien"};
		start = new Start(4, names, 10, 60, true, aiLevels);
		map = start.getMap();
		mapSize = map.getSize();
		powers = start.getPowers();
	}
	
	@Test
	public void testMainGroundBoxes() {
		//check if major boxes (on corners and at center)
		assertTrue(map.getBox(0, 0) instanceof GroundBox);
		assertTrue(map.getBox(mapSize - 1, 0) instanceof GroundBox);
		assertTrue(map.getBox(0, mapSize - 1) instanceof GroundBox);
		assertTrue(map.getBox(mapSize - 1, mapSize - 1) instanceof GroundBox);
		assertTrue(map.getBox(mapSize/2, mapSize/2) instanceof GroundBox);
	}
	
	@Test
	public void testBelonging() {
		assertEquals(map.getBox(0,0).getOwner(), powers[0]);
		assertEquals(map.getBox(mapSize - 1,0).getOwner(), powers[2]);
		assertEquals(map.getBox(0,mapSize - 1).getOwner(), powers[3]);
		assertEquals(map.getBox(mapSize - 1, mapSize - 1).getOwner(), powers[1]);
	}
	
	@Test
	public void testHaveAllCapitals() {
		GroundBox gb1 = (GroundBox) map.getBox(0, 0);
		GroundBox gb2 = (GroundBox) map.getBox(mapSize - 1, mapSize - 1);
		GroundBox gb3 = (GroundBox) map.getBox(0, mapSize - 1);
		GroundBox gb4 = (GroundBox) map.getBox(mapSize - 1, 0);
		assertTrue(gb1.getBuilding() instanceof Capital);
		assertTrue(gb2.getBuilding() instanceof Capital);
		assertTrue(gb3.getBuilding() instanceof Capital);
		assertTrue(gb4.getBuilding() instanceof Capital);
	}
	
	@Test 
	public void testArtifactPosition() {
		GroundBox groundBox = (GroundBox) map.getBox(mapSize/2, mapSize/2);
		assertEquals(ResourceTypes.RESOURCE_ARTIFACT, groundBox.getResourceType());
	}
		
}
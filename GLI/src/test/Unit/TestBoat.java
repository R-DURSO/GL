package test.Unit;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import data.GameMap;
import data.Position;
import data.Power;
import data.unit.*;
import data.boxes.*;
import process.management.MapBuilder;
import process.management.UnitManager;

/**
 * <p>JUnit of the {@link data.unit.Boat Boat}.</p>
 * @author Maxence
 */
public class TestBoat {
	private static Power powers[] = new Power[2];
	private static GameMap map;
	private static Position wPos1;
	private static Position wPos2;
	private static Position wPos3;
	private static Power power;
	
	@BeforeClass
	public static void prepareMap() {
		powers[0] = new Power("j1");
		powers[1] = new Power("basePlayer");
		MapBuilder mapBuilder = new MapBuilder(12, 200, powers);
		map = mapBuilder.buildMap();
		//Search for instance of Water
		for (int i = 1; i < map.getSize()-1; i++) {
			for (int j = 1; j < map.getSize()-1; j++) {
				if (map.getBox(i, j) instanceof WaterBox) {
					if (wPos1 != null) {
						if (wPos2 != null) {
							if (wPos3 != null) {
								//end
							}
							else {
								wPos3 = new Position(i,j);
							}
						}
						else {
							wPos2 = new Position(i,j);
						}
					}
					else {
						wPos1 = new Position(i,j);
					}
				}
			}
		}
//		mapBuilder.displayMap();
	}
	
	@Test
	public void empileBoat() {
		power = powers[0];
		
		UnitManager.getInstance().addUnits(power, map.getBox(wPos1), UnitTypes.UNIT_BOAT, 1);
		Boat boat1 = (Boat) map.getBox(wPos1).getUnit();
		
		UnitManager.getInstance().addUnits(power, map.getBox(wPos2), UnitTypes.UNIT_BOAT, 1);
		Boat boat2 = (Boat) map.getBox(wPos2).getUnit();
		
		UnitManager.getInstance().addUnits(power, map.getBox(wPos3), UnitTypes.UNIT_BOAT, 1);
		Boat boat3 = (Boat) map.getBox(wPos3).getUnit();

		Box[] Path1 = {map.getBox(wPos1),
					map.getBox(wPos2)};

		Box[] Path2 = {map.getBox(wPos2),
					map.getBox(wPos3)};

		Box[] Path3 = {map.getBox(wPos3),
					map.getBox(new Position(map.getSize()/2, map.getSize()/2))};
		
		UnitManager.getInstance().moveUnits(power, Path1);
		UnitManager.getInstance().moveUnits(power, Path2);
		UnitManager.getInstance().moveUnits(power, Path3);
		
//		System.out.println(boat3);
		
		assertFalse(boat1.hasContainedUnits());
		assertFalse(boat2.hasContainedUnits());
		assertFalse(boat3.hasContainedUnits());
	}
	
	@Test
	public void moveBoat() {
		power = powers[0];
		
		UnitManager.getInstance().addUnits(power, map.getBox(wPos1), UnitTypes.UNIT_BOAT, 1);
		Boat boat1 = (Boat) map.getBox(wPos1).getUnit();
		
		Box[] Path1 = {map.getBox(wPos1),
					map.getBox(wPos2)};

		Box[] Path2 = {map.getBox(wPos2),
					map.getBox(wPos3)};
		
		UnitManager.getInstance().moveUnits(power, Path1);
		UnitManager.getInstance().moveUnits(power, Path2);
		
		assertEquals(boat1.getTypes(), map.getBox(wPos3).getUnit().getTypes());
	}
	
	
}

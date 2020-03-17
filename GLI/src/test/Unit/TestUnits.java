package test.Unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.GameMap;
import data.InitialValue;
import data.Position;
import data.Power;
import data.boxes.*;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.product.Windmill;
import data.resource.ResourceTypes;
import data.unit.*;
import process.management.BuildingManager;
import process.management.MapBuilder;
import process.management.UnitManager;

/**
 * <p>JUnit of all {@link data.unit.Units Units}.</p>
 * <p>Testing the creation, movement and fight of different Units</p>
 * @author Maxence
 */

public class TestUnits {
	private static Power powers[] = new Power[3];
	private static GameMap map;
	private static Position from;
	private static Position target;
	private static Power power;
	
	@BeforeClass
	public static void prepareMap() {
		powers[0] = new Power("j1");
		powers[1] = new Power("j2");
		powers[2] = new Power("basePlayer");
		MapBuilder mapBuilder = new MapBuilder(4, 0, powers);
		map = mapBuilder.buildMap();
//		mapBuilder.displayMap();
	}
	
	@Before
	public void removeUnit() {
		for (int i = 0 ; i < map.getSize() ; i++) {
			for (int j = 0 ; j < map.getSize() ; j++) {
				target = new Position(i,j);
				if (map.getBox(target).hasOwner()) {
					UnitManager.getInstance().deleteUnits(map.getBox(target).getOwner(), map.getBox(target));
				}
			}
		}
		for (int p=0; p < powers.length; p++) {
			if (powers[p].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD) != 0) {
				fail("failed to reset production");
			}
		}
	}
	
	@Test(expected = AssertionError.class)
	public void createUnitTooMuch() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_ARCHER, 500);
		assertEquals(500, map.getBox(target).getUnit().getNumber());
	}
	
	@Test
	public void createNoneUnit() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_ARCHER, 0);
		assertEquals(false, map.getBox(target).hasUnit());
	}
	
	@Test
	public void createUnitUnknown() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), 8008135, 5);
		assertEquals(false, map.getBox(target).hasUnit());
	}
	
	@Test
	public void createUnitFoodCost() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_INFANTRY, 5);
		assertEquals(powers[2].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD) - (Infantry.COST_PER_TURN * 5), power.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD));
	}
	
	@Test
	public void createUnitMovement() {
		power = powers[0];
		from = new Position(0,1);
		target = new Position(0,2);
		
		UnitManager.getInstance().addUnits(power, map.getBox(from), UnitTypes.UNIT_INFANTRY, 5);
		Units unit = map.getBox(from).getUnit();
		
		UnitManager.getInstance().moveUnits(power, map.getBox(from), map.getBox(target));
		assertEquals(null, map.getBox(from).getUnit());
		
		assertEquals(unit, map.getBox(target).getUnit());
	}
	
	@Test
	public void createUnitAttack() {
		
		from = new Position(0,1);
		map.getBox(from).setOwner(powers[0]);
		powers[0].addBox(map.getBox(from));
		
		target = new Position(2,2);
		map.getBox(target).setOwner(powers[1]);
		powers[1].addBox(map.getBox(target));
		
		int nbUnit = 20;
		UnitManager.getInstance().addUnits(powers[0], map.getBox(from), UnitTypes.UNIT_INFANTRY, nbUnit);
		UnitManager.getInstance().addUnits(powers[1], map.getBox(target), UnitTypes.UNIT_INFANTRY, nbUnit/2);
		UnitManager.getInstance().attackUnits(powers[0], map.getBox(from), map.getBox(target));
		if (map.getBox(from).hasUnit()) {
			assertNotEquals(nbUnit, map.getBox(from).getUnit().getNumber());
		}
		else {
			assertNotEquals(nbUnit, map.getBox(target).getUnit().getNumber());
		}
	}
	
	@Test
	public void createUnitAttackBuilding() {
		
		from = new Position(0,1);
		Box fromBox = map.getBox(from);
		fromBox.setOwner(powers[0]);
		powers[0].addBox(fromBox);
		
		target = new Position(2,2);
		Box targetBox = map.getBox(target);
		targetBox.setOwner(powers[1]);
		powers[1].addBox(targetBox);
		
		int nbUnit = 20;
		GroundBox targetGBox = (GroundBox) targetBox;
		
		UnitManager.getInstance().addUnits(powers[0], fromBox, UnitTypes.UNIT_INFANTRY, nbUnit);
		Units unit = fromBox.getUnit();
		BuildingManager.getInstance().addNewBuilding(powers[1], BuildingTypes.BUILDING_WINDMILL, targetGBox);
		
		UnitManager.getInstance().attackUnits(powers[0], fromBox, targetBox);
		
		if (fromBox.hasUnit()) {
//			System.out.println("le batiment tiens toujours");
			assertNotEquals(Windmill.BASE_HEALTH, ((GroundBox) targetBox).getBuilding().getHealth());
		}
		else {
//			System.out.println("le batiment s'est effondre");
			assertEquals(unit, targetBox.getUnit());
			assertEquals(false, targetGBox.hasBuilding());
		}
	}
}

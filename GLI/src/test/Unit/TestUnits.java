package test.Unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.GameMap;
import data.InitialValue;
import data.Position;
import data.Power;
import data.boxes.*;
import data.building.BuildingTypes;
import data.resource.ResourceTypes;
import data.unit.*;
import process.management.BuildingManager;
import process.management.MapBuilder;
import process.management.PowerFactory;
import process.management.PowerManager;
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
		powers[0] = PowerFactory.createPower("" + (1), 0);
		powers[1] = PowerFactory.createPower("" + (2), 0);
		powers[2] = PowerFactory.createPower("" + (3), 0);
		MapBuilder mapBuilder = new MapBuilder(4, 0, false, powers);
		map = mapBuilder.buildMap();
//		mapBuilder.displayMap();
	}
	
	@Before
	public void removeUnit() {
		for (int i = 0; i < map.getSize(); i++) {
			for (int j = 0; j < map.getSize(); j++) {
				target = new Position(i, j);
				if (map.getBox(target).hasUnit()) {
					Box BoxUnitToDelete = map.getBox(target);
					UnitManager.getInstance().deleteUnits(BoxUnitToDelete.getUnit().getOwner(), BoxUnitToDelete);
				}
			}
		}
		for (int p = 0; p < powers.length; p++) {
			if (powers[p].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD) != InitialValue.FOOD_BASE_PRODUCTION) {
				fail("failed to reset production");
			}
		}
	}
	
	@Before
	public void makeAlliance() {
		if (!powers[0].isAllied()) {
			if (powers[1].isAllied()) {
				PowerManager.getInstance().breakAlliance(powers[1]);
			}
			PowerManager.getInstance().makeAlliance(powers[1], powers[2]);
		}
	}
	
	@Test(expected = AssertionError.class)
	public void createUnitTooMuch() {
		power = powers[0];
		target = new Position(0, 1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_ARCHER, 500);
		assertEquals(500, map.getBox(target).getUnit().getNumber());
	}
	
	@Test
	public void createNoneUnit() {
		power = powers[0];
		target = new Position(0, 1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_ARCHER, 0);
		assertEquals(false, map.getBox(target).hasUnit());
	}
	
	@Test
	public void createUnitUnknown() {
		power = powers[0];
		target = new Position(0, 1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), 8008135, 5);
		assertEquals(false, map.getBox(target).hasUnit());
	}
	
	@Test
	public void createUnitFoodCost() {
		power = powers[0];
		target = new Position(0, 1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_INFANTRY, 5);
		assertEquals(powers[2].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD) - (Infantry.COST_PER_TURN * 5),
				power.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD));
	}
	
	@Test
	public void createUnitMovement() {
		power = powers[0];
		from = new Position(0, 1);
		map.getBox(from).setOwner(power);
		target = new Position(0, 2);
		map.getBox(target).setOwner(null);
		
		UnitManager.getInstance().addUnits(power, map.getBox(from), UnitTypes.UNIT_INFANTRY, 5);
		Units unit = map.getBox(from).getUnit();
		
		Box[] Path = {map.getBox(from),
					map.getBox(target)};
		UnitManager.getInstance().moveUnits(power, Path);
		assertEquals(null, map.getBox(from).getUnit());
		
		assertEquals(unit, map.getBox(target).getUnit());
	}
	
	@Test
	public void createUnitMovementAllied() {
		power = powers[0];
		Power powerAllied = powers[1];
		PowerManager.getInstance().makeAlliance(power, powerAllied);

		from = new Position(0, 1);
		target = new Position(0, 2);

		map.getBox(from).setOwner(power);
		map.getBox(target).setOwner(powerAllied);
		
		UnitManager.getInstance().addUnits(power, map.getBox(from), UnitTypes.UNIT_CAVALRY, 5);
		Units unit = map.getBox(from).getUnit();

		Box[] Path = {map.getBox(from),
					map.getBox(target)};
		UnitManager.getInstance().moveUnits(power, Path);
		assertEquals(null, map.getBox(from).getUnit());
		assertEquals(unit, map.getBox(target).getUnit());
		assertEquals(powerAllied, map.getBox(target).getOwner());
	}
	
	@Test
	public void createUnitAttack() {

		from = new Position(0, 1);
		map.getBox(from).setOwner(powers[0]);
		powers[0].addBox(map.getBox(from));

		target = new Position(2, 2);
		map.getBox(target).setOwner(powers[1]);
		powers[1].addBox(map.getBox(target));
		
		int nbUnitAtt = 40;
		int nbUnitDef = 10;
		UnitManager.getInstance().addUnits(powers[0], map.getBox(from), UnitTypes.UNIT_INFANTRY, nbUnitAtt);
		UnitManager.getInstance().addUnits(powers[1], map.getBox(target), UnitTypes.UNIT_INFANTRY, nbUnitDef);
		UnitManager.getInstance().attack(powers[0], map.getBox(from), map.getBox(target));
		if (map.getBox(from).hasUnit()) {
			assertNotEquals(nbUnitAtt, map.getBox(from).getUnit().getNumber());
			assertNotEquals(nbUnitDef, map.getBox(target).getUnit().getNumber());
		}
		else {
			assertNotEquals(nbUnitAtt, map.getBox(target).getUnit().getNumber());
		}
	}
	
	@Test
	public void createUnitAttackRanged() {

		from = new Position(0, 1);
		map.getBox(from).setOwner(powers[0]);
		powers[0].addBox(map.getBox(from));

		target = new Position(2, 2);
		map.getBox(target).setOwner(powers[1]);
		powers[1].addBox(map.getBox(target));
		
		int nbUnitAtt = 20;
		int nbUnitDef = 5;
		UnitManager.getInstance().addUnits(powers[0], map.getBox(from), UnitTypes.UNIT_ARCHER, nbUnitAtt);
		UnitManager.getInstance().addUnits(powers[1], map.getBox(target), UnitTypes.UNIT_INFANTRY, nbUnitDef);
		if (powers[0].isAllied()) {
			PowerManager.getInstance().breakAlliance(powers[0]);
		}
		UnitManager.getInstance().attack(powers[0], map.getBox(from), map.getBox(target));
		if (map.getBox(target).hasUnit()) {
			assertNotEquals(nbUnitDef, map.getBox(target).getUnit().getNumber());
		}
		else {
			assertEquals(nbUnitAtt, map.getBox(from).getUnit().getNumber());
		}
	}
	
	@Test
	public void createUnitAttackBuilding() {

		from = new Position(0, 1);
		Box fromBox = map.getBox(from);
		fromBox.setOwner(powers[0]);
		powers[0].addBox(fromBox);

		target = new Position(1, 1);
		Box targetBox = map.getBox(target);
		targetBox.setOwner(powers[1]);
		powers[1].addBox(targetBox);
		
		int nbUnit = 34;
		GroundBox targetGBox = (GroundBox) targetBox;
		
		UnitManager.getInstance().addUnits(powers[0], fromBox, UnitTypes.UNIT_INFANTRY, nbUnit);
		Units unit = fromBox.getUnit();
		BuildingManager.getInstance().addNewBuilding(powers[1], BuildingTypes.BUILDING_WALL, targetGBox);
		int baseHP = targetGBox.getBuilding().getHealth();
		
		UnitManager.getInstance().attack(powers[0], fromBox, targetBox);
		
		if (fromBox.hasUnit()) {
//			System.out.println("le batiment tiens toujours");
			assertNotEquals(baseHP, ((GroundBox) targetBox).getBuilding().getHealth());
		}
		else {
//			System.out.println("le batiment s'est effondre");
			assertEquals(unit, targetBox.getUnit());
			assertEquals(false, targetGBox.hasBuilding());
		}
	}
	
	@Test
	public void createUnitAttackBuildingBySiegeUnit() {

		from = new Position(0, 1);
		Box fromBox = map.getBox(from);
		fromBox.setOwner(powers[0]);
		powers[0].addBox(fromBox);

		target = new Position(1, 1);
		Box targetBox = map.getBox(target);
		targetBox.setOwner(powers[1]);
		powers[1].addBox(targetBox);
		
		GroundBox targetGBox = (GroundBox) targetBox;
		
		UnitManager.getInstance().addUnits(powers[0], fromBox, UnitTypes.UNIT_BATTERING_RAM, 1);
		Units unit = fromBox.getUnit();
		BuildingManager.getInstance().addNewBuilding(powers[1], BuildingTypes.BUILDING_WALL, targetGBox);
		int baseHP = targetGBox.getBuilding().getHealth();
		
		UnitManager.getInstance().attack(powers[0], fromBox, targetBox);
		
		if (fromBox.hasUnit()) {
//			System.out.println("le batiment tiens toujours");
			assertNotEquals(baseHP, ((GroundBox) targetBox).getBuilding().getHealth());
		}
		else {
//			System.out.println("le batiment s'est effondre");
			assertEquals(unit, targetBox.getUnit());
			assertEquals(false, targetGBox.hasBuilding());
		}
	}
}

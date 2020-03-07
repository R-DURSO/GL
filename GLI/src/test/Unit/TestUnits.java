package test.Unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import data.GameMap;
import data.InitialValue;
import data.Position;
import data.Power;
import data.boxes.*;
import data.resource.ResourceTypes;
import data.unit.*;
import process.management.MapBuilder;
import process.management.UnitManager;

/**
 * <p>JUnit of all {@link data.unit.Units Units}.</p>
 * <p>Testing the creation, movement and fight of different Units</p>
 * @author Maxence
 */

public class TestUnits {
	private static Power powers[] = new Power[2];
	private static GameMap map;
	private static Position target;
	private static Power power;
	
	@BeforeClass
	public static void prepareMap() {
		powers[0] = new Power("j1");
		powers[1] = new Power("j2");
		MapBuilder mapBuilder = new MapBuilder(4, 0, powers);
		map = mapBuilder.buildMap();
	}
	
	@Test(expected = AssertionError.class)
	public void createUnitTooMuch() {
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(powers[0], map.getBox(target), UnitTypes.UNIT_ARCHER, 500);
		assertEquals(map.getBox(target).getUnit().getNumber(), 500);
	}
	
	@Test(expected = NullPointerException.class)
	public void createNoneUnit() {
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(powers[0], map.getBox(target), UnitTypes.UNIT_ARCHER, 0);
		assertEquals(map.getBox(target).getUnit().getNumber(), 0);
	}
	
	@Test
	public void createUnitUnknown() {
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(powers[0], map.getBox(target), 8008135, 5);
	}
	
	/*
	 * Actuellement, le cout en Or se fait dans l'ActionValidator...
	 * Faudrait surement déplacer ça
	@Test
	public void createUnitCost() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(powers[0], map.getBox(target), UnitTypes.UNIT_INFANTRY, 5);
		assertEquals(InitialValue.GOLD_INITAL_VALUE - (Infantry.COST * 5), power.getResourceAmount(ResourceTypes.RESOURCE_GOLD));
	}
	*/
	
	@Test
	public void createUnitFoodCost() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(powers[0], map.getBox(target), UnitTypes.UNIT_INFANTRY, 5);
	}
}

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
		MapBuilder mapBuilder = new MapBuilder(8, 0, powers);
		map = mapBuilder.buildMap();
	}
	
	@Test(expected = AssertionError.class)
	public void createUnitTooMuch() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_ARCHER, 500);
		assertEquals(map.getBox(target).getUnit().getNumber(), 500);
	}
	
	@Test(expected = NullPointerException.class)
	public void createNoneUnit() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_ARCHER, 0);
		assertEquals(map.getBox(target).getUnit().getNumber(), 0);
	}
	
	@Test
	public void createUnitUnknown() {
		power = powers[0];
		target = new Position(0,1);
		UnitManager.getInstance().addUnits(power, map.getBox(target), 8008135, 5);
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
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_INFANTRY, 5);
		assertEquals(powers[2].getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD), power.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD) - (Infantry.COST_PER_TURN * 5));
	}
	
	@Test
	public void createUnitMovement() {
		power = powers[0];
		from = new Position(0,1);
		target = new Position(0,2);
		UnitManager.getInstance().addUnits(power, map.getBox(from), UnitTypes.UNIT_INFANTRY, 5);
		UnitManager.getInstance().moveUnits(power, map.getBox(from), map.getBox(target));
		assertEquals(null, map.getBox(from).getUnit());
		assertEquals(new Infantry(5), map.getBox(target).getUnit());
	}
	
	@Test
	public void createUnitAttack() {
		power = powers[0];
		from = new Position(0,1);
		map.getBox(from).setOwner(power);
		target = new Position(1,1);
		map.getBox(target).setOwner(powers[1]);
		UnitManager.getInstance().addUnits(power, map.getBox(from), UnitTypes.UNIT_INFANTRY, 5);
		UnitManager.getInstance().addUnits(power, map.getBox(target), UnitTypes.UNIT_INFANTRY, 5);
		UnitManager.getInstance().attackUnits(power, map.getBox(from), map.getBox(target));
	}
}

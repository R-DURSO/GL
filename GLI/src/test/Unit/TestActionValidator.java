package test.Unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import data.GameMap;
import data.InitialValue;
import data.Position;
import data.Power;
import data.boxes.*;
import data.building.BuildingTypes;
import data.building.army.*;
import data.building.product.*;
import data.building.special.*;
import data.resource.*;
import data.unit.*;
import process.management.ActionValidator;
import process.management.MapBuilder;
import process.management.PowerFactory;
import process.management.UnitManager;

/**
 * Unit tests of all actions verified by ActionValidator. Map used is the one returned by 
 * {@link process.management.MapBuilder#buildSpecialMap()}.
 * @author Aldric Vitali Silvestre
 * @see process.management.ActionValidator
 */
public class TestActionValidator {
	private GameMap map;
	private Power powers[];
	private int mapSize;
	private ActionValidator actionValidator;
	
	@Before
	public void prepareSpecialMap() {
		generatePowers();
		generateSpecialMap();
		placeUnitsAndBuildings();
		actionValidator = new ActionValidator(map);
	}

	@Test
	public void testAttackUnitSuccess1() throws IllegalArgumentException{
		actionValidator.createActionAttack(powers[2], new Position(mapSize - 1, mapSize - 3), new Position(mapSize - 1, mapSize - 2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAttackUnitFailure() throws IllegalArgumentException{
		//too far to attack
		actionValidator.createActionAttack(powers[0], new Position(0, 0), new Position(0, mapSize - 1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAttackUnitFailure2() throws IllegalArgumentException{
		//target is ally's
		actionValidator.createActionAttack(powers[0], new Position(0, 0), new Position(mapSize - 1, mapSize - 2));
	}
	
	@Test
	public void testMakeAllianceSuccess() throws IllegalArgumentException{
		actionValidator.createActionMakeAlliance(powers[2], powers[3]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMakeAllianceFailure() throws IllegalArgumentException{
		//power[2] is already allied with powers[0]
		actionValidator.createActionMakeAlliance(powers[1], powers[2]);
	}
	
	@Test
	public void testBreakAllianceSuccess() throws IllegalArgumentException{
		actionValidator.createActionBreakAlliance(powers[0], powers[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBreakAllianceFailure() throws IllegalArgumentException{
		//no ally
		actionValidator.createActionBreakAlliance(powers[1], powers[3]);
	}

	@Test
	public void testMoveSuccess1() throws IllegalArgumentException{
		//deplacer le cavalier au milieu de la carte
		actionValidator.createActionMove(powers[1], new Position(mapSize -1, mapSize -2), new Position(mapSize/2, mapSize/2));
	}
	
	@Test
	public void testMoveSuccess2() throws IllegalArgumentException{
		//can move in his Door
		actionValidator.createActionMove(powers[2], new Position(mapSize -1, mapSize -3), new Position(mapSize -1, mapSize - 4));
	}
	
	@Test
	public void testMoveSuccess3() throws IllegalArgumentException{
		//Boat can move on water
		actionValidator.createActionMove(powers[0], new Position(mapSize/2, 0), new Position(mapSize/2, 1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailure1() throws IllegalArgumentException{
		//too far
		actionValidator.createActionMove(powers[0], new Position(0, 0), new Position(mapSize/2, mapSize/2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailure2() throws IllegalArgumentException{
		//obstacle
		actionValidator.createActionMove(powers[0], new Position(0, 0), new Position(0, 2));
	}
	
	@Test
	public void testConstructSucess() throws IllegalArgumentException{
		actionValidator.createActionConstruct(powers[0], BuildingTypes.BUILDING_MINE, new Position(1, 1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructFailure1() throws IllegalArgumentException{
		//not on power's boxes
		actionValidator.createActionConstruct(powers[0], BuildingTypes.BUILDING_MINE, new Position(mapSize/2, mapSize/2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructFailure2() throws IllegalArgumentException{
		//already another buiding
		actionValidator.createActionConstruct(powers[1], BuildingTypes.BUILDING_MINE, new Position(mapSize - 2, mapSize - 2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructFailure3() throws IllegalArgumentException{
		//not enough resources
		actionValidator.createActionConstruct(powers[1], BuildingTypes.BUILDING_MINE, new Position(mapSize - 2, mapSize - 1));
	}
	
	@Test
	public void testCreateUnitsSuccess() throws IllegalArgumentException{
		actionValidator.createActionCreateUnit(powers[1], UnitTypes.UNIT_INFANTRY, 20, new Position(mapSize - 2, mapSize - 2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateUnitsFailure() throws IllegalArgumentException{
		//not on barrack
		actionValidator.createActionCreateUnit(powers[1], UnitTypes.UNIT_INFANTRY, 20, new Position(mapSize - 2, mapSize - 1));
	}
	
	@Test
	public void testUpgradeCapitalSucess() throws IllegalArgumentException{
		//not enough resources
		actionValidator.createActionUpgradeCapital(powers[1]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpgradeCapitalFailure() throws IllegalArgumentException{
		//not enough resources
		actionValidator.createActionUpgradeCapital(powers[2]);
	}
	
	@Test
	public void testPathFindingFail() {
		assertFalse(actionValidator.pathFinding(new Position(0,0), map.getBox(new Position(0,0)).getUnit(), new Position(0,1)));
	}
	
	@Test
	public void testPathFindingSuccess() {
		assertTrue(actionValidator.pathFinding(new Position(0,0), map.getBox(new Position(0,0)).getUnit(), new Position(1,1)));
	}
	
	private void placeUnitsAndBuildings() {
		//we put a boat on water, on top side
		map.getBox(mapSize/2, 0).setOwner(powers[0]);
		map.getBox(mapSize/2, 0).setUnit(new Boat());
		
		//we add several units on different places
		map.getBox(0,0).setUnit(new Archer(20));
		map.getBox(0,mapSize - 1).setUnit(new Infantry(20));
		map.getBox(mapSize-1, 2).setOwner(powers[2]);
		map.getBox(mapSize - 1,2).setUnit(new Archer(20));
		map.getBox(mapSize - 1 ,mapSize - 2).setUnit(new Cavalry(20));
		
		//and buildings
		((GroundBox)map.getBox(mapSize/2 -1, 0)).setBuilding(new Barrack());
		((GroundBox)map.getBox(1, mapSize - 2)).setBuilding(new Sawmill());
		((GroundBox)map.getBox(mapSize - 2, mapSize - 2)).setBuilding(new Barrack());
		((GroundBox)map.getBox(1, 0)).setBuilding(new Dock());
		
		//obstacles
		((GroundBox)map.getBox(0,1)).setBuilding(new Wall());
		((GroundBox)map.getBox(mapSize - 1, 1)).setBuilding(new Door());
		
		//2 powers are allied here
		powers[0].setAlly(powers[1]);
		powers[1].setAlly(powers[0]);
	}

	private void generatePowers() {
		powers = new Power[4];
		for(int i = 0; i < 4; i++){
			powers[i] = PowerFactory.createPower("" + (i+1));
		}
		//remove power 2's wood resources (to check failure of building construct)
		powers[1].getResource(ResourceTypes.RESOURCE_WOOD).subValue(InitialValue.WOOD_INITAL_VALUE);
		//and power 3' gold resources (to check capital ugrade failure)
		powers[2].getResource(ResourceTypes.RESOURCE_GOLD).subValue(InitialValue.GOLD_INITAL_VALUE);
	}

	private void generateSpecialMap() {
		MapBuilder mapBuilder = new MapBuilder(5, 0, powers);
		map = mapBuilder.buildSpecialMap();
		mapSize = map.getSize();
	}
	
}

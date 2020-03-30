package test.Unit;
import org.junit.Before;
import org.junit.Test;

import data.GameMap;
import data.InitialValue;
import data.Power;
import data.boxes.*;
import data.building.BuildingTypes;
import data.resource.ResourceTypes;
import process.management.BuildingManager;
import process.management.MapBuilder;
import process.management.PowerFactory;
public class TestCreateBuilding {
	private GameMap map;
	private Power powers[];
	private int mapSize;
	@Before
	public void prepareMap() {
		generatePowers();
		generateSpecialMap();
	}
	@Test
	public void createBarrack(){
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_BARRACK , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createWorkshop() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_WORKSHOP , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createDock() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_DOCK , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createMine() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_MINE , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createSwamill() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_SAWMILL , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createWindmill() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_WINDMILL , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createQuarry() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_QUARRY , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createDoor() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_DOOR , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createWall() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_WALL , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createTemple() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_TEMPLE , (GroundBox) powers[0].getTerritory().get(1));	
	}
	@Test
	public void createCapital() {
		BuildingManager.getInstance().addNewBuilding(powers[0],BuildingTypes.BUILDING_CAPITAL , (GroundBox) powers[0].getTerritory().get(1));
	}
	@Test
	public void createWindmillOnFood() {
		BuildingManager.getInstance().addNewBuilding(powers[0], BuildingTypes.BUILDING_WINDMILL, (GroundBox) powers[0].getTerritory().get(1));
	}
	
	
	
	private void generatePowers() {
		powers = new Power[4];
		for(int i = 0; i < 4; i++){
			powers[i] = PowerFactory.createPower("" + (i+1));
		}
		//remove power 2's wood resources (to check failure of building construct)
		powers[1].getResource(ResourceTypes.RESOURCE_WOOD).subValue(InitialValue.WOOD_INITIAL_VALUE);
		//and power 3' gold resources (to check capital ugrade failure)
		powers[2].getResource(ResourceTypes.RESOURCE_GOLD).subValue(InitialValue.GOLD_INITIAL_VALUE);
	}

	private void generateSpecialMap() {
		MapBuilder mapBuilder = new MapBuilder(15, 0, powers);
		map = mapBuilder.buildSpecialMap();
		mapSize = map.getSize();
	}

}

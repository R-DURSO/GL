package test.old_tests;

import data.GameMap;
import data.Position;
import data.Power;
import data.actions.ActionCreateUnit;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.building.BuildingTypes;
import data.unit.UnitTypes;
import process.management.ActionValidator;
import process.management.BuildingManager;
import process.management.MapBuilder;
import process.management.PowerManager;
import process.management.UnitManager;

public class TestAttack2 {
	public static void main(String[] args) {
		Power powers[] = new Power[2];
		powers[0] = PowerManager.createPower(""+(1), 0);
		powers[1] = PowerManager.createPower(""+(2), 0);
		GameMap map;
		MapBuilder mapBuilder = new MapBuilder(4, 0, false, powers);
		map = mapBuilder.buildMap();
		
		ActionValidator validator = new ActionValidator(map);
		
		System.out.println(map);
		System.out.println("\n\n==============\n\n");
		
		try {
			validator.createActionConstruct(powers[0], BuildingTypes.BUILDING_BARRACK, new Position(1,1));
			BuildingManager.getInstance().addNewBuilding(powers[0], BuildingTypes.BUILDING_BARRACK, (GroundBox)map.getBox(1,1));
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		try {
			validator.createActionConstruct(powers[1], BuildingTypes.BUILDING_BARRACK, new Position(2,2));
			BuildingManager.getInstance().addNewBuilding(powers[1], BuildingTypes.BUILDING_BARRACK, (GroundBox)map.getBox(2,2));
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		
		try {
			validator.createActionCreateUnit(powers[1], UnitTypes.UNIT_INFANTRY, 10, new Position(2,2));
			UnitManager.getInstance().addUnits(powers[1], map.getBox(2, 2), UnitTypes.UNIT_INFANTRY, 10);
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		try {
			validator.createActionCreateUnit(powers[0], UnitTypes.UNIT_INFANTRY, 20, new Position(1,1));
			UnitManager.getInstance().addUnits(powers[0], map.getBox(1, 1), UnitTypes.UNIT_INFANTRY, 20);
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}

		
		try {
			validator.createActionMove(powers[0], new Position(1,1), new Position(2,1));
			Box[] Path = {map.getBox(1, 1),
							map.getBox(1, 2)};
			UnitManager.getInstance().moveUnits(powers[0], Path);
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		System.out.println(map);
		
		try {
			validator.createActionAttack(powers[0], new Position(1,2), new Position(2,2));
			UnitManager.getInstance().attack(powers[0], map.getBox(1,2), map.getBox(2, 2));
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		
		System.out.println(map);
		
	}
}

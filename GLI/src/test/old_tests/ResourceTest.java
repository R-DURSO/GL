package test.old_tests;

import data.Power;
import data.boxes.*;
import data.building.BuildingTypes;
import data.resource.*;
import data.unit.UnitTypes;
import process.management.BuildingManager;
import process.management.UnitManager;

public class ResourceTest {

	public static void main(String[] args) {
		Power pow1 = new Power("Albert");
		Power pow2 = new Power("Roger");
		
		pow1.getResource(ResourceTypes.RESOURCE_FOOD).addValue(500);
		pow1.getResource(ResourceTypes.RESOURCE_GOLD).addValue(500);
		System.out.println(pow1.toString());
		
		pow2.getResource(ResourceTypes.RESOURCE_FOOD).addValue(1000);
		pow2.getResource(ResourceTypes.RESOURCE_GOLD).addValue(1500);
		
		Box box1 = new GroundBox(ResourceTypes.RESOURCE_GOLD);
		Box box2 = new GroundBox(ResourceTypes.RESOURCE_FOOD);
		Box box3 = new GroundBox(ResourceTypes.NO_RESOURCE);
		
		box1.setOwner(pow1);
		//box2.setOwner(pow1);
		box3.setOwner(pow2);
		
		System.out.println("boite1 vide\n"+box1);
		BuildingManager.getInstance().addNewBuilding(pow1, BuildingTypes.BUILDING_MINE, (GroundBox)box1);
		System.out.println("boite1 avec batiment\n"+box1);
		
		UnitManager.getInstance().addUnits(pow1, box1, UnitTypes.UNIT_ARCHER, 20);
		System.out.println("boite1 avec unit\n"+box1);
		
		UnitManager.getInstance().addUnits(pow2, box3, UnitTypes.UNIT_INFANTRY, 100);
		System.out.println("boite3 avec unit\n"+box3);
		
		Box[] Path = {box1, box2};
		
		UnitManager.getInstance().moveUnits(pow1, Path);
		System.out.println("boite2 avec unit moved\n"+box2);

		UnitManager.getInstance().attack(pow2, box3, box2);
		System.out.println("boite2 avec combat\n"+box2);
		System.out.println("boite3 avec combat\n"+box3);
	}

}

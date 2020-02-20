package test;

import data.Power;
import data.boxes.*;
import data.building.BuildingTypes;
import data.resource.*;
import data.unit.UnitTypes;
import process.management.BuildingFactory;
import process.management.UnitFactory;

public class ResourceTest {

	public static void main(String[] args) {
		Power pow = new Power("Albert");
		pow.getResource(ResourceTypes.RESOURCE_FOOD).addValue(500);
		pow.getResource(ResourceTypes.RESOURCE_GOLD).addValue(500);
		System.out.println(pow.toString());
		Box box = new GroundBox(ResourceTypes.RESOURCE_GOLD);
		box.setOwner(pow);
		System.out.println(box);
		
		new BuildingFactory(pow, BuildingTypes.BUILDING_MINE, (GroundBox)box);
		System.out.println(box);
		
		System.out.println(box.getUnit());
		new UnitFactory(pow, box, UnitTypes.UNIT_ARCHER, 10);
		System.out.println(box);
	}

}

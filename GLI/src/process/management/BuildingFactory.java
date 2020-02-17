package process.management;
import data.boxes.GroundBox;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.*;

import data.building.product.*;
import data.building.special.*;
import data.Power;
public class BuildingFactory {

	public BuildingFactory(Power name, int type, GroundBox b) {
		boolean result;
		Building r ;
		result =constructableCase( b , name.getName());
		r=construction(result, type);
		b.setBuilding(r);
	}
	
	public  boolean  constructableCase(GroundBox b, String name) {
		if(b.getBuilding()!= null && b.getOwner().equals(name)) {
			return false;			
		}else {
			return true;
		}
	}
	public Building construction(boolean r, int cons) {
		if(r==true) {
			Building b = null;
			switch(cons) {
			case BuildingTypes.BUILDING_BARRACK:
				b = new Barrack();
			case BuildingTypes.BUILDING_STABLE:
				b = new Stable();
			case BuildingTypes.BUILDING_DOCK:
				b = new Dock();
			case BuildingTypes.BUILDING_MINE:
				b = new Mine();
			case BuildingTypes.BUILDING_SAWMILL:
				b = new Sawmill();
			case BuildingTypes.BUILDING_WINDMILL:
				b = new Windmill();
			case BuildingTypes.BUILDING_DOOR:
				b = new Door();
			case BuildingTypes.BUILDING_WALL:
				b = new Wall();
			case BuildingTypes.BUILDING_TEMPLE:
				b = new Temple();
			}
			return b;
		}
		return null;
	}
}

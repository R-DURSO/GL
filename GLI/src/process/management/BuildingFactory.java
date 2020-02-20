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
				break;
			case BuildingTypes.BUILDING_STABLE:
				b = new Stable();
				break;
			case BuildingTypes.BUILDING_DOCK:
				b = new Dock();
				break;
			case BuildingTypes.BUILDING_MINE:
				b = new Mine();
				break;
			case BuildingTypes.BUILDING_SAWMILL:
				b = new Sawmill();
				break;
			case BuildingTypes.BUILDING_WINDMILL:
				b = new Windmill();
				break;
			case BuildingTypes.BUILDING_DOOR:
				b = new Door();
				break;
			case BuildingTypes.BUILDING_WALL:
				b = new Wall();
				break;
			case BuildingTypes.BUILDING_TEMPLE:
				b = new Temple();
				break;
			}
			return b;
		}
		return null;
	}
}

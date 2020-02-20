package process.management;
import data.boxes.GroundBox;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.*;

import data.building.product.*;
import data.building.special.*;
import data.Power;

/**
 * Singleton which permits to create and add buildings on map
 *
 */
public class BuildingManager {

	private static BuildingManager instance = new BuildingManager();
	
	public BuildingManager() {
		
	}
	
	public static BuildingManager getInstance() {
		return instance;
	}
	
	public void addNewBuilding(Power power, int buildingType, GroundBox box) {
		Building building;
		building = construction(buildingType);
		box.setBuilding(building);
	}
	
	//vérifications déjà faites dans ActionManager
	public Building construction(int buildingType) {
			switch(buildingType) {
			case BuildingTypes.BUILDING_BARRACK:
				return new Barrack();
			case BuildingTypes.BUILDING_WORKSHOP:
				return new Workshop();
			case BuildingTypes.BUILDING_DOCK:
				return new Dock();
			case BuildingTypes.BUILDING_MINE:
				return new Mine();
			case BuildingTypes.BUILDING_SAWMILL:
				return new Sawmill();
			case BuildingTypes.BUILDING_WINDMILL:
				return new Windmill();
			case BuildingTypes.BUILDING_DOOR:
				return new Door();
			case BuildingTypes.BUILDING_WALL:
				return new Wall();
			case BuildingTypes.BUILDING_TEMPLE:
				return new Temple();
			default:
				return null;
			}
	}
}

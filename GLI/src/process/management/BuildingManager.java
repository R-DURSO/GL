package process.management;
import data.boxes.Box;
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
	
	/**
	 * Permits to work with the unique instance of BuildingManager.
	 * @return unique instance of BuildingManager
	 */
	public static BuildingManager getInstance() {
		return instance;
	}
	
	/**
	 * Add a building on a {@link GroundBox].
	 * @param power tht power who decided to build
	 * @param buildingType the type of building
	 * @param box where power wants to build
	 * @see BuildingTypes
	 */
	public void addNewBuilding(Power power, int buildingType, GroundBox box) {
		Building building;
		//create building depending on his type
		building = createBuildingWithType(buildingType);
		//add building to the box
		box.setBuilding(building);
		//now, we check if building is a production building and if he is on the right resource
		if (building instanceof BuildingProduct) {
			BuildingProduct buildingProduct = (BuildingProduct) building;
			if (buildingProduct.isOnRightResource(box.getResourceType())) {
				buildingProduct.setOnRightResource(true);
			}
			else {
				buildingProduct.setOnRightResource(false);
			}
		}
	}
	
	private Building createBuildingWithType(int buildingType) {
			switch(buildingType) {
			case BuildingTypes.BUILDING_BARRACK:
				return new Barrack();
			case BuildingTypes.BUILDING_WORKSHOP:
				return new Workshop();
			case BuildingTypes.BUILDING_DOCK:
				return new Dock();
			case BuildingTypes.BUILDING_MINE:
				return new Mine();
			case BuildingTypes.BUILDING_QUARRY:
				return new Quarry();
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
	
	
	/**
	 * Destroy a building and remove production per turn if the building is a {@link BuildingProduct}
	 * @param powerConcerned the power who want to destroy a building
	 * @param targetBox where the building to be destroy is
	 */
	public void destroyBuilding(Power powerConcerned, GroundBox targetBox) {
		Building building = targetBox.getBuilding();
		//if building is a production building, we can't just destroy it
		if(building instanceof BuildingProduct) {
			BuildingProduct buildingProduct = (BuildingProduct)building;
			//we check if building is enabled
			if(buildingProduct.getBuildTime() > 0) {
				//we get resource type and production
				int resourceType = buildingProduct.getProductionType();
				int resourceProdPerTurn = buildingProduct.getProductionPerTurn();
				powerConcerned.substractResourcesProductionPerTurn(resourceType, resourceProdPerTurn);
			}
		}
		//now we simply remove building from map
		targetBox.setBuilding(null);
	}
}

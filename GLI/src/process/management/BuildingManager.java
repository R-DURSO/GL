package process.management;

import data.boxes.*;

import data.building.Building;
import data.building.BuildingTypes;
import data.building.army.*;
import data.building.product.*;
import data.building.special.*;
import data.resource.ActionPoints;
import data.resource.Resource;
import data.resource.ResourceTypes;

import log.LoggerUtility;
import org.apache.log4j.Logger;

import data.Power;
import data.ScoreValue;

/**
 * Singleton which permits to create and 
 * <br>add {@link data.building.Building Building} on map
 */
public class BuildingManager {
	private static Logger Logger = LoggerUtility.getLogger(BuildingManager.class, "text");
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
		if (building == null) {
			Logger.error(power.getName()+" tried to create an invalid Building");
		}
		else {
			Logger.info(power.getName()+" create "+building.getClass().getSimpleName());
			//add building to the box
			box.setBuilding(building);
			//we add score from creating a Building
			if(building.getType() < BuildingTypes.BUILDING_ARMY) {
				power.addScore(ScoreValue.SCORE_VALUE_BUILDING_ARMY);
				Logger.info(power.getName()+" gain "+ScoreValue.SCORE_VALUE_BUILDING_ARMY+" score ");
			}else if(building.getType() < BuildingTypes.BUILDING_PRODUCT) {
				power.addScore(ScoreValue.SCORE_VALUE_BUILDING_PRODUCT);
				Logger.info(power.getName()+" gain "+ScoreValue.SCORE_VALUE_BUILDING_PRODUCT+" score ");
			}
			else if(building.getType() < BuildingTypes.BUILDING_SPECIAL) {
				if (building.getType() == BuildingTypes.BUILDING_TEMPLE) {
					power.addScore(ScoreValue.SCORE_VALUE_BUILDING_TEMPLE);
					Logger.info(power.getName()+" gain "+ScoreValue.SCORE_VALUE_BUILDING_TEMPLE+" score ");
				}
				else {
					power.addScore(ScoreValue.SCORE_VALUE_BUILDING_SPECIAL);
					Logger.info(power.getName()+" gain "+ScoreValue.SCORE_VALUE_BUILDING_SPECIAL+" score ");
				}
			}else {
				power.addScore(ScoreValue.SCORE_VALUE_DEFAULT);
				Logger.info(power.getName()+" gain "+ScoreValue.SCORE_VALUE_DEFAULT+" score ");
			}
			
			//now, we check if building is a production building and if he is on the right resource
			if (building instanceof BuildingProduct) {
				BuildingProduct buildingProduct = (BuildingProduct) building;
				if (buildingProduct.isOnRightResource(box.getResourceType())) {
					buildingProduct.setOnRightResource(true);
					// j'ai pas la ressource j'ai que le batiment
					Logger.info(power.getName()+" receive "+buildingProduct.getProductionPerTurn()+" "+Resource.getResourceType(buildingProduct.getProductionType())+" per turn");
				}
				else {
					buildingProduct.setOnRightResource(false);
					Logger.info(power.getName()+" doesn't receive "+Resource.getResourceType(buildingProduct.getProductionType())+" production, building isn't on right Resources");
				}
			}
		}
	}
	
	/**
	 * Return the associated {@link data.building.Building Building} from his {@link data.building.BuildingTypes Types}
	 * @param buildingType {@link data.building.BuildingTypes BuildingTypes}
	 * @return the associated {@link data.building.Building Building}
	 */
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
			case BuildingTypes.BUILDING_CAPITAL:
				return new Capital();
			default:
				return null;
			}
	}
	
	
	/**
	 * Destroy a building and remove production per turn if the building is a {@link BuildingProduct}
	 * @param powerConcerned the power who want to destroy a building
	 * @param targetBox where the building to be destroy is
	 */
	public void destroyBuilding(GroundBox targetBox) {
		Power powerConcerned = targetBox.getOwner();
		Building building = targetBox.getBuilding();
		//if building is a production building, we can't just destroy it
		if (building instanceof BuildingProduct) {
			BuildingProduct buildingProduct = (BuildingProduct)building;
			//we check if building is enabled
			if (buildingProduct.getBuildTime() > 0) {
				//we get resource type and production
				int resourceType = buildingProduct.getProductionType();
				int resourceProdPerTurn = buildingProduct.getProductionPerTurn();
				// faut que je r�cup�re la ressource 
				Logger.info(powerConcerned.getName()+" recover "+resourceProdPerTurn+" per turn");
				powerConcerned.subResourcesProductionPerTurn(resourceType, resourceProdPerTurn);
			}
		}
		// we remove score 
		if(building.getType() < BuildingTypes.BUILDING_ARMY) {
			powerConcerned.subScore(ScoreValue.SCORE_VALUE_BUILDING_ARMY);
			Logger.info(powerConcerned.getName()+" lose "+ScoreValue.SCORE_VALUE_BUILDING_ARMY+" score ");
		}
		else if(building.getType() < BuildingTypes.BUILDING_PRODUCT) {
			powerConcerned.subScore(ScoreValue.SCORE_VALUE_BUILDING_PRODUCT);
			Logger.info(powerConcerned.getName()+" lose "+ScoreValue.SCORE_VALUE_BUILDING_PRODUCT+" score ");
		}
		else if(building.getType() < BuildingTypes.BUILDING_SPECIAL) {
			if (building.getType() == BuildingTypes.BUILDING_TEMPLE) {
				powerConcerned.subScore(ScoreValue.SCORE_VALUE_BUILDING_TEMPLE);
				Logger.info(powerConcerned.getName()+" lose "+ScoreValue.SCORE_VALUE_BUILDING_TEMPLE+" score ");
			}
			else {
				powerConcerned.subScore(ScoreValue.SCORE_VALUE_BUILDING_SPECIAL);
				Logger.info(powerConcerned.getName()+" lose "+ScoreValue.SCORE_VALUE_BUILDING_SPECIAL+" score ");
			}
		}
		else {
			powerConcerned.subScore(ScoreValue.SCORE_VALUE_DEFAULT);
			Logger.info(powerConcerned.getName()+" lose "+ScoreValue.SCORE_VALUE_DEFAULT+" score ");
		}
		//now we simply remove building from map
		targetBox.setBuilding(null);
		Logger.info(powerConcerned.getName()+" has removed "+building.toString());
	}
	
	/**
	 * Method called to reduce a Building buildTime
	 * @param power {@link data.Power Power} that hold the {@link data.building.Building Building}
	 * @param building {@link data.building.Building Building} that reduce his buildTime
	 */
	public void decreaseBuildTime(Power power, Building building) {
		building.decreaseBuildTime();
		if (building.isFinished()) {
			if (building instanceof BuildingProduct) {
				BuildingProduct buildingP = (BuildingProduct)building;
				if (buildingP.getOnRightResource()) {
					power.addResourcesProductionPerTurn(buildingP.getProductionType(), buildingP.getProductionPerTurn());
					Logger.info(power.getName()+" gain "+buildingP.getProductionPerTurn()+Resource.getResourceType(buildingP.getProductionType())+" production each turn");
				}
			}
		}
	}
	
	/**
	 * {@link data.building.special.Capital#upgrade() Upgrade} the {@link data.building.special.Capital Capital} of a given {@link data.Power Power}
	 * @param power {@link data.Power Power} that upgrade his Capital
	 */
	public void upgradeCapital (Power power) {
		if (power.getCapital().upgrade()) {
			power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_ACTIONS, 1);
			((ActionPoints) power.getResource(ResourceTypes.RESOURCE_ACTIONS)).addMaxActions(2);
			Logger.info("Capital has been upgraded, +2 maxActionPoints, +1/turn ActionPoints");
			power.addScore(ScoreValue.SCORE_VALUE_UPGRADE_CAPITALE);
			Logger.info(power.getName()+" receive "+ScoreValue.SCORE_VALUE_UPGRADE_CAPITALE+" score");
		}
	}
	
}

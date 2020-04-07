package GUI.drawing;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import data.GameConstants;
import data.building.BuildingTypes;
import data.resource.ResourceTypes;
import data.unit.UnitTypes;
import log.LoggerUtility;
import process.management.UnitManager;

/**
 * Handles images loadings and storing during process.
 * @author Aldric Vitali Silvestre
 *
 */
public class ImagesUtility {
	/*For logs*/
	private static Logger logger = LoggerUtility.getLogger(ImagesUtility.class, GameConstants.LOG_TYPE);
	
	/*Units Paths & Images*/
	private final String INFANTRY_IMAGE_PATH = "GLI/src/images/Units/infantry.jpg";
	private final String ARCHER_IMAGE_PATH = "GLI/src/images/Units/Archer.jpg";
	private final String BATTERING_RAM_IMAGE_PATH = "GLI/src/images/Units/BatteringRam.jpg";
	private final String BOAT_IMAGE_PATH = "GLI/src/images/Units/Boat.jpg";
	private final String CAVALRY_IMAGE_PATH = "GLI/src/images/Units/Cavalry.jpg";
	private final String SPEARMAN_IMAGE_PATH = "GLI/src/images/Units/spear.jpg";
	private final String TREBUCHET_IMAGE_PATH = "GLI/src/images/Units/Trebuchet.jpg";
	private Image unitsImages[] = new Image[UnitTypes.NUMBER_UNITS];
	
	/*Building Paths & Images*/
	private final String BARRACK_IMAGE_PATH = "GLI/src/images/Buildings/barrack.jpg";
	private final String CAPITAL_IMAGE_PATH = "GLI/src/images/Buildings/capital.jpg";
	private final String DOCK_IMAGE_PATH = "GLI/src/images/Buildings/dock.jpg";
	private final String DOOR_IMAGE_PATH = "GLI/src/images/Buildings/door.jpg";
	private final String MINE_IMAGE_PATH = "GLI/src/images/Buildings/mine.jpg";
	private final String QUARRY_IMAGE_PATH = "GLI/src/images/Buildings/quarry.jpg";
	private final String SAWMILL_IMAGE_PATH = "GLI/src/images/Buildings/sawmill.jpg";
	private final String TEMPLE_IMAGE_PATH = "GLI/src/images/Buildings/temple.jpg";
	private final String WALL_IMAGE_PATH = "GLI/src/images/Buildings/wall.jpg";
	private final String WINDMILL_IMAGE_PATH = "GLI/src/images/Buildings/windmill.jpg";
	private final String WORKSHOP_IMAGE_PATH = "GLI/src/images/Buildings/workshop.jpg";
	private Image buildingImages[] = new Image[BuildingTypes.NUMBER_BUILDINGS];
	
	/*Resources Path & Images*/
	private final String ARTIFACT_IMAGE_PATH = "GLI/src/images/Resources/artifact.jpg";
	private final String FOOD_IMAGE_PATH = "GLI/src/images/Resources/food.jpg";
	private final String GOLD_IMAGE_PATH = "GLI/src/images/Resources/gold.jpg";
	private final String STONE_IMAGE_PATH = "GLI/src/images/Resources/stone.jpg";
	private final String WOOD_IMAGE_PATH = "GLI/src/images/Resources/wood.jpg";
	private Image resourceImages[] = new Image[ResourceTypes.NUMBER_TYPE_RESOURCES];

	/**
	 * Load all images that application needs and create an instance of ImagesUtility to retrieve those images.
	 */
	public ImagesUtility() {
		//we just load all images and store them in arrays
		loadImages();
	}

	private void loadImages() {
		logger.info("=== Starting to load all image ===");
		
		//load Units images
		loadUnitImage(UnitTypes.UNIT_INFANTRY, INFANTRY_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_ARCHER, ARCHER_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_BATTERING_RAM, BATTERING_RAM_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_BOAT, BOAT_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_CAVALRY, CAVALRY_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_PIKEMAN, SPEARMAN_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_TREBUCHET, TREBUCHET_IMAGE_PATH);
		
		//load Building images
		loadBuildingImage(BuildingTypes.BUILDING_BARRACK, BARRACK_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_CAPITAL, CAPITAL_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_DOCK, DOCK_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_DOOR, DOOR_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_MINE, MINE_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_QUARRY, QUARRY_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_SAWMILL, SAWMILL_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_TEMPLE, TEMPLE_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_WALL, WALL_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_WINDMILL, WINDMILL_IMAGE_PATH);
		loadBuildingImage(BuildingTypes.BUILDING_WORKSHOP, WORKSHOP_IMAGE_PATH);
		
		//load Resources images
		loadResourceImage(ResourceTypes.RESOURCE_ARTIFACT, ARTIFACT_IMAGE_PATH);
		loadResourceImage(ResourceTypes.RESOURCE_FOOD, FOOD_IMAGE_PATH);
		loadResourceImage(ResourceTypes.RESOURCE_GOLD, GOLD_IMAGE_PATH);
		loadResourceImage(ResourceTypes.RESOURCE_STONE, STONE_IMAGE_PATH);
		loadResourceImage(ResourceTypes.RESOURCE_WOOD, WOOD_IMAGE_PATH);

		logger.info("=== DONE ===\n");
	}
	
	/**
	 * Returns unit image of unit wanted
	 * @param unitType the unit type ID
	 * @return the image of the unit wanted
	 * @see UnitTypes
	 */
	public Image getUnitsImage(int unitType) {
		//check for errors, if buildingType <= 0, then no image returned
		if (unitType <= 0) {
			return null;
		}
		return unitsImages[unitType - 1];
	}
	
	/**
	 * Returns building image of unit wanted
	 * @param buildingType the building type ID
	 * @return the image of the building wanted
	 * @see BuildingTypes
	 */
	public Image getBuildingImage(int buildingType) {
		//check for errors, if buildingType <= 0, then no image returned
		if (buildingType <= 0) {
			return null;
		}
		return buildingImages[buildingType - 1];
	}
	
	/**
	 * Returns resource image of resource wanted
	 * @param resourceType the resource type ID
	 * @return the image of the resource wanted
	 * @see ResourceTypes
	 */
	public Image getResourceImage(int resourceType) {
		//check for errors, if buildingType <= 0, then no image returned
		if (resourceType <= 0) {
			return null;
		}
		//check if resourceType is Artifact's (special case for this type)
		if(resourceType == ResourceTypes.RESOURCE_ARTIFACT) {
			resourceType = ResourceTypes.NUMBER_TYPE_RESOURCES;
		}
		return resourceImages[resourceType - 1];
	}
	

	/**
	 * Loads an image and put it in array dedicated to units images.
	 * @param unitType the type of unit who will be assigned to the image
	 * @param path where image is located
	 */
	private void loadUnitImage(int unitType, String path) {
		try {
			File f = new File(path);
			//-1 because unitTypes starts to 1
			unitsImages[unitType - 1] = ImageIO.read(f);
			logger.info("Image " + path + " successfully loaded");
		} catch (IOException e) {
			System.err.println("--Cannot read input file--");
			logger.error("Image " + path + " failed to load : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Loads an image and put it in array dedicated to building images.
	 * @param buildingType the type of building who will be assigned to the image
	 * @param path where image is located
	 * @see BuildingTypes
	 */
	private void loadBuildingImage(int buildingType, String path) {
		try {
			File f = new File(path);
			//-1 because building types starts to 1
			buildingImages[buildingType - 1] = ImageIO.read(f);
			logger.info("Image " + path + " successfully loaded");
		} catch (IOException e) {
			System.err.println("--Cannot read input file--");
			logger.error("Image " + path + " failed to load");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads an image and put it in array dedicated to Resource images.
	 * @param resourceType the type of Resource who will be assigned to the image
	 * @param path where image is located
	 * @see ResourceTypes
	 */
	private void loadResourceImage(int resourceType, String path) {
		try {
			File f = new File(path);
			
			//we just check if resource is Artifact, because Artifact type is > than array size
			if(resourceType == ResourceTypes.RESOURCE_ARTIFACT)
				resourceType = ResourceTypes.NUMBER_TYPE_RESOURCES;
			
			//-1 because building types starts to 1
			resourceImages[resourceType - 1] = ImageIO.read(f);
			logger.info("Image " + path + " successfully loaded");
		} catch (IOException e) {
			System.err.println("--Cannot read input file--");
			logger.error("Image " + path + " failed to load");
			e.printStackTrace();
		}
	}

}

package GUI.drawing;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import data.unit.UnitTypes;

/**
 * Handles images loadings and storing during process.
 * @author Aldric Vitali Silvestre
 *
 */
public class ImagesUtility {
	
	/*Units Paths & Images*/
	private final String INFANTRY_IMAGE_PATH = "GLI/src/images/Units/infantry.jpg";
	private final String ARCHER_IMAGE_PATH = "GLI/src/images/Units/Archer.jpg";
	private final String BATTERING_RAM_IMAGE_PATH = "GLI/src/images/Units/BatteringRam.jpg";
	private final String BOAT_IMAGE_PATH = "GLI/src/images/Units/Boat.jpg";
	private final String CAVALRY_IMAGE_PATH = "GLI/src/images/Units/Cavalry.jpg";
	private final String SPEARMAN_IMAGE_PATH = "GLI/src/images/Units/spear.jpg";
	private final String TREBUCHET_IMAGE_PATH = "GLI/src/images/Units/Trebuchet.jpg";
	
	private Image unitsImages[] = new Image[UnitTypes.NUMBER_UNITS];

	/**
	 * Load all images that application needs and create an instance of ImagesUtility to retrieve those images.
	 */
	public ImagesUtility() {
		loadImages();
	}

	private void loadImages() {
		//-1 because unitTypes starts to 1
		loadUnitImage(UnitTypes.UNIT_INFANTRY, INFANTRY_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_ARCHER, ARCHER_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_BATTERING_RAM, BATTERING_RAM_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_BOAT, BOAT_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_CAVALRY, CAVALRY_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_PIKEMAN, SPEARMAN_IMAGE_PATH);
		loadUnitImage(UnitTypes.UNIT_TREBUCHET, TREBUCHET_IMAGE_PATH);
		
		
		try {
			File f = new File(INFANTRY_IMAGE_PATH);
			unitsImages[UnitTypes.UNIT_INFANTRY - 1] = ImageIO.read(f);
		} catch (IOException e) {
			System.err.println("--Cannot read input file--");
			e.printStackTrace();
		}
	}

	private void loadUnitImage(int unitType, String path) {
		try {
			File f = new File(path);
			unitsImages[unitType - 1] = ImageIO.read(f);
		} catch (IOException e) {
			System.err.println("--Cannot read input file--");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns image of unit wanted
	 * @param unitType the unit type ID
	 * @return the image of the unit wanted
	 * @see UnitTypes
	 */
	public Image getUnitsImage(int unitType) {
		return unitsImages[unitType - 1];
	}

}

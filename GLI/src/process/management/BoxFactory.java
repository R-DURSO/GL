package process.management;

import data.boxes.GroundBox;
import data.boxes.WaterBox;

public class BoxFactory {

	private BoxFactory() {}
	
	public static GroundBox createGroundBox(int ressourceType) {
		return new GroundBox(ressourceType);
	}
	
	public static WaterBox createWaterBox() {
		return new WaterBox();
	}

}

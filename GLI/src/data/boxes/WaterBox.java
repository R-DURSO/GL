package data.boxes;

import java.io.Serializable;

/**
 * <p>All {@link data.boxes.Box Boxes} that are made of Water</p>
 * <p>Can only be traveled on by {@link data.unit.Boat Boat}</p>
 * @author Maxence
 */
public class WaterBox extends Box implements Serializable{

	public WaterBox() {
		super();
	}

	public String toString() {
		return "WaterBox " + super.toString();
	}
}

package data.building.special;

import java.io.Serializable;

/**
 * This Building type is not a playable Building, it simply ensures that 2 Buildings are never constructed at the same position
 * during preparation time. 
 * <b>This Class must be used only with {@link process.management.ActionValidator}</b>
 * @author Aldric Vitali Silvestre
 * @see process.management.ActionValidator
 */
public class PhantomBuilding extends BuildingSpecial implements Serializable{

	public PhantomBuilding(){
		super(0, 0);
	}

	@Override
	public int getType() {
		return -1;
	}
	
	public String toString() {
		return "Phantom";
	}

}

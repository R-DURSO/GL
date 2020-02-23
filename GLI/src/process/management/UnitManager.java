package process.management;
import data.boxes.*;
import data.resource.ResourceTypes;
import data.unit.*;
import data.Power;

/**
 * Singleton class to manipulate units : creation, update and deletion
 * No much verification made here, they are principally made in {@link ActionValidator}
 */
public class UnitManager {
	
	private static UnitManager instance = new UnitManager();
	
	public static UnitManager getInstance() { return instance; }
	
	public void addUnits(Power power, Box box, int unitType, int numberUnits) {
		//we will check if there is already units of the same type on the box
		if(box.hasUnit()) {
			Units unitsOnBox = box.getUnit();
			int numberUnitsOnBox = unitsOnBox.getNumber();
			int numberUnitsNeeded = numberUnits + numberUnitsOnBox;
			if(numberUnitsNeeded <= unitsOnBox.getMaxNumber())
				unitsOnBox.addNumber(numberUnits);
			else
				// à changer car faux
				unitsOnBox.addNumber(20);
		}else
			box.setUnit(createUnit(unitType, numberUnits));
	}
	
	private Units createUnit(int type, int nb) {
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			return new Infantry(nb);
		case UnitTypes.UNIT_ARCHER:
			return new Archer(nb);
		case UnitTypes.UNIT_CAVALRY:
			return new Cavalry(nb);
		case UnitTypes.UNIT_PIKEMAN:
			return new Pikeman(nb);
		case UnitTypes.UNIT_BATTERING_RAM:
			return new BatteringRam(nb);
		case UnitTypes.UNIT_TREBUCHET:
			return new Trebuchet();
		case UnitTypes.UNIT_BOAT:
			return new Boat();
		default:
			return null;
		}
	}
	
	public boolean hasAlreadyUnits (Power pow, Box box) {
		return box.hasUnit();
	}
	
	public boolean canAfford (Power pow, int type, int nb) {
		if (pow.getResource(ResourceTypes.RESOURCE_GOLD).getAmount() >= (createUnit(type,nb).getCost() * nb)) {
			return true;
		}
		else {
			return false;
		}
	}
}
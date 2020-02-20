package process.management;
import data.boxes.*;
import data.resource.ResourceTypes;
import data.unit.*;
import data.Power;

public class UnitFactory {

	public UnitFactory(Power pow, Box box, int type, int nb) {
		Units unit;
		if (canAfford(pow, type, nb)) {
			switch(canTrain(pow, box, type)) {
			case 0:
				//Cas vide, on ne peut pas entrainer d'unité
				break;
			case 1:
				unit = createUnit(type,nb);
				box.setUnit(unit);
				break;
			case 2:
				unit = box.getUnit();
				unit.addNumber(nb);
				box.setUnit(unit);
				break;
			}
		}
	}
	
	public Units createUnit(int type, int nb) {
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
	
	public int canTrain (Power pow, Box box, int type) {
		if (box.getOwner() != null) {
			if (box.getOwner().equals(pow)) {
				if (box.getUnit() == null) {
					return 1;
				}
				else if (box.getUnit().getTypes() == type) {
					return 2;
				}
			}
		}
		return 0;
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
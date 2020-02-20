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
		Units unit = null;
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			unit = new Infantry(nb);
			break;
		case UnitTypes.UNIT_ARCHER:
			unit = new Archer(nb);
			break;
		case UnitTypes.UNIT_CAVALRY:
			unit = new Cavalry(nb);
			break;
		case UnitTypes.UNIT_PIKEMAN:
			unit = new Pikeman(nb);
			break;
		case UnitTypes.UNIT_BATTERING_RAM:
			unit = new BatteringRam(nb);
			break;
		case UnitTypes.UNIT_TREBUCHET:
			unit = new Trebuchet();
			break;
		case UnitTypes.UNIT_BOAT:
			unit = new Boat();
			break;
		}
		return unit;
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
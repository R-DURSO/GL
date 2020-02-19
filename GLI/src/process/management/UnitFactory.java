package process.management;
import data.boxes.*;
import data.unit.*;
import data.Power;

public class UnitFactory {

	public UnitFactory(Power pow, Box box, int type, int nb) {
		Units unit;
		switch(canTrain(pow, box, type)) {
		case 0:
			//Cas vide, on ne peut pas entrainer d'unité
		case 1:
			unit = createUnit(type,nb);
			box.setUnit(unit);
		case 2:
			unit = box.getUnit();
			unit.setNumber(unit.getNumber()+nb);
			box.setUnit(unit);
		}
	}
	
	public Units createUnit(int type, int nb) {
		Units unit = null;
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			unit = new Infantry(nb);
		case UnitTypes.UNIT_ARCHER:
			unit = new Archer(nb);
		case UnitTypes.UNIT_CAVALRY:
			unit = new Cavalry(nb);
		case UnitTypes.UNIT_PIKEMAN:
			unit = new Pikeman(nb);
		case UnitTypes.UNIT_BATTERING_RAM:
			unit = new BatteringRam(nb);
		case UnitTypes.UNIT_TREBUCHET:
			unit = new Trebuchet();
		case UnitTypes.UNIT_BOAT:
			unit = new Boat();
		}
		return unit;
	}
	
	public int canTrain (Power pow, Box box, int type) {
		if (box.getOwner().equals(pow)) {
			if (box.getUnit() == null) {
				return 1;
			}
			else if (box.getUnit().getTypes() == type) {
				return 2;
			}
		}
		return 0;
	}
}
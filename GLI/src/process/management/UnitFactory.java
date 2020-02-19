package process.management;
import data.boxes.*;
import data.unit.*;
import data.Power;

public class UnitFactory {

	public UnitFactory(Power pow, Box b, int type, int nb) {
		Units u;
		switch(canTrain(pow, b, type)) {
		case 0:
			//Cas vide, on ne peut pas entrainer d'unité
		case 1:
			u = createUnit(type,nb);
			b.setUnit(u);
		case 2:
			u = b.getUnit();
			u.setNumber(u.getNumber()+nb);
			b.setUnit(u);
		}
	}
	
	public Units createUnit(int type, int nb) {
		Units u = null;
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			u = new Infantry(nb);
		case UnitTypes.UNIT_ARCHER:
			u = new Archer(nb);
		case UnitTypes.UNIT_CAVALRY:
			u = new Cavalry(nb);
		case UnitTypes.UNIT_PIKEMAN:
			u = new Pikeman(nb);
		case UnitTypes.UNIT_BATTERING_RAM:
			u = new BatteringRam(nb);
		case UnitTypes.UNIT_TREBUCHET:
			u = new Trebuchet();
		case UnitTypes.UNIT_BOAT:
			u = new Boat();
		}
		return u;
	}
	
	public int canTrain (Power pow, Box b, int type) {
		if (b.getOwner().equals(pow)) {
			if (b.getUnit() == null) {
				return 1;
			}
			else if (b.getUnit().getTypes() == type) {
				return 2;
			}
		}
		return 0;
	}
}
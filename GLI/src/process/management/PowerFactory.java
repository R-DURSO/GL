package process.management;

import data.building.special.Capital;
import data.Power;

public class PowerFactory {
	private PowerFactory() {}
	
	public static Power createPower(String name) {
		return new Power(name);
	}
	
	public static Power createPower(String name, Capital capital) {
		return new Power(name, capital);
	}
}

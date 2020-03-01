package process.management;

import data.Position;
import data.Power;

public class PowerFactory {
	private PowerFactory() {}
	
	public static Power createPower(String name) {
		return new Power(name);
	}
	
	public static Power createPower(String name, Position capital) {
		return new Power(name, capital);
	}
}

package process.management;

import data.Power;

public class PowerFactory {
	private PowerFactory() {}
	
	public static Power createPower(String name) {
		return new Power(name);
	}

}

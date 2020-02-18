package process.management;

import data.Power;
import data.resource.Resource;
import data.resource.ResourceTypes;

public class PowerFactory {
	private PowerFactory() {}
	
	public static Power createPower(String name) {
		return new Power(name);
	}

}

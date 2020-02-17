package process.management;

import data.Power;
import data.ressource.Resource;
import data.ressource.ResourceTypes;

public class PowerFactory {
	private PowerFactory() {}
	
	public static Power createPower(String name) {
		Power p = new Power(name);
		Resource[] ress = new Resource[6];
		ress[ResourceTypes.RESOURCE_ACTIONS - 1] = ResourcesFactory.createAction();
		ress[ResourceTypes.RESOURCE_FOOD - 1] = ResourcesFactory.createFood();
		ress[ResourceTypes.RESOURCE_GOLD - 1] = ResourcesFactory.createGold();
		ress[ResourceTypes.RESOURCE_WOOD - 1] = ResourcesFactory.createWood();
		ress[ResourceTypes.RESOURCE_STONE - 1] = ResourcesFactory.createStone();
		ress[ResourceTypes.RESOURCE_SCORE - 1] = ResourcesFactory.createScore();
		p.setResource(ress);
		return p;
	}

}

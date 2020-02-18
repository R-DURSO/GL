package process.management;

import data.resource.ActionPoints;
import data.resource.Food;
import data.resource.Gold;
import data.resource.Score;
import data.resource.Stone;
import data.resource.Wood;

public class ResourcesFactory {

	public static ActionPoints createAction() {
		return new ActionPoints();
	}
	
	public static Food createFood() {
		return new Food();
	}
	
	public static Gold createGold() {
		return new Gold();
	}
	
	public static Score createScore() {
		return new Score();
	}
	
	public static Stone createStone() {
		return new Stone();
	}
	
	public static Wood createWood() {
		return new Wood();
	}

}

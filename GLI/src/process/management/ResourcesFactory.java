package process.management;

import data.resource.ActionPoints;
import data.resource.Food;
import data.resource.Gold;
import data.resource.Score;
import data.resource.Stone;
import data.resource.Wood;
import data.InitialValue;
public class ResourcesFactory {

	public static ActionPoints createAction() {
		return new ActionPoints( InitialValue.ACTION_INITIAL_VALUE);
	}
	
	public static Food createFood() {
		return new Food(InitialValue.FOOD_INITAL_VALUE);
	}
	
	public static Gold createGold() {
		return new Gold(InitialValue.GOLD_INITAL_VALUE);
	}
	
	public static Score createScore() {
		return new Score();
	}
	
	public static Stone createStone() {
		return new Stone(InitialValue.STONE_INITAL_VALUE);
	}
	
	public static Wood createWood() {
		return new Wood(InitialValue.WOOD_INITAL_VALUE);
	}

}

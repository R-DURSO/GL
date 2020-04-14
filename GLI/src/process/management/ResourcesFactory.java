package process.management;

import data.resource.*;
import data.InitialValues;

/**
 * Factory class used to create different {@linkplain data.resource.Resource} types.
 */
public class ResourcesFactory {

	public static ActionPoints createAction() {
		return new ActionPoints(InitialValues.ACTION_INITIAL_VALUE, InitialValues.ACTION_BASE_PRODUCTION);
	}
	
	public static Food createFood() {
		return new Food(InitialValues.FOOD_INITIAL_VALUE, InitialValues.FOOD_BASE_PRODUCTION);
	}
	
	public static Gold createGold() {
		return new Gold(InitialValues.GOLD_INITIAL_VALUE, InitialValues.GOLD_BASE_PRODUCTION);
	}
	
	public static Score createScore() {
		return new Score();
	}
	
	public static Stone createStone() {
		return new Stone(InitialValues.STONE_INITIAL_VALUE, InitialValues.STONE_BASE_PRODUCTION);
	}
	
	public static Wood createWood() {
		return new Wood(InitialValues.WOOD_INITIAL_VALUE, InitialValues.WOOD_BASE_PRODUCTION);
	}
	
	public static Artifact createArtifact() {
		return new Artifact();
	}
	
}

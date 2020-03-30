package process.management;

import data.resource.*;
import data.InitialValue;
public class ResourcesFactory {

	public static ActionPoints createAction() {
		return new ActionPoints(InitialValue.ACTION_INITIAL_VALUE, InitialValue.ACTION_BASE_PRODUCTION);
	}
	
	public static Food createFood() {
		return new Food(InitialValue.FOOD_INITIAL_VALUE, InitialValue.FOOD_BASE_PRODUCTION);
	}
	
	public static Gold createGold() {
		return new Gold(InitialValue.GOLD_INITIAL_VALUE, InitialValue.GOLD_BASE_PRODUCTION);
	}
	
	public static Score createScore() {
		return new Score();
	}
	
	public static Stone createStone() {
		return new Stone(InitialValue.STONE_INITIAL_VALUE, InitialValue.STONE_BASE_PRODUCTION);
	}
	
	public static Wood createWood() {
		return new Wood(InitialValue.WOOD_INITIAL_VALUE, InitialValue.WOOD_BASE_PRODUCTION);
	}
	
	public static Artifact createArtifact() {
		return new Artifact();
	}
	
	public static String getResourceType(int ResourceType) {
		switch(ResourceType) {
		case ResourceTypes.RESOURCE_ACTIONS:
			return "Actions";
		case ResourceTypes.RESOURCE_FOOD:
			return "Food";
		case ResourceTypes.RESOURCE_WOOD:
			return "Wood";
		case ResourceTypes.RESOURCE_STONE:
			return "Stone";
		case ResourceTypes.RESOURCE_GOLD:
			return "Gold";
		case ResourceTypes.RESOURCE_SCORE:
			return "Score";
		case ResourceTypes.RESOURCE_ARTIFACT:
			return "Artifact";
		default:
			return "Unknown";
		}
	}
}

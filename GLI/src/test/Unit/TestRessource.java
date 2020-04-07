package test.Unit;

import org.junit.Before;
import org.junit.Test;

import data.InitialValue;
import data.Power;
import data.resource.Gold;
import data.resource.Resource;
import data.resource.ResourceTypes;
import process.management.PowerManager;
import process.management.ResourcesFactory;

public class TestRessource {
	private Power powers[];
	private Resource gold,wood,stone,food,action,score;
	@Before
	public void prepare(){
		generatePowers();
		initResource();
	}
	@Test
	public void createGold() {
		ResourcesFactory.createGold();
	}
	
	@Test
	public void createFood() {
		ResourcesFactory.createFood();
	}
	@Test
	public void createAction() {
		ResourcesFactory.createAction();
	}
	@Test
	public void createStone() {
		ResourcesFactory.createStone();
	}
	@Test
	public void createWood() {
		ResourcesFactory.createWood();
	}
	@Test
	public void createScore() {
		ResourcesFactory.createScore();
	}
	@Test
	public void createArtifact() {
		ResourcesFactory.createArtifact();
	}
	/* Pas sur que je dois tester sa 
	 @Test
	public void addTurnResourceGold() {
		powers[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_GOLD, 50);
	}
	@Test
	public void addTurnResourceAction() {
		powers[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_ACTIONS, 5);
	}
	@Test
	public void addTurnResourceFood() {
		powers[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, 50);
	}
	@Test
	public void addTurnResourceWood() {
		powers[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_WOOD, 50);
	}
	@Test
	public void addTurnResourceStone() {
		powers[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_STONE, 50);
	}
	@Test
	public void addTurnResourceScore() {
		powers[0].addResourcesProductionPerTurn(ResourceTypes.RESOURCE_SCORE, 50);
	}
	*/
	@Test
	public void initGoldValue() {
		gold.addValue(50);
	}
	@Test
	public void initStoneValue() {
		stone.addValue(50);
	}
	@Test
	public void initWoodValue() {
		wood.addValue(50);
	}
	@Test
	public void initActionValue() {
		action.addValue(50);
	}
	@Test
	public void initScoreValue() {
		score.addValue(50);
	}
	@Test
	public void initFoodValue() {
		food.addValue(50);
	}
	@Test
	public void initFoodValuePerturn() {
		food.addProductionPerTurn(10);;
	}
	@Test
	public void initStoneValuePerturn() {
		stone.addProductionPerTurn(10);;
	}
	@Test
	public void initWooddValuePerturn() {
		wood.addProductionPerTurn(10);;
	}
	@Test
	public void initScoreValuePerturn() {
		score.addProductionPerTurn(10);;
	}
	@Test
	public void initActionValuePerturn() {
		action.addProductionPerTurn(10);;
	}
	@Test
	public void initGoldValuePerturn() {
		gold.addProductionPerTurn(10);;
	}

	


private void generatePowers() {
	powers = new Power[1];
	for(int i = 0; i < 1; i++){
		powers[i] = PowerManager.createPower("" + (i+1), 0);
	}
}
private void initResource() {
	this.action = ResourcesFactory.createAction(); 
	this.gold = ResourcesFactory.createGold();
	this.food = ResourcesFactory.createFood();
	this.stone = ResourcesFactory.createStone();
	this.wood = ResourcesFactory.createWood();
	this.score= ResourcesFactory.createScore();
	
}
}

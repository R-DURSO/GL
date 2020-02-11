package process.management;

import data.ressource.Action;
import data.ressource.Food;
import data.ressource.Gold;
import data.ressource.Score;
import data.ressource.Stone;
import data.ressource.Wood;

public class RessourcesFactory {

	public static Action createAction() {
		return new Action();
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

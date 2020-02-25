package process.game;

import data.GameMap;
import data.Power;
import process.management.MapBuilder;
import process.management.PowerFactory;

public class Start {
	private static Start instance = new Start();
	
	public static Start getInstance() {
		return instance;
	}
	
	private Start(){}
	
	public Power[] initPowers(int numberplayer){
		Power powers[] = new Power[numberplayer];
		for(int i = 0; i < numberplayer; i++) {
			powers[i] = PowerFactory.createPower("Joueur " + (i + 1));
		}
		return powers;
	}
	
	public GameMap generateMap(int size , int waterAmout, Power powers[]) {
		MapBuilder mb = new MapBuilder(size, waterAmout, powers);
		mb.displayMap();
		GameMap map = mb.buildMap();
		return map;
	}
	
	
}

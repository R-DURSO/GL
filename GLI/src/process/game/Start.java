package process.game;

import data.GameMap;
import data.Power;
import process.management.MapBuilder;
import process.management.PowerFactory;

public class Start {
	private Power powers[];
	private GameMap map;
	public Start(int numberplayer , int size , int waterAmout) {
		initPlayer(numberplayer); 
		generatedMap(size, waterAmout);	
		new GameLoop(powers,  map);
	}
	
	public void initPlayer(int numberplayer){
		powers = new Power[numberplayer];
		for(int i = 0; i < numberplayer; i++) {
			this.powers[i] = PowerFactory.createPower("Joueur " + (i + 1));
		}
	}
	
	public void generatedMap(int size , int waterAmout) {
		MapBuilder mb = new MapBuilder(size, waterAmout, powers);
		mb.displayMap();
		map = mb.buildMap();
		
	}
}

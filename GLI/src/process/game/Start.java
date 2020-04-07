package process.game;

import data.GameMap;
import data.Power;
import process.management.MapBuilder;
import process.management.PowerManager;

public class Start {
	private GameMap map;
	private Power powers[];
	
	public Start(int numberPlayers, int mapSize, int waterAmount, int aiLevels[]){
		initPowers(numberPlayers);
		this.map=null;
		generateMap(mapSize, waterAmount, this.powers);
		/* initinalisation des ia qui ne sont pas encore crée donc 
		 * IA1 = IAFactory.createIA(aiLevels[0]);
		 * IA2 = IAFactory.createIA(aiLevels[0]);
		 * IA3 = IAFactory.createIA(aiLevels[0]);
		 */
	}
	
	public void initPowers(int numberplayer){
		this.powers = new Power[numberplayer];
		for(int i = 0; i < numberplayer; i++) {
			powers[i] = PowerManager.createPower("Joueur " + (i + 1));
		}
		
	}
	
	public void generateMap(int size , int waterAmout, Power powers[]) {
		MapBuilder mb = new MapBuilder(size, waterAmout, powers);
		this.map = mb.buildMap();
	}
	
	public GameMap getMap() {
		return map;
	}
	public Power[] getPowers() {
		return powers;
	}
}

package process.game;

import GUI.components.menu.PreferencesPanel;
import data.GameMap;
import data.Power;
import process.management.MapBuilder;
import process.management.PowerFactory;

public class Start {
	private GameMap map;
	private Power powers[];
	
	

	
	public Start(PreferencesPanel preferences){
		initPowers(preferences.getNumberPlayers());
		this.map=null;
		generateMap(preferences.getMapSize(),preferences.getWaterAmount(), this.powers);
		System.out.println(map);
		/* initinalisation des ia qui ne sont pas encore crée donc 
		 * IA1 = new createIA(preferences.getAi1Level());
		 * IA2 = new createIA(preferences.getAi2Level());
		 * IA3 = new createIA(preferences.getAi3Level());
		 */
	}
	
	public void initPowers(int numberplayer){
		this.powers = new Power[numberplayer];
		for(int i = 0; i < numberplayer; i++) {
			powers[i] = PowerFactory.createPower("Joueur " + (i + 1));
		}
		
	}
	
	public void generateMap(int size , int waterAmout, Power powers[]) {
		MapBuilder mb = new MapBuilder(size, waterAmout, powers);
		mb.displayMap();
		GameMap map = mb.buildMap();
		 this.map=map;
	}
	
	public GameMap getMap() {
		return map;
	}
	public Power[] getPower() {
		return powers;
	}
}

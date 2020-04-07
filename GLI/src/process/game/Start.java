package process.game;

import org.apache.log4j.Logger;
import log.LoggerUtility;

import data.GameConstants;
import data.GameMap;
import data.Power;

import process.management.MapBuilder;
import process.management.PowerManager;
import process.management.UnitManager;

public class Start {
	private GameMap map;
	private Power powers[];
	private static Logger Logger = LoggerUtility.getLogger(UnitManager.class, GameConstants.LOG_TYPE);
	
	public Start(int numberPlayers, int mapSize, int waterAmount, int aiLevels[]) {
		Logger.info("\n=== START OF THE GAME ===");
		Logger.info("Initialisation with: "+numberPlayers+" Power on a "+mapSize+"x"+mapSize+" Map\n");
		initPowers(numberPlayers);
		this.map=null;
		generateMap(mapSize, waterAmount, this.powers);
		/* initinalisation des ia qui ne sont pas encore cr�e donc 
		 * IA1 = IAFactory.createIA(aiLevels[0]);
		 * IA2 = IAFactory.createIA(aiLevels[0]);
		 * IA3 = IAFactory.createIA(aiLevels[0]);
		 */
	}
	
	public void initPowers(int numberplayer) {
		this.powers = PowerManager.createDefaultPowers(numberplayer);
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

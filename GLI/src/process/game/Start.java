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
	
	public Start(int numberPlayers, String playerNames[], int mapSize, int waterAmount, int aiLevels[]) {
		Logger.info("\n=== START OF THE GAME ===");
		Logger.info("Initialisation with: "+numberPlayers+" Power on a "+mapSize+"x"+mapSize+" Map. WaterAmount: " + waterAmount +".\n");
		initPowers(numberPlayers, playerNames, aiLevels);
		this.map=null;
		generateMap(mapSize, waterAmount, this.powers);
	}
	
	public void initPowers(int numberplayer, String playerNames[], int aiLevels[]){
		this.powers = new Power[numberplayer];
		for(int i = 0; i < numberplayer; i++) {
			powers[i] = PowerManager.createPower(playerNames[i], aiLevels[i]);
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

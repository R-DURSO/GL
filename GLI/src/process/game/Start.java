package process.game;

import org.apache.log4j.Logger;
import log.LoggerUtility;

import data.GameConstants;
import data.GameMap;
import data.Power;

import process.management.MapBuilder;
import process.management.PowerFactory;
import process.management.PowerManager;
import process.management.UnitManager;

/**
 * Initialize the {@link process.game.GameLoop GameLoop}.
 * <br>Use a {@link data.GameMap GameMap} and a List of {@link data.Power Power}
 */
public class Start {
	private GameMap map;
	private Power powers[];
	private static Logger Logger = LoggerUtility.getLogger(UnitManager.class, GameConstants.LOG_TYPE);
	
	public Start(int numberPlayers, String playerNames[], int mapSize, int waterAmount, boolean artifact, int aiLevels[]) {
		Logger.info("=== START OF THE GAME ===");
		Logger.info("Initialisation with: "+numberPlayers+" Power on a "+mapSize+"x"+mapSize+" Map. WaterAmount: " + waterAmount +"%.\n");
		initPowers(numberPlayers, playerNames, aiLevels);
		this.map = null;
		generateMap(mapSize, waterAmount, artifact, this.powers);
		setPowersStats();
	}
	
	public void initPowers(int numberplayer, String playerNames[], int aiLevels[]) {
		this.powers = new Power[numberplayer];
		for(int i = 0; i < numberplayer; i++) {
			powers[i] = PowerFactory.createPower(playerNames[i], aiLevels[i]);
		}
	}
	
	public void setPowersStats() {
		for(int i = 0; i < powers.length; i++){
			PowerManager.getInstance().refreshPowerStats(powers[i], map);
		}
	}
	
	public void generateMap(int size , int waterAmout, boolean artifact, Power powers[]) {
		MapBuilder mb = new MapBuilder(size, waterAmout, artifact, powers);
		this.map = mb.buildMap();
	}
	
	public GameMap getMap() {
		return map;
	}
	public Power[] getPowers() {
		return powers;
	}
}

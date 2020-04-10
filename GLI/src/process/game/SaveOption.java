package process.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import data.GameConstants;
import data.GameMap;
import log.LoggerUtility;

public class SaveOption {
	private static Logger Logger = LoggerUtility.getLogger(SaveOption.class, "text");

	public SaveOption() {

	}

	public GameMap loadGame() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream stream = new ObjectInputStream(new FileInputStream(GameConstants.SAVE_LOCATION));
		GameMap map = (GameMap) stream.readObject();
		stream.close();
		return map;
	}

	public void save(File file, GameMap map) throws FileNotFoundException, IOException {
		Logger.info("=== SAVE GAME  ===");
		ObjectOutputStream saveStream = new ObjectOutputStream(new FileOutputStream(file));
		//all data in the game is in the GameMap, so, we just have to save that map in order to save the game
		saveStream.writeObject(map);
		Logger.info("Map is saved");
		saveStream.close();
	}
}

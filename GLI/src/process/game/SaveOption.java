package process.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import data.GameMap;
import data.Power;
import log.LoggerUtility;

public class SaveOption {
	private static Logger Logger = LoggerUtility.getLogger(SaveOption.class, "text");
	public SaveOption() {

	}
	public GameMap  loadMap() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream stream = new ObjectInputStream(new FileInputStream("game.ser"));
		 GameMap map = null;
		 map= (GameMap) stream.readObject();
		return map;
	}

public 	void sauvegarder(File file, GameMap map, Power player) throws FileNotFoundException, IOException {
	Logger.info("=== FIN DE PARTIE  ===");
	ObjectOutputStream gamesave= new ObjectOutputStream(new FileOutputStream(file));
	gamesave.writeObject(map);
	Logger.info(" map is serialized  ");
	gamesave.writeObject(player);
	Logger.info("player is serialized");
	gamesave.close();
	
}
}

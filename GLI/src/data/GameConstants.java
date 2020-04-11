package data;

import java.io.Serializable;

public class GameConstants implements Serializable {
	private GameConstants() {}
	
	public static final int WATER_AMOUNT_LITTLE = 20;
	public static final int WATER_AMOUNT_NORMAL = 40;
	public static final int WATER_AMOUNT_MANY = 60;
	
	
	public static final int HUMAN_PLAYER = -1;
	public static final int AI_EASY = 0;
	public static final int AI_NORMAL = 1;
	public static final int AI_HARD = 2;
	
	public static final String LOG_TYPE = "text";
	public static final String SAVE_LOCATION = "GLI/src/save/game.ser";
}

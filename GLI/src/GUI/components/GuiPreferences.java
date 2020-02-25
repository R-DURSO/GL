package GUI.components;

import java.awt.Font;

public class GuiPreferences {
	public static final int RATIO_WIDTH = 16;
	public static final int RATIO_HEIGHT = 9;

	public static final int HEIGHT = 720;
	
	public static final int WIDTH = HEIGHT * RATIO_WIDTH/RATIO_HEIGHT;
	
	public static final int BASIC = WIDTH / 1280;

	public static final int SCALE = BASIC * 20;
	
	public static final int FONT_SIZE = WIDTH / 50;
	
	public static final Font BASE_FONT = new Font("Arial", Font.PLAIN, FONT_SIZE);
	
	public static final int GAME_PANELS_RATIO_HEIGHT = 6;
	public static final int GAME_PANELS_SUBSTRACT_HEIGHT = GAME_PANELS_RATIO_HEIGHT - 2;
	
	
	public static final int GAME_MAIN_RATIO_WIDTH = 6;
	public static final int GAME_MAIN_RATIO_SUBSTRACT_WIDTH = GAME_MAIN_RATIO_WIDTH - 1;
	
	
	
}

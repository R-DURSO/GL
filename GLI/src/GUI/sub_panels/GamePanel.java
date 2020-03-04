package GUI.sub_panels;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.game.GameButtonsPanel;
import GUI.components.game.GameInfoPanel;
import GUI.components.game.MainGamePanel;
import data.GameMap;

import data.Power;
import data.resource.Resource;
import data.resource.ResourceTypes;
import data.boxes.*;
import data.unit.*;


/**
 * <p>The class that control the game window</p>
 * <p>Use GameInfoPanel & MainGamePanel</p>
 */
public class GamePanel extends JPanel{
	private static final long serialVersionUID = 7722109867943150729L;
	private MainWindow context;
	
	private final Dimension INFO_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / GuiPreferences.GAME_PANELS_RATIO_HEIGHT);
	private final Dimension MAIN_DIMENSION = 
			new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT * GuiPreferences.GAME_PANELS_SUBSTRACT_HEIGHT / GuiPreferences.GAME_PANELS_RATIO_HEIGHT);
	private final Dimension BUTTONS_DIMENSION = INFO_DIMENSION;
	
	private JPanel gameInfoPanel;
	private JPanel mainGamePanel;
	private JPanel gameButtonsPanel = new GameButtonsPanel();
	
	
	public GamePanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		gameButtonsPanel.setPreferredSize(BUTTONS_DIMENSION);

		
	}
	public void initMainGamePanel(GameMap map, Power powers[]) {
		this.mainGamePanel	= new MainGamePanel(map, powers);
		mainGamePanel.setPreferredSize(MAIN_DIMENSION);
		gameInfoPanel = new GameInfoPanel(powers[0].getResources(), map.getBox(0,0));
		gameInfoPanel.setPreferredSize(INFO_DIMENSION);
		add(gameInfoPanel);
		add(mainGamePanel);
		add(gameButtonsPanel);
	}
}

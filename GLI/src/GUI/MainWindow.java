package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.components.GuiPreferences;
import GUI.components.game.MainGamePanel;
import GUI.components.menu.PreferencesPanel;
import GUI.sub_panels.GamePanel;
import GUI.sub_panels.MenuPanel;
import process.game.GameLoop;
import process.game.Start;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6569351248766478602L;
	
	private final String GAME_WINDOW = "game";
	private final String MENU_WINDOW = "menu";
	private GamePanel gamePanel = new GamePanel(this);
	private MenuPanel menuPanel = new MenuPanel(this);
	private MainWindow context = this;
	private CardLayout cardLayout = new CardLayout();

	public MainWindow() {
		super("Conquête");
		setPreferredSize(new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT));
		init();
		getContentPane().add(gamePanel, "game");
		getContentPane().add(menuPanel, "menu");
		cardLayout.show(getContentPane(), "menu");
	}

	private void init() {
		this.setLayout(cardLayout);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	public void changeWindow() {
		cardLayout.next(getContentPane());
	}
	
	public void newGame() {
		PreferencesPanel preferences = menuPanel.getPreferencesPanel();
		int numberPlayers = preferences.getNumberPlayers();
		int mapSize = preferences.getMapSize();
		int waterAmount = preferences.getWaterAmount();
		int aiLevels[] = {preferences.getAi1Level(), preferences.getAi2Level(), preferences.getAi3Level()}; 
		Start starter = new Start(numberPlayers, mapSize, waterAmount, aiLevels);
		gamePanel.initGamePanel(starter.getMap(), starter.getPowers());
		cardLayout.show(getContentPane(), "game");
		
	}
	
	public void loadGame() {
		
	}
	
	

}

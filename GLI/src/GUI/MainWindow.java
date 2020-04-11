package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import GUI.components.GuiPreferences;
import GUI.components.menu.OptionsPanel;
import GUI.sub_panels.GamePanel;
import GUI.sub_panels.MenuPanel;
import GUI.sub_panels.VictoryPanel;
import data.GameConstants;
import data.GameMap;
import log.LoggerUtility;
import process.game.SaveOption;
import process.game.Start;

/**
 * <p>The Window of the Game</p>
 * <p>Can be switched between the {@link GUI.sub_panels.MenuPanel MenuPanel} and the {@link GUI.sub_panels.GamePanel GamePanel}.</p>
 * @author Aldric
 */
public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerUtility.getLogger(MainWindow.class, GameConstants.LOG_TYPE);
	
	public static final String GAME_WINDOW = "game";
	public static final String MENU_WINDOW = "menu";
	public static final String VICTORY_WINDOW = "victory";
	private GamePanel gamePanel = new GamePanel(this);
	private MenuPanel menuPanel = new MenuPanel(this);
	private VictoryPanel victoryPanel = new VictoryPanel(this);
	private MainWindow context = this;
	private CardLayout cardLayout = new CardLayout();
	private SaveOption saver = new SaveOption();

	public MainWindow() {
		super("Conquête");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT));
		init();
		getContentPane().add(gamePanel, GAME_WINDOW);
		getContentPane().add(menuPanel, MENU_WINDOW);
		getContentPane().add(victoryPanel, VICTORY_WINDOW);
		victoryPanel.initVictoryPanel("Albert", 1, 333);
		cardLayout.show(getContentPane(), VICTORY_WINDOW);
	}

	private void init() {
		this.setLayout(cardLayout);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
	}
	
	public void changeWindow(String windowName) {
		cardLayout.show(getContentPane(), windowName);
	}
	
	public void newGame() {
		
		OptionsPanel optionsPanel = menuPanel.getOptionsPanel();

		int numberPlayers = optionsPanel.getNumberPlayers();
		int mapSize = optionsPanel.getMapSize();
		int waterAmount = optionsPanel.getWaterAmount();
		boolean artifact = optionsPanel.getArtifactSpawn();
		int aiLevels[] = new int[numberPlayers];
		String playerNames[] = new String[numberPlayers];

		for(int i = 0; i < numberPlayers; i++){
			aiLevels[i] = optionsPanel.getPlayerAILevel(i + 1);
			playerNames[i] = optionsPanel.getPlayerName(i + 1);
		}
		
		Start starter = new Start(numberPlayers, playerNames, mapSize, waterAmount, artifact, aiLevels);
		gamePanel.initGamePanel(starter.getMap(), starter.getPowers());
		cardLayout.show(getContentPane(), GAME_WINDOW);
	}
	
	public void loadGame() {
		GameMap map = null;
		try {
			map = saver.loadGame();
			gamePanel.initGamePanel(map, map.getPowers());
			changeWindow(GAME_WINDOW);
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(context, "Une erreur lors du chargement de la partie est apparue : " + e.getMessage(), "erreur", JOptionPane.ERROR_MESSAGE);
			logger.error("Problem with loading : " + e.getMessage());
		}
	}
}

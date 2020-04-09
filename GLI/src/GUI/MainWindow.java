package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import GUI.components.GuiPreferences;
import GUI.components.menu.OptionsPanel;
import GUI.sub_panels.GamePanel;
import GUI.sub_panels.MenuPanel;
import data.GameMap;
import data.Power;
import process.game.SaveOption;
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
	private SaveOption game = new SaveOption();

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
		cardLayout.show(getContentPane(), MENU_WINDOW);
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
		Power player = null ;
		GameMap map = null ;
		try {
			map=game.loadMap();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(map.getSize());
		
	}
	
	

}

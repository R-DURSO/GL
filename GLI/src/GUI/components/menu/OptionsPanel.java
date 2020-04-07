package GUI.components.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import GUI.components.GuiPreferences;
import GUI.components.SliderPanel;
import GUI.components.menu.options_components.NumberPlayerPanel;
import GUI.components.menu.options_components.PlayersTabPanel;
import GUI.components.menu.options_components.WaterAmountPanel;
import GUI.sub_panels.MenuPanel;
import data.GameConstants;
import log.LoggerUtility;
import process.management.ActionValidator;

/**
 * Gui class that display and treat all user informations needed to initialize the game
 * @author Aldric Vitali Silvestre
 *
 */
public class OptionsPanel extends JPanel {
	
	private static Logger logger = LoggerUtility.getLogger(OptionsPanel.class, GameConstants.LOG_TYPE);

	//gui components
	private NumberPlayerPanel numberPlayerPanel;
	private SliderPanel mapSizePanel;
	private PlayersTabPanel playerTabPanel;
	private WaterAmountPanel waterAmountPanel;

	private JPanel bottomPanel = new JPanel();

	private JPanel sidePanel = new JPanel();

	//dimensions 
	public static final int WIDTH = (int) MenuPanel.PREFERENCES_DIMENSION.getWidth();
	public static final int HEIGHT = (int) MenuPanel.PREFERENCES_DIMENSION.getHeight();

	public static final Dimension MAIN_DIMENSION = new Dimension(WIDTH, HEIGHT);

	public static final Dimension SIDE_DIMENSION = new Dimension(WIDTH / 4 - 10, 3 * HEIGHT / 4);
	public static final Dimension INSIDE_SIDE_DIMENSION = new Dimension(WIDTH / 3, 3 * HEIGHT / 8);

	public static final Dimension PLAYER_TAB_DIMENSION = new Dimension(3 * WIDTH / 4, 3 * HEIGHT / 4);
	public static final Dimension MAP_SIZE_DIMENSION = new Dimension(WIDTH / 3, 2 * HEIGHT / 5);
	
	//map size

	private final int MIN_MAP_SIZE = 10;
	private final int INIT_MAP_SIZE = 12;
	private final int MAX_MAP_SIZE = 15;

	public OptionsPanel() {
		init();
		setVisible(true);
	}

	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLayout(new BorderLayout());
		setPreferredSize(MAIN_DIMENSION);

		sidePanel.setLayout(new BorderLayout());
		sidePanel.setPreferredSize(SIDE_DIMENSION);

		numberPlayerPanel = new NumberPlayerPanel(this);
		numberPlayerPanel.setPreferredSize(INSIDE_SIDE_DIMENSION);

		mapSizePanel = new SliderPanel("Taille de la carte", MAX_MAP_SIZE, MIN_MAP_SIZE, INIT_MAP_SIZE);
		mapSizePanel.setPreferredSize(MAP_SIZE_DIMENSION);
		bottomPanel.add(mapSizePanel);

		playerTabPanel = new PlayersTabPanel(this);
		playerTabPanel.setPreferredSize(PLAYER_TAB_DIMENSION);

		waterAmountPanel = new WaterAmountPanel();
		waterAmountPanel.setPreferredSize(INSIDE_SIDE_DIMENSION);

		sidePanel.add(numberPlayerPanel, BorderLayout.NORTH);
		sidePanel.add(waterAmountPanel, BorderLayout.SOUTH);

		this.add(playerTabPanel, BorderLayout.EAST);
		this.add(sidePanel, BorderLayout.WEST);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Hides or reveals a player tab (used by {@linkplain NumberPlayerPanel})
	 * @param playerIndex the number of the player concerned
	 * @param visible if this tab has to be visible or not
	 */
	public void setPlayerTabVisibility(int playerIndex, boolean visible) {
		playerTabPanel.setPlayerVisibility(playerIndex, visible);
	}
	
	public int getWaterAmount() {
		return waterAmountPanel.getWaterAmount();
	}

	public int getNumberPlayers() {
		return numberPlayerPanel.getNumberPlayers();
	}
	
	public String getPlayerName(int playerIndex) {
		return playerTabPanel.getPlayerName(playerIndex);
	}
	
	public int getPlayerAILevel(int playerIndex) {
		return playerTabPanel.getPlayerAILevel(playerIndex);
	}
	
	public int getMapSize() {
		return mapSizePanel.getValue();
	}
}

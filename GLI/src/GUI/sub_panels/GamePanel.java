package GUI.sub_panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.game.ActionsButtonsPanel;
import GUI.components.game.GameButtonsPanel;
import GUI.components.game.InfosPanel;
import GUI.components.game.MapPanel;
import GUI.components.game.PlayerResourcesPanel;
import data.GameConstants;
import data.GameMap;
import data.Position;
import data.Power;
import data.actions.Action;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.building.Building;
import data.building.special.PhantomBuilding;
import data.resource.ResourceTypes;
import data.unit.PhantomUnit;
import data.unit.Units;
import process.game.GameLoop;
import process.game.SaveOption;
import process.management.ActionValidator;

/**
 * <p>The class that control the Game {@link GUI.MainWindow Window}</p>
 * <p>Check from {@link process.game.GameLoop GameLoop} all action done in the Game</p>
 * <ul> Also contains various SubComponents
 * 		<li>{@link GUI.components.game.PlayerResourcesPanel PlayerResourcesPanel}</li>
 * 		<li>{@link GUI.components.game.InfosPanel infosPanel}</li>
 * 		<li>{@link GUI.components.game.MapPanel MapPanel}</li>
 * 		<li>{@link GUI.components.game.GameButtonsPanel GameButtonsPanel}</li>
 * </ul>
 */
public class GamePanel extends JPanel {
	private static final long serialVersionUID = 7722109867943150729L;
	private MainWindow window;

	//public static to be reusable in aother classes
	public static final Dimension DIM_RESOURCES = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 30);
	public static final Dimension DIM_MAP = new Dimension(4 * GuiPreferences.WIDTH / 5, 4 * GuiPreferences.HEIGHT / 5);
	public static final Dimension DIM_INFOS = new Dimension(GuiPreferences.WIDTH / 5, 4 * GuiPreferences.HEIGHT / 5);
	public static final Dimension DIM_BUTTONS = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 10);

	private PlayerResourcesPanel playerResourcesPanel;
	private InfosPanel infosPanel;
	private MapPanel mapPanel;
	private GameButtonsPanel gameButtonsPanel;

	// attributes used for game logic
	private GameMap map;
	private Position fromPosition = null;
	private Position targetPosition = null;
	private ActionValidator actionValidator;
	private Power player;
	private GameLoop gameLoop;
	// use for save of game  
	private SaveOption save;
	public GamePanel(MainWindow window) {
		this.window = window;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		gameButtonsPanel = new GameButtonsPanel(this);
		gameButtonsPanel.setPreferredSize(DIM_BUTTONS);
	}

	public void initGamePanel(GameMap map, Power powers[]) {
		this.map = map;
		this.player = powers[0];
		gameLoop = new GameLoop(map, powers);

		actionValidator = new ActionValidator(map);
		this.mapPanel = new MapPanel(this, map, powers);
		mapPanel.setPreferredSize(DIM_MAP);

		infosPanel = new InfosPanel();
		infosPanel.initInfosPanel(this, gameLoop.getPowers());
		infosPanel.setPreferredSize(DIM_INFOS);
		infosPanel.refreshStatsPanel();

		playerResourcesPanel = new PlayerResourcesPanel(powers[0].getResources());
		playerResourcesPanel.setPreferredSize(DIM_RESOURCES);

		// mouseListener
		mapPanel.addMouseMotionListener(new MouseMotionManager());
		mapPanel.addMouseListener(new MouseInputManager());

		add(playerResourcesPanel, BorderLayout.NORTH);
		add(mapPanel, BorderLayout.CENTER);
		add(infosPanel, BorderLayout.EAST);
		add(gameButtonsPanel, BorderLayout.SOUTH);
	}

	public void addAction(Action action, int actionType) {
		//add action to gameLoop, who will do actions later
		gameLoop.addAction(actionType, action);
		
		//we hide Action buttons
		gameButtonsPanel.hideActionButtons();
		
		//we reset positions to null, to hide them
		resetPositions();
		gameButtonsPanel.getActionsBoutonsPanel().setStateWaitingFromPosition();
		
		//refresh display
		refreshMap();
		playerResourcesPanel.refreshAll();
		
	}

	private void refreshMap() {
		mapPanel.refreshSelection(fromPosition, targetPosition);
		mapPanel.repaint();
	}
	
	/**
	 * End the current turn (from the player perspective)
	 */
	public void endPlayerTurn() {
		resetPositions();
		gameButtonsPanel.getActionsBoutonsPanel().setMajorButtonsVisibility(false);
		gameLoop.endTurn();
		infosPanel.refreshStatsPanel();
		refreshMap();
		playerResourcesPanel.refreshAll();
		repaint();
		
		//check if someone won
		int victoryType = gameLoop.checkVictoryConditions();
		if(victoryType != GameConstants.NO_VICTORY) {
			setVictoryScreen(victoryType);
		}
	}

	private void setVictoryScreen(int victoryType) {
		//get power for retireving stats
		Power winner = null;
		if(victoryType == GameConstants.VICTORY_TYPE_MILITARY)
			winner = gameLoop.getMilitaryWinner();
		else if(victoryType == GameConstants.VICTORY_TYPE_TEMPLE)
			winner = gameLoop.getTempleWinner();
			
		//if one of those victory really have been triggered
		if(winner != null) {
			//we can finally put those informations in a new window
			String winnerName = winner.getName();
			int winnerScore = winner.getResourceAmount(ResourceTypes.RESOURCE_SCORE);
			removePanels();
			window.initVictoryPanel(winnerName, victoryType, winnerScore);
		}
	}

	public void cancelAction() {
		resetPositions();
		gameButtonsPanel.getActionsBoutonsPanel().setStateWaitingFromPosition();
		gameButtonsPanel.changeMiddlePanel();
		refreshMap();
		repaint();
	}

	class MouseMotionManager implements MouseMotionListener {

		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			Box box = mapPanel.getBoxFromCoordinates(x, y);
			infosPanel.refreshBoxHover(box);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}

	class MouseInputManager implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Position position = mapPanel.getPositionFromCoordinates(e.getX(), e.getY());
			ActionsButtonsPanel actionsButtonsPanel = gameButtonsPanel.getActionsBoutonsPanel();
			if (actionsButtonsPanel.isWaitingFromPosition()) {
				fromPosition = position;
			}else if(actionsButtonsPanel.isWaitingTargetPosition()) {
				targetPosition = position;
			}
			
			/*we choose which buttons are displayed*/
			//first, we "reset" buttons display
			actionsButtonsPanel.setMajorButtonsVisibility(false);
			
			//we check if we are in our territory
			Box box = mapPanel.getBoxFromCoordinates(e.getX(), e.getY());
			if(box.getOwner() == player)
				actionsButtonsPanel.setCreationButtonsVisibility(true);
			
			//next, we check if any unit that player owns are on the box
			if(box.hasUnit()) {
				Units units = box.getUnit();
				if(units.getOwner() == player && !(units instanceof PhantomUnit))
					actionsButtonsPanel.setUnitsButtonsVisibility(true);
			}
			//and we check if there is any building 
			if(box instanceof GroundBox) {
				GroundBox groundBox = (GroundBox)box;
				if(groundBox.getOwner() == player && groundBox.hasBuilding()) {
					//finally, we check if building is not a phantom building
					Building building = groundBox.getBuilding();
					if(!(building instanceof PhantomBuilding))
						actionsButtonsPanel.setBuildingButtonsVisibility(true);
				}
			}
			
			
			mapPanel.refreshSelection(fromPosition, targetPosition);
			mapPanel.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}
	
	/**
	 * Reset selected Box position
	 */
	public void resetPositions() {
		fromPosition = null;
		targetPosition = null;
	}

	public MapPanel getMapPanel() {
		return mapPanel;
	}

	public ActionValidator getActionValidator() {
		return actionValidator;
	}

	public int getPlayersNumber() {
		return gameLoop.getPlayerNumber();
	}

	public Power[] getPowers() {
		return gameLoop.getPowers();
	}

	public Position getFromPosition() {
		return fromPosition;
	}

	public Power getPlayer() {
		return player;
	}

	public Position getTargetPosition() {
		return targetPosition;
	}
	
	/**
	 * Crée un fichier de sauvegarde de la partie
	 */
	public void save()  {
		File filesave = new File(GameConstants.SAVE_LOCATION);
		save = new SaveOption();
		try {
			save.save(filesave, map);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		removePanels();
	}

	public void removePanels() {
		remove(gameButtonsPanel);
		remove(mapPanel);
		remove(infosPanel);
		remove(playerResourcesPanel);
		window.changeWindow(MainWindow.MENU_WINDOW);
	}
	
	
}

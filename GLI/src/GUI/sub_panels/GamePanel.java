package GUI.sub_panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.game.ActionsButtonsPanel;
import GUI.components.game.GameButtonsPanel;
import GUI.components.game.InfosPanel;
import GUI.components.game.PlayerResourcesPanel;
import GUI.components.game.MapPanel;
import data.GameMap;
import data.Position;
import data.Power;
import data.actions.Action;
import data.resource.Resource;
import data.resource.ResourceTypes;
import data.boxes.*;
import data.building.Building;
import data.building.special.PhantomBuilding;
import data.unit.*;
import process.game.GameLoop;
import process.game.SaveOption;
import process.management.ActionValidator;

/**
 * <p>
 * The class that control the game window
 * </p>
 * <p>
 * Use GameInfoPanel & MainGamePanel
 * </p>
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
	private Power allpowers[];
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
		allpowers=powers;
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
	public void sauvegarder()  {
		File filesave = new File("game.ser");
		save = new SaveOption();
		
		try {
			save.sauvegarder(filesave, map, allpowers);
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
		window.changeWindow();
	}
}

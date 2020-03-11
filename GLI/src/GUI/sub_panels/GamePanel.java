package GUI.sub_panels;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.game.GameButtonsPanel;
import GUI.components.game.PlayerResourcesPanel;
import GUI.components.game.MainGamePanel;
import data.GameMap;
import data.Position;
import data.Power;
import data.actions.Action;
import data.resource.Resource;
import data.resource.ResourceTypes;
import data.boxes.*;
import data.unit.*;
import process.game.GameLoop;
import process.management.ActionValidator;


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
	
	private PlayerResourcesPanel gameInfoPanel;
	private MainGamePanel mainGamePanel;
	private GameButtonsPanel gameButtonsPanel;
	
	//attributes used for game logic
	private GameMap map;
	private Position fromPosition = new Position(0, 0);
	private Position targetPosition = new Position(0, 0);
	private ActionValidator actionValidator;
	private Power player;
	private GameLoop gameLoop;
	
	public GamePanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		gameButtonsPanel = new GameButtonsPanel(this);
		gameButtonsPanel.setPreferredSize(BUTTONS_DIMENSION);	
	}
	
	public void initGamePanel(GameMap map, Power powers[]) {
		gameLoop = new GameLoop(map, powers);
		
		int mapSize = map.getSize();
		int mapBoxWidth = (int) (MAIN_DIMENSION.getWidth() / mapSize);
		int mapBoxHeight = (int) (MAIN_DIMENSION.getHeight() / mapSize);
		actionValidator = new ActionValidator(map);
		this.mainGamePanel	= new MainGamePanel(map, powers, mapBoxWidth, mapBoxHeight);
		mainGamePanel.setPreferredSize(MAIN_DIMENSION);
		//mouseListener
		mainGamePanel.addMouseMotionListener(new MouseMotionManager());
		mainGamePanel.addMouseListener(new MouseInputManager());
		
		gameInfoPanel = new PlayerResourcesPanel(powers[0].getResources(), map.getBox(0,0));
		gameInfoPanel.setPreferredSize(INFO_DIMENSION);
		this.map=map;
		this.player=powers[0];
		add(gameInfoPanel);
		add(mainGamePanel);
		add(gameButtonsPanel);
	}
	
	public void addAction(Action action, int actionType) {
		gameLoop.addAction(actionType, action);
	}
	
	public void endPlayerTurn() {
		gameLoop.doActions();
		repaint();		
	}
	
	
	
	class MouseMotionManager implements MouseMotionListener{

		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			Box box = mainGamePanel.getBoxFromCoordinates(x, y);
			gameInfoPanel.getSelectionPanel().refresh(box);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}
	
	class MouseInputManager implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			Position position = mainGamePanel.getPositionFromCoordinates(e.getX(), e.getY());
			if (gameButtonsPanel.getActionsBoutonsPanel().isWaitingFromPosition()) {
				fromPosition = position;
			}else if(gameButtonsPanel.getActionsBoutonsPanel().isWaitingTargetPosition()) {
				targetPosition = position;
			}
			mainGamePanel.refreshSelection(fromPosition, targetPosition);
			mainGamePanel.repaint();
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		
	}
	public MainGamePanel getMainGamePanel() {
		return mainGamePanel ;
	}
	public ActionValidator getActionValidator() {
		return actionValidator;
	}
	public Position getPositionFrom() {
		return fromPosition;
	}
	public Power getPlayer() {
		return player;
	}
	public Position getPositiontarget() {
		return targetPosition;
	}
}

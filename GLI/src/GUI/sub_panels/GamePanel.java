package GUI.sub_panels;

import java.awt.BorderLayout;
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
import data.unit.*;
import process.game.GameLoop;
import process.management.ActionValidator;


/**
 * <p>The class that control the game window</p>
 * <p>Use GameInfoPanel & MainGamePanel</p>
 */
public class GamePanel extends JPanel{
	private static final long serialVersionUID = 7722109867943150729L;
	private MainWindow window;
	
	private final Dimension DIM_RESOURCES = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 30);
	private final Dimension DIM_MAP = new Dimension(4 * GuiPreferences.WIDTH / 5, 4 * GuiPreferences.HEIGHT / 5);
	private final Dimension DIM_INFOS = new Dimension(GuiPreferences.WIDTH / 5, 4 * GuiPreferences.HEIGHT / 5);
	private final Dimension DIM_BUTTONS= new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 10);
	
	private PlayerResourcesPanel playerResourcesPanel;
	private InfosPanel infosPanel;
	private MapPanel mapPanel;
	private GameButtonsPanel gameButtonsPanel;
	
	//attributes used for game logic
	private GameMap map;
	private Position fromPosition = null;
	private Position targetPosition = null;
	private ActionValidator actionValidator;
	private Power player;
	private GameLoop gameLoop;
	
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
		this.map=map;
		this.player=powers[0];
		
		gameLoop = new GameLoop(map, powers);
		
		actionValidator = new ActionValidator(map);
		this.mapPanel = new MapPanel(this, map, powers);
		mapPanel.setPreferredSize(DIM_MAP);
		
		infosPanel = new InfosPanel();
		infosPanel.initInfosPanel(this);
		infosPanel.setPreferredSize(DIM_INFOS);
		
		playerResourcesPanel = new PlayerResourcesPanel(powers[0].getResources());
		playerResourcesPanel.setPreferredSize(DIM_RESOURCES);
		
		//mouseListener
		mapPanel.addMouseMotionListener(new MouseMotionManager());
		mapPanel.addMouseListener(new MouseInputManager());
		

		add(playerResourcesPanel, BorderLayout.NORTH);
		add(mapPanel, BorderLayout.CENTER);
		add(infosPanel, BorderLayout.EAST);
		add(gameButtonsPanel, BorderLayout.SOUTH);
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
			Box box = mapPanel.getBoxFromCoordinates(x, y);
			infosPanel.refreshBoxHover(box);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}
	
	class MouseInputManager implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			Position position = mapPanel.getPositionFromCoordinates(e.getX(), e.getY());
			if (gameButtonsPanel.getActionsBoutonsPanel().isWaitingFromPosition()) {
				targetPosition=fromPosition;
				fromPosition = position;
			}else if(gameButtonsPanel.getActionsBoutonsPanel().isWaitingTargetPosition()) {
				targetPosition = position;
			}
			mapPanel.refreshSelection(fromPosition, targetPosition);
			mapPanel.repaint();
			
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
	public MapPanel getMapPanel() {
		return mapPanel ;
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

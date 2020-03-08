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
import GUI.components.game.GameInfoPanel;
import GUI.components.game.MainGamePanel;
import data.GameMap;
import data.Position;
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
	
	private GameInfoPanel gameInfoPanel;
	private MainGamePanel mainGamePanel;
	private GameButtonsPanel gameButtonsPanel = new GameButtonsPanel();
	
	//attributes used for game logic
	private GameMap map;
	private Position fromPosition = new Position();
	private Position targetPosition = new Position();
	
	
	public GamePanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		gameButtonsPanel.setPreferredSize(BUTTONS_DIMENSION);	
	}
	
	public void initGamePanel(GameMap map, Power powers[]) {
		int mapSize = map.getSize();
		int mapBoxWidth = (int) (MAIN_DIMENSION.getWidth() / mapSize);
		int mapBoxHeight = (int) (MAIN_DIMENSION.getHeight() / mapSize);
		
		this.mainGamePanel	= new MainGamePanel(map, powers, mapBoxWidth, mapBoxHeight);
		mainGamePanel.setPreferredSize(MAIN_DIMENSION);
		//mouseListener
		mainGamePanel.addMouseMotionListener(new MouseMotionManager());
		mainGamePanel.addMouseListener(new MouseInputManager());
		
		gameInfoPanel = new GameInfoPanel(powers[0].getResources(), map.getBox(0,0));
		gameInfoPanel.setPreferredSize(INFO_DIMENSION);
		add(gameInfoPanel);
		add(mainGamePanel);
		add(gameButtonsPanel);
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
			// TODO Auto-generated method stub
			
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
	
}

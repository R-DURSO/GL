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

public class GamePanel extends JPanel{
	private static final long serialVersionUID = 7722109867943150729L;
	private MainWindow context;
	
	private Power power;
	
	private final Dimension INFO_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / GuiPreferences.GAME_PANELS_RATIO_HEIGHT);
	private final Dimension MAIN_DIMENSION = 
			new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT * GuiPreferences.GAME_PANELS_SUBSTRACT_HEIGHT / GuiPreferences.GAME_PANELS_RATIO_HEIGHT);
	private final Dimension BUTTONS_DIMENSION = INFO_DIMENSION;
	
	private JPanel gameInfoPanel;
	private JPanel mainGamePanel;
	private JPanel gameButtonsPanel = new GameButtonsPanel();
	
	
	public GamePanel(MainWindow context) {
		this.context = context;
		initPower();
		
		gameInfoPanel = new GameInfoPanel(power.getResources());
		
		
		init();
	}

	/*
	 * Juste pour voir si le squelette est bon, pas une méthode finale
	 */
	@Deprecated
	private void initPower() {
		power = new Power("Joueur 1");
		
		power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(200);
		power.getResource(ResourceTypes.RESOURCE_STONE).addValue(50);
		power.getResource(ResourceTypes.RESOURCE_WOOD).addValue(100);
		power.getResource(ResourceTypes.RESOURCE_FOOD).addValue(400);
		
		
		power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_GOLD, 200);
		power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_STONE, 300);
		power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_WOOD, -100);
		power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, 2000);
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		gameInfoPanel.setPreferredSize(INFO_DIMENSION);
		gameButtonsPanel.setPreferredSize(BUTTONS_DIMENSION);
		add(gameInfoPanel);

		
	}
	public void initMainGamePanel(GameMap map, Power powers[]) {
		this.mainGamePanel	= new MainGamePanel(map, powers);
		mainGamePanel.setPreferredSize(MAIN_DIMENSION);
		add(mainGamePanel);
		add(gameButtonsPanel);
	}
}

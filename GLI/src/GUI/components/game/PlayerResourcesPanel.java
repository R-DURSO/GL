package GUI.components.game;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.boxes.Box;
import data.resource.Resource;
import data.resource.ResourceTypes;

/**
 * <p>Container of Resources information for the {@link GUI.sub_panels.GamePanel GamePanel}.</p>
 * <p>Use various {@link GUI.components.game.ResourcePanel ResourcePanel}</p>
 */
public class PlayerResourcesPanel extends JPanel{
	private static final long serialVersionUID = 1700840665124988595L;
	
	private ResourcePanel actionPointsPanel;
	private ResourcePanel goldPanel;
	private ResourcePanel stonePanel;
	private ResourcePanel woodPanel;
	private ResourcePanel foodPanel;

	public PlayerResourcesPanel(Resource resources[]) {
		actionPointsPanel = new ResourcePanel("Points d'action", resources[ResourceTypes.RESOURCE_ACTIONS - 1]);
		goldPanel = new ResourcePanel("Or", resources[ResourceTypes.RESOURCE_GOLD - 1]);
		stonePanel = new ResourcePanel("Pierre", resources[ResourceTypes.RESOURCE_STONE - 1]);
		woodPanel = new ResourcePanel("Bois", resources[ResourceTypes.RESOURCE_WOOD - 1]);
		foodPanel = new ResourcePanel("Nourriture", resources[ResourceTypes.RESOURCE_FOOD - 1]);
		init();
	}

	private void init() {
		setLayout(new GridLayout(0,5));
		add(actionPointsPanel);
		add(goldPanel);
		add(stonePanel);
		add(woodPanel);
		add(foodPanel);
	}
	
	/**
	 * Refresh all diplayed Resources
	 */
	public void refreshAll() {
		actionPointsPanel.refresh();
		goldPanel.refresh();
		stonePanel.refresh();
		woodPanel.refresh();
		foodPanel.refresh();
	}

}

package GUI.components.game;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.boxes.Box;
import data.resource.Resource;
import data.resource.ResourceTypes;

/**
 * <p>The container for information from the Game.</p>
 * <p>Use ResourcePanel and BoxSelectedPanel</p>
 */

public class GameInfoPanel extends JPanel{
	private static final long serialVersionUID = 1700840665124988595L;
	
	private BoxSelectedPanel boxInfosPanel;
	private ResourcePanel actionPointsPanel;
	private ResourcePanel goldPanel;
	private ResourcePanel stonePanel;
	private ResourcePanel woodPanel;
	private ResourcePanel foodPanel;

	public GameInfoPanel(Resource resources[], Box box) {
		actionPointsPanel = new ResourcePanel("Points d'action", resources[ResourceTypes.RESOURCE_ACTIONS - 1]);
		goldPanel = new ResourcePanel("Or", resources[ResourceTypes.RESOURCE_GOLD - 1]);
		stonePanel = new ResourcePanel("Pierre", resources[ResourceTypes.RESOURCE_STONE - 1]);
		woodPanel = new ResourcePanel("Bois", resources[ResourceTypes.RESOURCE_WOOD - 1]);
		foodPanel = new ResourcePanel("Nourriture", resources[ResourceTypes.RESOURCE_FOOD - 1]);
		boxInfosPanel = new BoxSelectedPanel(box);
		init();
	}

	private void init() {
		setLayout(new GridLayout(0,6));
		/**
		//temporaire
		positionPanel.setLayout(new GridLayout(0,1));
		positionPanel.add(new JLabel("Position de la case sélectionée"));
		*/
		add(boxInfosPanel);
		add(actionPointsPanel);
		add(goldPanel);
		add(stonePanel);
		add(woodPanel);
		add(foodPanel);
	}
	
	public void refreshAll() {
	}
	
	public BoxSelectedPanel getSelectionPanel() {
		return boxInfosPanel;
	}

}

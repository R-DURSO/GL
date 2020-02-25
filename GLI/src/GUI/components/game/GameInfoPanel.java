package GUI.components.game;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.resource.Resource;
import data.resource.ResourceCost;
import data.resource.ResourceTypes;

public class GameInfoPanel extends JPanel{
	private static final long serialVersionUID = 1700840665124988595L;
	private Resource resources[];
	
	private JPanel positionPanel = new JPanel();
	private ResourcePanel actionPointsPanel;
	private ResourcePanel goldPanel;
	private ResourcePanel stonePanel;
	private ResourcePanel woodPanel;
	private ResourcePanel foodPanel;

	public GameInfoPanel(Resource resources[]) {
		this.resources = resources;
		actionPointsPanel = new ResourcePanel("Points d'action", resources[ResourceTypes.RESOURCE_ACTIONS]);
		goldPanel = new ResourcePanel("Or", resources[ResourceTypes.RESOURCE_GOLD]);
		stonePanel = new ResourcePanel("Pierre", resources[ResourceTypes.RESOURCE_STONE]);
		woodPanel = new ResourcePanel("Bois", resources[ResourceTypes.RESOURCE_WOOD]);
		foodPanel = new ResourcePanel("Nourriture", resources[ResourceTypes.RESOURCE_FOOD]);
		init();
	}

	private void init() {
		setLayout(new GridLayout(0,6));
		
		//temporaire
		positionPanel.setLayout(new GridLayout(0,1));
		positionPanel.add(new JLabel("Position de la case sélectionée"));
		
		add(positionPanel);
		add(actionPointsPanel);
		add(goldPanel);
		add(stonePanel);
		add(woodPanel);
		add(foodPanel);
	}

}

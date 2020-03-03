package GUI.components.game;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.resource.Resource;

/**
 * <p>The list of Resource the power hold (and it's production)</p>
 */

public class ResourcePanel extends JPanel{
	private JLabel resourceNameLabel;
	private JLabel resourceAmountProdLabel;

	public ResourcePanel(String name, Resource resource) {
		resourceNameLabel = new JLabel(name + "%");
		resourceAmountProdLabel = new JLabel(resource.getAmount() + "(" + withSign(resource.getProductionPerTurn()) + ")", SwingConstants.CENTER);
		init();
	}

	private String withSign(int productionPerTurn) {
		return productionPerTurn < 0 ? "" + productionPerTurn : "+" + productionPerTurn;
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(resourceNameLabel);
		add(resourceAmountProdLabel);
	}

}

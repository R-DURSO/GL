package GUI.components.game;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.components.GuiPreferences;
import data.resource.Resource;

/**
 * <p>The list of Resource the power hold (and it's production)</p>
 * <p>Used in {@link PlayerResourcesPanel}
 */
public class ResourcePanel extends JPanel{
	private static final long serialVersionUID = -1991549825949046698L;
	private Resource resource;
	private String resourceName;
	private String resourceAmountAndProd;
	private JLabel resourceLabel;

	public ResourcePanel(String name, Resource resource) {
		resourceName = name;
		this.resource = resource;
		resourceLabel = new JLabel("", SwingConstants.CENTER);
		resourceLabel.setFont(GuiPreferences.BASE_FONT);
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		refresh();
		
		init();

	}
	
	/**
	 * Rewrite the amount of Resource owned and product each turn.
	 */
	public void refresh() {
		resourceAmountAndProd = resource.getAmount() + " (" + withSign(resource.getProductionPerTurn()) + ")";		
		resourceLabel.setText(resourceName + " : " + resourceAmountAndProd);
		repaint();
	}

	private String withSign(int productionPerTurn) {
		return productionPerTurn < 0 ? "" + productionPerTurn : "+" + productionPerTurn;
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(resourceLabel);
	}

}

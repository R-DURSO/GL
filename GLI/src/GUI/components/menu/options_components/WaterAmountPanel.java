package GUI.components.menu.options_components;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import data.GameConstants;

public class WaterAmountPanel extends JPanel{
	
	private JLabel waterLabel = new JLabel("Quantité d'eau : ", SwingConstants.CENTER);
	
	private JRadioButton lowWaterRadio = new JRadioButton("un peu");
	private JRadioButton midWaterRadio = new JRadioButton("moyen");
	private JRadioButton highWaterRadio = new JRadioButton("beaucoup");
	
	private ButtonGroup buttonGroup = new ButtonGroup();

	public WaterAmountPanel() {
		init();
	}

	private void init() {
		setLayout(new GridLayout(3, 1));
		
		lowWaterRadio.setSelected(true);
		buttonGroup.add(lowWaterRadio);
		buttonGroup.add(midWaterRadio);
		buttonGroup.add(highWaterRadio);
		
		add(new JPanel());
		add(waterLabel);
		
		JPanel container = new JPanel();
		
		container.add(lowWaterRadio);
		container.add(midWaterRadio);
		container.add(highWaterRadio);
		
		add(container);
		
	}
	
	/**
	 * Returns the water amount depending on the user choice (needed for creating the game)
	 * @return the water amount for the game
	 */
	public int getWaterAmount() {
		//check which button is selected
		if(highWaterRadio.isSelected())
			return GameConstants.WATER_AMOUNT_MANY;
		if(midWaterRadio.isSelected())
			return GameConstants.WATER_AMOUNT_NORMAL;
		
		//if problem (no one is selected --> can't happen but I prefer to be sure), the minimum amount is returned
		return GameConstants.WATER_AMOUNT_LITTLE;
	}
}

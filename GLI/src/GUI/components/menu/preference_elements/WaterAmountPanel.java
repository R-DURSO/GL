package GUI.components.menu.preference_elements;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

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

}

package GUI.components.game;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.components.GuiPreferences;
import data.boxes.*;
import data.building.Building;
import data.unit.PhantomUnit;


/**
 * <p>Represent the Selected Box from the map, and display it.</p>
 * @author Maxence
 */

public class BoxSelectedPanel extends JPanel {
	private JLabel titleLabel = new JLabel("<html><u>Informations de la case survolée</u></html>");
	private JLabel ownerLabel;
	private JLabel buildingLabel;
	private JLabel unitsLabel;
	private JLabel resourceLabel;

	public BoxSelectedPanel(Box box) {
		init();
		initFont();
		refresh(box);
	}
	
	private void initFont() {
		for(Component component : this.getComponents()) {
			if (component instanceof JLabel) {
				((JLabel)component).setFont(GuiPreferences.BASE_FONT);
			}
		}
		titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		
		titleLabel.setFont(GuiPreferences.ITALIC_FONT);
	}

	private void init() {
		setLayout(new GridLayout(0,1));
		ownerLabel = new JLabel();
		unitsLabel = new JLabel();
		buildingLabel = new JLabel();
		resourceLabel = new JLabel();
		
		add(titleLabel);
		add(ownerLabel);
		add(unitsLabel);
		add(buildingLabel);
		add(resourceLabel);
	}
	
	public void refresh(Box box) {
		if (box.hasOwner()) {
			ownerLabel.setText(box.getOwner().toString());
		}
		else {
			ownerLabel.setText("Aucun propriétaire");
		}
		
		if (box.hasUnit() && !(box.getUnit() instanceof PhantomUnit)) {
			//permits to break line automatically
			unitsLabel.setText("<html>" + box.getUnit().toString() +"</html>");
		}
		else {
			unitsLabel.setText("Pas d'unité");
		}
		
		if (box instanceof GroundBox) {
			GroundBox GBox = (GroundBox)box;
			if (GBox.hasBuilding()) {
				Building building = GBox.getBuilding();
				if (!building.isFinish())
					buildingLabel.setText(building.toString() + " (" + building.getBuildTime() + " tour(s) avant activation)");
				else
					buildingLabel.setText(building.toString());
			}
			else {
				buildingLabel.setText("Pas de bâtiment");
			}
			resourceLabel.setText("Ressource: " + GBox.getResourceTypeName());
		}
		else {
			buildingLabel.setText("Case d'eau");
			resourceLabel.setText("___");
		}
	}

}

package GUI.components.game;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.boxes.*;
import data.building.Building;
import data.unit.PhantomUnit;


/**
 * <p>Represent the Selected Box from the map, and display it.</p>
 * @author Maxence
 */

public class BoxSelectedPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel OwnerLabel;
	private JLabel BuildingLabel;
	private JLabel UnitsLabel;
	private JLabel ResourceLabel;

	public BoxSelectedPanel(Box box) {
		init();
		refresh(box);
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		OwnerLabel = new JLabel();
		UnitsLabel = new JLabel();
		BuildingLabel = new JLabel();
		ResourceLabel = new JLabel();
		
		add(OwnerLabel);
		add(UnitsLabel);
		add(BuildingLabel);
		add(ResourceLabel);
	}
	
	public void refresh(Box box) {
		if (box.hasOwner()) {
			OwnerLabel.setText(box.getOwner().toString());
		}
		else {
			OwnerLabel.setText("Aucun propriétaire");
		}
		
		if (box.hasUnit() && !(box.getUnit() instanceof PhantomUnit)) {
			UnitsLabel.setText(box.getUnit().toString());
		}
		else {
			UnitsLabel.setText("Pas d'unité");
		}
		
		if (box instanceof GroundBox) {
			GroundBox GBox = (GroundBox)box;
			if (GBox.hasBuilding()) {
				Building building = GBox.getBuilding();
				if (!building.isFinish())
					BuildingLabel.setText(building.toString() + " (" + building.getBuildTime() + " tour(s) avant activation)");
				else
					BuildingLabel.setText(building.toString());
			}
			else {
				BuildingLabel.setText("Pas de bâtiment");
			}
			ResourceLabel.setText("Ressource: " + GBox.getResourceTypeName());
		}
		else {
			BuildingLabel.setText("Case d'eau");
			ResourceLabel.setText("___");
		}
	}

}

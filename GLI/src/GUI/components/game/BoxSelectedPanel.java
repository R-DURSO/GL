package GUI.components.game;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.boxes.*;


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
		
		if (box.hasOwner()) {
			OwnerLabel = new JLabel(box.getOwner().toString());
		}
		else {
			OwnerLabel = new JLabel("Aucun propriétaire");
		}
		
		if (box.hasUnit()) {
			UnitsLabel = new JLabel(box.getUnit().toString());
		}
		else {
			UnitsLabel = new JLabel();
		}
		
		if (box instanceof GroundBox) {
			GroundBox GBox = (GroundBox)box;
			if (GBox.hasBuilding()) {
				BuildingLabel = new JLabel(GBox.getBuilding().toString());
			}
			else {
				BuildingLabel = new JLabel();
			}
			ResourceLabel = new JLabel("Ressource: " + GBox.getResourceTypeName());
		}
		else {
			BuildingLabel = new JLabel();
			ResourceLabel = new JLabel();
		}
		
		init();
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(OwnerLabel);
		add(UnitsLabel);
		add(BuildingLabel);
		add(ResourceLabel);
	}

}

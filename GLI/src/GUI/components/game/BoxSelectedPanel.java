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
		init();
		refresh(box);
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		OwnerLabel = new JLabel();
		add(OwnerLabel);
		UnitsLabel = new JLabel();
		add(UnitsLabel);
		BuildingLabel = new JLabel();
		add(BuildingLabel);
		ResourceLabel = new JLabel();
		add(ResourceLabel);
	}
	
	public void refresh(Box box) {
		if (box.hasOwner()) {
			OwnerLabel.setText(box.getOwner().toString());
		}
		else {
			OwnerLabel.setText("Aucun propriétaire");
		}
		
		if (box.hasUnit()) {
			UnitsLabel.setText(box.getUnit().toString());
		}
		else {
			UnitsLabel.setText("");
		}
		
		if (box instanceof GroundBox) {
			GroundBox GBox = (GroundBox)box;
			if (GBox.hasBuilding()) {
				BuildingLabel.setText(GBox.getBuilding().toString());
			}
			else {
				BuildingLabel.setText("");
			}
			ResourceLabel.setText("Ressource: " + GBox.getResourceTypeName());
		}
		else {
			BuildingLabel.setText("");
			ResourceLabel.setText("");
		}
	}

}

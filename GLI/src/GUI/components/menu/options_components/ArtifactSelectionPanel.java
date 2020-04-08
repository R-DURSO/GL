package GUI.components.menu.options_components;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;


public class ArtifactSelectionPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JCheckBox checkArtifact = new JCheckBox("Apparition de l'Artéfact : ");

	public ArtifactSelectionPanel() {
		init();
	}

	private void init() {
		setLayout(new GridLayout(1, 1));
		
		checkArtifact.setSelected(true);
		add(checkArtifact);
	}
	
	/**
	 * @return if the spawning of the Artifact is enable
	 */
	public boolean getSelection() {
		if (checkArtifact.isSelected()) {
			return true;
		}
		else {
			return false;
		}
	}
	
}

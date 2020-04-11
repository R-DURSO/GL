package GUI.components.menu.options_components;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ArtifactSelectionPanel extends JPanel {
	private JLabel artifactLabel = new JLabel("Apparition de l'Artéfact : ");
	private JCheckBox checkArtifact = new JCheckBox();

	public ArtifactSelectionPanel() {
		init();
	}

	private void init() {
		checkArtifact.setSelected(true);
		add(artifactLabel);
		add(checkArtifact);
	}
	
	/**
	 * @return if the spawning of the Artifact is enable
	 */
	public boolean getArtifactSelection() {
		return checkArtifact.isSelected();
	}
	
}

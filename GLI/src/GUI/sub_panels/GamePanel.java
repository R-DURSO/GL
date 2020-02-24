package GUI.sub_panels;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	private JLabel testLabel = new JLabel("jeu");

	public GamePanel() {
		init();
	}

	private void init() {
		add(testLabel);
	}

}

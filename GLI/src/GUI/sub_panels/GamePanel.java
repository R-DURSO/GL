package GUI.sub_panels;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.MainWindow;

public class GamePanel extends JPanel{
	private JLabel testLabel = new JLabel("jeu");
	private JFrame context;
	
	public GamePanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		add(testLabel);
	}

}

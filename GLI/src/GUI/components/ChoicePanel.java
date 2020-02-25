package GUI.components;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ChoicePanel extends JPanel{
	private static final long serialVersionUID = -5496095243931073915L;
	private JButton testButton = new JButton("choix");

	public ChoicePanel() {
		setLayout(new BorderLayout());
		add(testButton);
	}

}

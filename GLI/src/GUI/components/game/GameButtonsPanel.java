package GUI.components.game;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GameButtonsPanel extends JPanel{
	private static final long serialVersionUID = -3154461334874408763L;
	private JPanel ActionPanel =  new chooseActionBoutonsPanel();
	private JButton endTurnButton = new JButton("Fin du tour");
	private JButton tempButton = new JButton("Liste des actions");
	private JButton quitButton = new JButton("Quitter");

	public GameButtonsPanel() {
		setLayout(new GridLayout(0,3));
		add(endTurnButton);
		add(quitButton);
		add(ActionPanel);
			}

}

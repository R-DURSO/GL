package GUI.components.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import GUI.sub_panels.GamePanel;

public class GameButtonsPanel extends JPanel{
	private static final long serialVersionUID = -3154461334874408763L;
	private ActionsButtonsPanel actionsPanel;
//	private JPanel controlPanel = new JPanel(); //Contains endturnButton and tempButton
	private JButton endTurnButton = new JButton("Fin du tour");
	private JButton quitButton = new JButton("Quitter");

	public GameButtonsPanel(GamePanel context) {
		actionsPanel = new ActionsButtonsPanel(context); 
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 2;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(actionsPanel, gbc);
		gbc.weightx = 0.5;
		
		add(endTurnButton, gbc);
		add(quitButton, gbc);
	}

	public ActionsButtonsPanel getActionsBoutonsPanel() {
		return actionsPanel;
	}

}

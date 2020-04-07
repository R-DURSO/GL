package GUI.components.menu.preference_elements;

import java.awt.GridLayout;

import javax.swing.JPanel;

import GUI.components.menu.OptionsPanel;

public class PlayersTabPanel extends JPanel{
	
	private PlayerPanel player1Panel = new PlayerPanel("Joueur 1", false);
	private PlayerPanel player2Panel = new PlayerPanel("Joueur 2", true);
	private PlayerPanel player3Panel = new PlayerPanel("Joueur 3", true);
	private PlayerPanel player4Panel = new PlayerPanel("Joueur 4", true);
	
	private OptionsPanel context;

	public PlayersTabPanel(OptionsPanel context) {
		this.context = context;
		setLayout(new GridLayout(2, 2));
		add(player1Panel);
		add(player2Panel);
		add(player3Panel);
		add(player4Panel);
	}
	
	public void setPlayerVisibility(int playerIndex, boolean visible) {
		PlayerPanel playerPanel = (PlayerPanel) this.getComponent(playerIndex - 1);
		playerPanel.setVisible(visible);
	}

}

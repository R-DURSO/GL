package GUI.components.menu.options_components;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import GUI.components.menu.OptionsPanel;
import data.GameConstants;
import log.LoggerUtility;

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
	
	public String getPlayerName(int playerIndex) {
		PlayerPanel playerPanel = (PlayerPanel) this.getComponent(playerIndex - 1);
		return playerPanel.getName();
	}
	
	public int getPlayerAILevel(int playerIndex) {
		PlayerPanel playerPanel = (PlayerPanel) this.getComponent(playerIndex - 1);
		return playerPanel.getAILevel();
	}

}

package GUI.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PreferencesPanel extends JPanel{
	private static final long serialVersionUID = -714238221735387964L;
	
	private final int MIN_MAP_SIZE = 15;
	private final int MAX_MAP_SIZE = 30;
	
	//number players
	private JPanel numberPlayersPanel = new JPanel();
	private JLabel numberPlayersLabel = new JLabel("Nombre de joueurs");
	private final int numberPlayersList[] = {2, 3, 4};
	
	//water amount
	private JPanel waterAmountPanel = new JPanel();
	private JLabel waterAmountLabel = new JLabel("Quantité d'eau");
	private final String waterAmountList[] = {"peu", "moyen", "beaucoup"};
	//map size
	private JPanel mapSizePanel = new JPanel();
	private JLabel mapSizeLabel = new JLabel("Taille de la carte");
	
	//ai levels
	private JPanel aiPanel = new JPanel();
	private JLabel aiLabel = new JLabel("Intelligence des autres joueurs");
	private final String aiLevelsList[] = {"stupide", "normal", "stratège"};
	
	public PreferencesPanel() {
		init();
		
		
	}
	
	private void init() {
		setLayout(new GridLayout(2, 2, 8, 8));
	}

	public int getNumberPlayers() {
		return 4;
	}
	
	public int getWaterAmount() {
		return 20;
	}
	
	public int getMapSize() {
		return 20;
	}
	
	public int getAi1Level() {
		return 1;
	}
	
	public int getAi2Level() {
		return 1;
	}
	
	public int getAi3Level() {
		return 1;
	}

}

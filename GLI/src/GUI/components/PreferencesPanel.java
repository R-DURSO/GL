package GUI.components;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class PreferencesPanel extends JPanel{
	private static final long serialVersionUID = -714238221735387964L;
	
	private final int MIN_MAP_SIZE = 15;
	private final int INIT_MAP_SIZE = 20;
	private final int MAX_MAP_SIZE = 50;
	
	//number players
	private JPanel numberPlayersPanel = new JPanel();
	private JLabel numberPlayersLabel = new JLabel("Nombre de joueurs");
	private final Integer numberPlayersList[] = {2, 3, 4};
	private JComboBox<Integer> numberPlayerComboBox = new JComboBox<>(numberPlayersList);
	
	//water amount
	private JPanel waterAmountPanel = new JPanel();
	private JLabel waterAmountLabel = new JLabel("Quantité d'eau : ");
	private final String waterAmountList[] = {"peu", "moyen", "beaucoup"};
	private JComboBox<String> waterAmountComboBox = new JComboBox<>(waterAmountList);
	
	//map size
	private JPanel mapSizePanel = new JPanel();
	private JLabel mapSizeLabel = new JLabel("Taille de la carte");
	private JLabel mapSizeNumberLabel = new JLabel("" + INIT_MAP_SIZE);
	private JSlider mapSizeSlider = new JSlider(JSlider.HORIZONTAL, MIN_MAP_SIZE, MAX_MAP_SIZE, INIT_MAP_SIZE); 
	
	//ai levels
	private JPanel aiPanel = new JPanel();
	private final String aiLevelsList[] = {"stupide", "normal", "stratège"};
	private JPanel ai1Panel = new JPanel();
	private JPanel ai2Panel = new JPanel();
	private JPanel ai3Panel = new JPanel();
	private JLabel ai1Label = new JLabel("intelligence IA 1");
	private JLabel ai2Label = new JLabel("intelligence IA 2");
	private JLabel ai3Label = new JLabel("intelligence IA 3");
	private JComboBox<String> ai1ComboBox = new JComboBox<>(aiLevelsList);
	private JComboBox<String> ai2ComboBox = new JComboBox<>(aiLevelsList);
	private JComboBox<String> ai3ComboBox = new JComboBox<>(aiLevelsList);
	
	public PreferencesPanel() {
		init();
	}
	
	private void init() {
		setLayout(new GridLayout(0, 2, 5, 5));
		setFont(GuiPreferences.BASE_FONT);

		initNumberPlayersPanel();
		initAiPanel();
		initMapSizePanel();
		initWaterAmountPanel();
		
		
		add(numberPlayersPanel);
		add(aiPanel);
		add(mapSizePanel);
		add(waterAmountPanel);
		
		
	}

	private void initNumberPlayersPanel() {
		numberPlayersPanel.setLayout(new GridLayout(0,1));
		numberPlayersPanel.add(numberPlayersLabel);
		numberPlayersPanel.add(numberPlayerComboBox);
	}

	private void initWaterAmountPanel() {
		waterAmountPanel.setLayout(new GridLayout(0,1));
		waterAmountPanel.add(waterAmountLabel);
		waterAmountPanel.add(waterAmountComboBox);
	}

	private void initMapSizePanel() {
		mapSizePanel.setLayout(new GridLayout(0,1));
		mapSizePanel.add(mapSizeLabel);
		mapSizePanel.add(mapSizeNumberLabel);
		mapSizePanel.add(mapSizeSlider);
	}

	private void initAiPanel() {
		aiPanel.setLayout(new GridLayout(0,3));
		
		ai1Panel.setLayout(new GridLayout(0,1));
		ai1Panel.add(ai1Label);
		ai1Panel.add(ai1ComboBox);
		
		ai2Panel.setLayout(new GridLayout(0,1));
		ai2Panel.add(ai2Label);
		ai2Panel.add(ai2ComboBox);
		
		ai3Panel.setLayout(new GridLayout(0,1));
		ai3Panel.add(ai3Label);
		ai3Panel.add(ai3ComboBox);
		
		aiPanel.add(ai1Panel);
		aiPanel.add(ai2Panel);
		aiPanel.add(ai3Panel);
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

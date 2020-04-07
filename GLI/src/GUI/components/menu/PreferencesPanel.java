package GUI.components.menu;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.components.GuiPreferences;
import GUI.components.SliderPanel;
import data.GameConstants;

public class PreferencesPanel extends JPanel{
	private static final long serialVersionUID = -714238221735387964L;
	
	private final int MIN_MAP_SIZE = 10;
	private final int INIT_MAP_SIZE = 12;
	private final int MAX_MAP_SIZE = 15;
	
	//number players
	private JPanel numberPlayersPanel = new JPanel();
	private JLabel numberPlayersLabel = new JLabel("Nombre de joueurs", SwingConstants.CENTER);
	private final Integer numberPlayersList[] = {2, 3, 4};
	private JComboBox<Integer> numberPlayerComboBox = new JComboBox<>(numberPlayersList);
	
	//water amount
	private JLabel waterAmountLabel = new JLabel("Quantité d'eau : ", SwingConstants.CENTER);
	private JPanel waterAmountPanel = new JPanel();
	private final String waterAmountList[] = {"un peu", "moyen", "beaucoup"};
	private JComboBox<String> waterAmountComboBox = new JComboBox<>(waterAmountList);
	
	//map size
	private SliderPanel mapSizePanel = new SliderPanel("Taille de la carte", MAX_MAP_SIZE, MIN_MAP_SIZE, INIT_MAP_SIZE);
	
	//ai levels
	private JPanel aiPanel = new JPanel();
	private final String aiLevelsList[] = {"stupide", "normal", "stratège"};
	private JPanel ai1Panel = new JPanel();
	private JPanel ai2Panel = new JPanel();
	private JPanel ai3Panel = new JPanel();
	private JLabel ai1Label = new JLabel("IA 1", SwingConstants.CENTER);
	private JLabel ai2Label = new JLabel("IA 2", SwingConstants.CENTER);
	private JLabel ai3Label = new JLabel("IA 3", SwingConstants.CENTER);
	private JComboBox<String> ai1ComboBox = new JComboBox<>(aiLevelsList);
	private JComboBox<String> ai2ComboBox = new JComboBox<>(aiLevelsList);
	private JComboBox<String> ai3ComboBox = new JComboBox<>(aiLevelsList);
	
	public PreferencesPanel() {
		init();
	}

	private void init() {
		int paddingWidth = GuiPreferences.WIDTH / 20;
		int paddingHeight = GuiPreferences.HEIGHT / 20;
		setLayout(new GridLayout(0, 2, paddingWidth, paddingHeight));
		setBorder(BorderFactory.createEmptyBorder(paddingHeight, paddingWidth, paddingHeight, paddingWidth));
		setFont(GuiPreferences.BASE_FONT);

		initNumberPlayersPanel();
		initAiPanel();
		initWaterAmountPanel();
		
		
		add(numberPlayersPanel);
		add(aiPanel);
		add(mapSizePanel);
		add(waterAmountPanel);
		
		
	}

	private void initNumberPlayersPanel() {
		numberPlayersPanel.setLayout(new GridLayout(0,1));
		numberPlayersPanel.add(numberPlayersLabel);
		numberPlayerComboBox.setSelectedIndex(2);
		numberPlayersPanel.add(numberPlayerComboBox);
	}

	private void initWaterAmountPanel() {
		waterAmountPanel.setLayout(new GridLayout(0,1));
		waterAmountPanel.add(waterAmountLabel);
		waterAmountComboBox.setSelectedIndex(0);
		waterAmountPanel.add(waterAmountComboBox);
	}

	private void initAiPanel() {
		aiPanel.setLayout(new GridLayout(0,3));
		
		ai1Panel.setLayout(new GridLayout(0,1));
		ai1Panel.add(ai1Label);
		ai1ComboBox.setSelectedIndex(0);
		ai1Panel.add(ai1ComboBox);
		
		ai2Panel.setLayout(new GridLayout(0,1));
		ai2Panel.add(ai2Label);
		ai2ComboBox.setSelectedIndex(0);
		ai2Panel.add(ai2ComboBox);
		
		ai3Panel.setLayout(new GridLayout(0,1));
		ai3Panel.add(ai3Label);
		ai3ComboBox.setSelectedIndex(0);
		ai3Panel.add(ai3ComboBox);
		
		aiPanel.add(ai1Panel);
		aiPanel.add(ai2Panel);
		aiPanel.add(ai3Panel);
	}

	public int getNumberPlayers() {
		return numberPlayerComboBox.getSelectedIndex() + 2;
	}
	
	public int getWaterAmount() {
		switch (waterAmountComboBox.getSelectedIndex()) {
		case 0: //"peu"
			return GameConstants.WATER_AMOUNT_LITTLE;
		case 1: //"moyen"
			return GameConstants.WATER_AMOUNT_NORMAL;
		case 2: //"beaucoup"
			return GameConstants.WATER_AMOUNT_MANY;
		default:
			return GameConstants.WATER_AMOUNT_LITTLE; 
		}
		
		//return (waterAmountComboBox.getSelectedIndex() + 1) * 20;
	}
	
	public int getMapSize() {
		return mapSizePanel.getValue();
	}
	
	public int getAi1Level() {
		return ai1ComboBox.getSelectedIndex() + 1;
	}
	
	public int getAi2Level() {
		return ai2ComboBox.getSelectedIndex() + 1;
	}
	
	public int getAi3Level() {
		return ai3ComboBox.getSelectedIndex() + 1;
	}

}

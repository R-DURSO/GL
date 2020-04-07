package GUI.components.menu.preference_elements;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PlayerPanel extends JPanel{
	
	private JLabel titleLabel;
	private JLabel nameLabel = new JLabel("Nom : ");
	private JTextField nameField;
	private JLabel aiLabel;
	
	private final String aiLevels[] = {"stupide", "normal", "stratège"};
	private JComboBox<String> aiComboBox = new JComboBox<>(aiLevels);

	public PlayerPanel(String title, boolean isAI) {
		titleLabel = new JLabel("<html><u>" + title + "</u></html>", SwingConstants.CENTER);
		//init common to all player panels
		initGeneral(title);
		
		//init specific to player panels (changes if player is human or not) 
		initSpecific(isAI);
	}

	private void initGeneral(String title) {
		setLayout(new GridLayout(3, 1));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel namePanel = new JPanel();
		//we define generic name for players
		nameField = new JTextField(title, 20);
		nameField.setDocument(new DocumentFieldLimit(15));
		nameField.setText(title);
		
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		//add all labels and panels in right order
		this.add(titleLabel);
		this.add(namePanel);
	}
	
	private void initSpecific(boolean isAI) {
		JPanel specificPanel = new JPanel();
		if(isAI) {
			aiLabel = new JLabel("Intelligence : ");
			aiComboBox.setSelectedIndex(1);
			specificPanel.add(aiLabel);
			specificPanel.add(aiComboBox);
		}else {
			aiLabel = new JLabel("Joueur Humain", SwingConstants.CENTER);
			specificPanel.add(aiLabel);
		}
		
		this.add(specificPanel);
	}
	
	

}

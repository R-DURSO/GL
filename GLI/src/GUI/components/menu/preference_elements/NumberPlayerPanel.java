package GUI.components.menu.preference_elements;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.components.GuiPreferences;
import GUI.components.menu.OptionsPanel;

/**
 * Panel wich contains number palyer display and behavior
 * @author Aldric Vitali Silvestre
 *
 */
public class NumberPlayerPanel extends JPanel{
	private static final long serialVersionUID = 6745932739570392477L;

	private JLabel nbPlayersLabel = new JLabel("Nombre de joueurs : ", SwingConstants.CENTER);
	
	private final String choices[] = {"2", "3", "4"};
	private JComboBox<String> nbPlayersComboBox = new JComboBox<>(choices);
	
	private OptionsPanel context;
	
	private int playerNumber;
	
	public NumberPlayerPanel(OptionsPanel context) {
		this.context = context;
		init();
		
	}

	private void init() {
		setLayout(new GridLayout(4,1));
		nbPlayersLabel.setFont(GuiPreferences.BASE_FONT);
		nbPlayersComboBox.setFont(GuiPreferences.BASE_FONT);
		nbPlayersComboBox.setSelectedIndex(2);
		playerNumber = 4;
		nbPlayersComboBox.addActionListener(new ActionChanged());
		
		add(new JPanel());
		add(nbPlayersLabel);
		add(nbPlayersComboBox);
	}
	
	public int getNumberPlayers() {
		return playerNumber;
	}
	
	class ActionChanged implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			playerNumber = nbPlayersComboBox.getSelectedIndex() + 2;
			
			//change playerTabPanel display depending on player number
			
		}
	}
}

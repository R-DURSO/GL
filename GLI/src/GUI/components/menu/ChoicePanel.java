package GUI.components.menu;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.MainWindow;


public class ChoicePanel extends JPanel{
	private static final long serialVersionUID = -5496095243931073915L;
	private MainWindow context;
	
	private JButton newGameButton = new JButton("Nouvelle partie");
	private JButton loadGameButton = new JButton("Charger une partie");

	public ChoicePanel(MainWindow context) {
		init();
		this.context = context;
	}

	private void init() {
		setLayout(new GridLayout(1, 3));
		
		newGameButton.addActionListener(new ActionNewGame());
		loadGameButton.addActionListener(new ActionLoadGame());
		
		add(newGameButton);
		add(new JPanel()); //empty panel for letting space between buttons
		add(loadGameButton);
	}
	
	class ActionNewGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(choiceBox("Voulez-vous vraiment lancer une nouvelle partie?\nLa partie pr�c�dente sera �cras�e."))
				context.loadGame();
		}
	}
	
	class ActionLoadGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(choiceBox("Voulez-vous vraiment charger la derni�re partie?"))
				context.loadGame();
		}
	}
	
	private boolean choiceBox(String question) {
		int answer = JOptionPane.showConfirmDialog(context, question, "Confirmation", JOptionPane.YES_NO_OPTION);
		return answer == JOptionPane.YES_OPTION;
	}

}

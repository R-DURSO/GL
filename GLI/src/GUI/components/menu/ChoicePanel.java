package GUI.components.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.sub_panels.MenuPanel;


public class ChoicePanel extends JPanel{
	private MainWindow context;
	
	private JButton newGameButton = new JButton("Nouvelle partie");
	private JButton loadGameButton = new JButton("Charger la dernière partie");
	private JButton quitButton = new JButton("Quitter la partie");
	
	private final int PANEL_WIDTH = (int)MenuPanel.CHOICE_DIMENSION.getWidth();
	private final int PANEL_HEIGHT = (int)MenuPanel.CHOICE_DIMENSION.getHeight();
	private final Dimension DIM_BUTTON = new Dimension(PANEL_WIDTH / 4, PANEL_HEIGHT / 2);
	

	public ChoicePanel(MainWindow context) {
		init();
		this.context = context;
	}

	private void init() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, PANEL_WIDTH / 100, PANEL_HEIGHT / 3));
		
		newGameButton.addActionListener(new ActionNewGame());
		newGameButton.setPreferredSize(DIM_BUTTON);
		
		loadGameButton.addActionListener(new ActionLoadGame());
		loadGameButton.setPreferredSize(DIM_BUTTON);
		
		quitButton.addActionListener(new ActionQuit());
		quitButton.setPreferredSize(DIM_BUTTON);
		
		add(newGameButton);
		add(loadGameButton);
		add(quitButton);
	}
	
	class ActionNewGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(choiceBox("Voulez-vous vraiment lancer une nouvelle partie?"))
				context.newGame();
		}
	}
	
	class ActionLoadGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(choiceBox("Voulez-vous vraiment charger la dernière partie?"))
				context.loadGame();
		}
	}
	
	class ActionQuit implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(choiceBox("Voulez-vous vraiment quitter le jeu?"))
				System.exit(0);
		}
	}
	
	private boolean choiceBox(String question) {
		int answer = JOptionPane.showConfirmDialog(context, question, "Confirmation", JOptionPane.YES_NO_OPTION);
		return answer == JOptionPane.YES_OPTION;
	}

}

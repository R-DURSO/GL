package GUI.components.game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.components.GuiPreferences;
import GUI.sub_panels.GamePanel;

public class GameButtonsPanel extends JPanel{
	private static final long serialVersionUID = -3154461334874408763L;
	private GamePanel context;
	
	private JPanel changingPanel = new JPanel();
	private CardLayout cardLayout = new CardLayout();
	
	private ActionsButtonsPanel actionsPanel;
	private ValidationPanel validationPanel;
	
	private JPanel contextPanel = new JPanel();
	private JButton endTurnButton = new JButton("Fin du tour");
	private JButton quitButton = new JButton("Quitter");
	
	private final Dimension LEFT_DIMENSION = new Dimension(4 * GuiPreferences.WIDTH / 5, GuiPreferences.HEIGHT / 10);
	private final Dimension RIGHT_DIMENSION = new Dimension(GuiPreferences.WIDTH / 5, GuiPreferences.HEIGHT / 10);

	public GameButtonsPanel(GamePanel context) {
		
		this.context = context;
		setLayout(new BorderLayout());

		changingPanel.setLayout(cardLayout);
		
		validationPanel = new ValidationPanel(context, this);
		actionsPanel = new ActionsButtonsPanel(context, this); 
		changingPanel.add(actionsPanel, "actions");
		changingPanel.add(validationPanel, "validation");
		changingPanel.setPreferredSize(LEFT_DIMENSION);
		add(changingPanel, BorderLayout.CENTER);
		
		contextPanel.setLayout(new GridLayout(0,1));
		endTurnButton.addActionListener(new ActionEndTurn());
		quitButton.addActionListener(new ActionQuit());
		contextPanel.add(endTurnButton);
		contextPanel.add(quitButton);
		contextPanel.setPreferredSize(RIGHT_DIMENSION);
		
		add(contextPanel, BorderLayout.EAST);
	}
	
	public void changeMiddlePanel() {
		cardLayout.next(changingPanel);
		//check if validation panel is inmiddle panel, in order to disable endTurnButton (errors can occur)
		if (validationPanel.isVisible()) {
			endTurnButton.setVisible(false);
		}else {
			endTurnButton.setVisible(true);
		}
	}

	public ActionsButtonsPanel getActionsBoutonsPanel() {
		return actionsPanel;
	}
	
	public void hideActionButtons() {
		actionsPanel.setMajorButtonsVisibility(false);
	}
	
	public void setValidationActionType(int actionType) {
		validationPanel.setActionType(actionType);
	}
	
	class ActionEndTurn implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int answer = JOptionPane.showConfirmDialog(context, "Voulez-vous vraiment terminer votre tour?", "Fin de tour", JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION)
				context.endPlayerTurn();
		}
	}
	
	class ActionQuit implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//c'est deja la commande pour quitter?
			int answer = JOptionPane.showConfirmDialog(null, "Voulez-vous voulez vous sauvegarder la partie",
					"Fin du jeux ", JOptionPane.YES_NO_OPTION);
			if(answer==JOptionPane.YES_OPTION) {
				context.sauvegarder();
				
			}else if(answer==JOptionPane.NO_OPTION) {
				
			}
		}
	}
}

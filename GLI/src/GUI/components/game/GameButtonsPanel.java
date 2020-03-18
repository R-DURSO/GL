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
	
	private final Dimension LEFT_DIMENSION = new Dimension(3 * GuiPreferences.WIDTH / 4, GuiPreferences.HEIGHT / 10);
	private final Dimension RIGHT_DIMENSION = new Dimension(GuiPreferences.WIDTH / 4, GuiPreferences.HEIGHT / 10);

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
		contextPanel.add(endTurnButton);
		contextPanel.add(quitButton);
		contextPanel.setPreferredSize(RIGHT_DIMENSION);
		
		add(contextPanel, BorderLayout.EAST);
	}
	
	public void changeMiddlePanel() {
		cardLayout.next(changingPanel);
	}

	public ActionsButtonsPanel getActionsBoutonsPanel() {
		return actionsPanel;
	}
	
	public void setValidationActionType(int actionType) {
		validationPanel.setActionType(actionType);
	}
	
	class ActionEndTurn implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			context.endPlayerTurn();
		}
	}

}

package GUI.components.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.components.GuiPreferences;
import GUI.drawing.ColorData;
import GUI.sub_panels.GamePanel;
import data.Position;
import data.Power;
import data.actions.Action;
import data.actions.ActionTypes;

public class ValidationPanel extends JPanel{
	
	private JButton validateButton = new JButton("Valider");
	private JButton cancelButton = new JButton("Annuler");
	private GameButtonsPanel gameButtonsPanel;
	private GamePanel gamePanel;
	private int actionType;
	
	private final int PANEL_WIDTH = (int) GamePanel.DIM_BUTTONS.getWidth();
	private final int PANEL_HEIGHT = (int) GamePanel.DIM_BUTTONS.getHeight();
	private final Dimension DIM_BUTTON = new Dimension(PANEL_WIDTH / 3, PANEL_HEIGHT / 2);

	public ValidationPanel(GamePanel gamePanel, GameButtonsPanel gameButtonsPanel) {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, PANEL_HEIGHT / 3));
		this.gamePanel = gamePanel;
		this.gameButtonsPanel = gameButtonsPanel;
		
		validateButton.addActionListener(new ActionValidate());
		cancelButton.addActionListener(new ActionCancel());
		
		initButtons();
		
		add(validateButton);
		add(cancelButton);
	}
	
	private void initButtons() {
		//init colors
		validateButton.setBackground(ColorData.BUTTON_CREATE_COLOR);
		cancelButton.setBackground(ColorData.BUTTON_DESTROY_COLOR);
		
		validateButton.setFont(GuiPreferences.BASE_FONT);
		cancelButton.setFont(GuiPreferences.BASE_FONT);
		
		//init size (same for both)
		validateButton.setPreferredSize(DIM_BUTTON);
		cancelButton.setPreferredSize(DIM_BUTTON);
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	class ActionValidate implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			proceedDependingOnActionType();
		}
	}
	
	private void proceedDependingOnActionType() {
		//only 2 actions needs to have a 2nd mouse button press, so we treat both actions separately
		//first, we check if target position is set
		boolean hasFinished = false;
		if (gamePanel.getTargetPosition() != null) {
			switch (actionType) {
			case ActionTypes.ACTION_ATTACK:
				hasFinished = doActionAttack();
				break;
			case ActionTypes.ACTION_MOVE:
				hasFinished = doActionMove();
				break;
			default:
				gamePanel.cancelAction();
				break;
			}
			//we return on the action buttons "state", if no error occurs
			if(hasFinished)
				gameButtonsPanel.changeMiddlePanel();
		}else {
			JOptionPane.showMessageDialog(null, "Choisissez une case à cibler avant.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	private boolean doActionMove() {
		int result;
		result = JOptionPane.showConfirmDialog(null, "Voulez-vous déplacer vos troupes?");
		if (result == JOptionPane.YES_OPTION) {
			try {
				Power power = gamePanel.getPlayer();
				Position targetPosition = gamePanel.getTargetPosition();
				Position fromPosition = gamePanel.getFromPosition();
				Action action = gamePanel.getActionValidator().createActionMove(power, fromPosition, targetPosition);
				gamePanel.addAction(action, ActionTypes.ACTION_MOVE);
				return true;
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				return false;
			}
		}
		return false;
		
	}

	private boolean doActionAttack() {
		int result = 0;
		result = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment attaquer?");
		if (result == 0) {
			try {
				Power power = gamePanel.getPlayer();
				Position targetPosition = gamePanel.getTargetPosition();
				Position fromPosition = gamePanel.getFromPosition();
				Action action = gamePanel.getActionValidator().createActionAttack(power, fromPosition, targetPosition);
				gamePanel.addAction(action, ActionTypes.ACTION_ATTACK);
				return true;
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				return false;
			}
		}
		return false;
	}

	class ActionCancel implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			gamePanel.cancelAction();
		}
	}
	
//	
}

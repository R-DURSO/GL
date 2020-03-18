package GUI.components.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

	public ValidationPanel(GamePanel gamePanel, GameButtonsPanel gameButtonsPanel) {
		this.gamePanel = gamePanel;
		this.gameButtonsPanel = gameButtonsPanel;
		validateButton.addActionListener(new ActionValidate());
		cancelButton.addActionListener(new ActionCancel());
		
		add(validateButton);
		add(cancelButton);
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
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				return false;
			}
		}
		return true;
		
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
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				return false;
			}
		}
		return true;
	}

	class ActionCancel implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			gamePanel.cancelAction();
		}
	}
	
//	
}

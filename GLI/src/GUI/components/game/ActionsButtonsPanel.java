package GUI.components.game;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import GUI.components.GuiPreferences;
import GUI.components.SliderPanel;
import GUI.drawing.ColorData;
import GUI.sub_panels.GamePanel;
import data.Position;
import data.Power;
import data.actions.Action;
import data.actions.ActionTypes;
import data.building.army.*;
import data.building.product.*;
import data.building.special.*;
import data.unit.*;
import process.management.UnitManager;

/**
 * 
 * @author rdurs this class is use for implements the button of gameButtonPanel
 *
 */
public class ActionsButtonsPanel extends JPanel {

	private JButton actionBreakAllianceButton = new JButton("Briser une alliance");
	private JButton actionMakeAllianceButton = new JButton("Cr�er une alliance");
	private JButton createActionAttackButton = new JButton("Lancer une attaque");
	private JButton createActionMoveButton = new JButton("D�placer les troupes");
	private JButton createActionConstructButton = new JButton("Construire un batiment");
	private JButton createActionCreateUnitButton = new JButton("Cr�er de nouvelles troupes");
	private JButton createActionDestroyUnitButton = new JButton("D�truire des unit�s");
	private JButton createActionDestroyBuildingtButton = new JButton("D�truire un batiment");
	private JButton createUpgradeCapitalButton = new JButton("Am�liorer la capitale");

	/*
	 * State system : decide what to do whan user click on map, either modify
	 * fromPosition or targetPosition (in GamePanel)
	 * exs:	--> if no action button clicked, user click on map will change fromPosition (state WAITING_FROM_POSITION)
	 * 		--> if action button clicked, and if needed (depending on action type), state will be change, so the user click on map will change targetPosition (state WAITING_TARGET_POSITION)
	 * 		--> when action button is "finished", state must return to WAITING_FROM_POSITION
	 */
	private final int STATE_WAITING_FROM_POSTION = 0;
	private final int STATE_WAITING_TARGET_POSTION = 1;
	private int state = STATE_WAITING_FROM_POSTION;

	private GamePanel context;
	private GameButtonsPanel gameButtonsPanel;
	private ActionsButtonsPanel panel = this;
	private Action action;

	public ActionsButtonsPanel(GamePanel context, GameButtonsPanel gameButtonsPanel) {
		this.context = context;
		this.gameButtonsPanel = gameButtonsPanel;
		setLayout(new GridLayout(0, 3));

		//add Action listeners
		actionBreakAllianceButton.addActionListener(new ActionListenerBreakAlliance());
		actionMakeAllianceButton.addActionListener(new ActionListenerMakeAlliance());
		createActionAttackButton.addActionListener(new ActionListenerAttack());
		createActionMoveButton.addActionListener(new ActionListenerMoveUnits());
		createActionDestroyUnitButton.addActionListener(new ActionListenerDestroyUnits());
		createActionDestroyBuildingtButton.addActionListener(new ActionListenerDestroyBuilding());
		createActionCreateUnitButton.addActionListener(new ActionListenerCreateUnits());
		createActionConstructButton.addActionListener(new ActionListenerConstruct());
		createUpgradeCapitalButton.addActionListener(new ActionListenerUpgradeCapital());


		//change Button's color
		actionBreakAllianceButton.setBackground(ColorData.BUTTON_ALLIANCE_COLOR);
		actionMakeAllianceButton.setBackground(ColorData.BUTTON_ALLIANCE_COLOR);

		createActionAttackButton.setBackground(ColorData.BUTTON_MOVE_COLOR);
		createActionMoveButton.setBackground(ColorData.BUTTON_MOVE_COLOR);

		createActionDestroyUnitButton.setBackground(ColorData.BUTTON_DESTROY_COLOR);
		createActionDestroyBuildingtButton.setBackground(ColorData.BUTTON_DESTROY_COLOR);

		createActionCreateUnitButton.setBackground(ColorData.BUTTON_CREATE_COLOR);
		createActionConstructButton.setBackground(ColorData.BUTTON_CREATE_COLOR);

		createUpgradeCapitalButton.setBackground(ColorData.BUTTON_CAPITAL_COLOR);

		//add Buttons to Panel
		add(createUpgradeCapitalButton);
		add(createActionMoveButton);
		add(createActionAttackButton);
		add(actionMakeAllianceButton);
		add(createActionConstructButton);
		add(createActionDestroyBuildingtButton);
		add(actionBreakAllianceButton);
		add(createActionCreateUnitButton);
		add(createActionDestroyUnitButton);


		//change buttons Font
		for (Component comp : getComponents()) {
		    if (comp instanceof JButton) {
		        ((JButton)comp).setFont(GuiPreferences.BASE_FONT);
		    }
		}

		setMajorButtonsVisibility(false);
	}

	public void setMajorButtonsVisibility(boolean visibility) {
		createActionMoveButton.setVisible(visibility);
		createActionConstructButton.setVisible(visibility);
		createActionCreateUnitButton.setVisible(visibility);
		createActionAttackButton.setVisible(visibility);
		createActionDestroyBuildingtButton.setVisible(visibility);
		createActionDestroyUnitButton.setVisible(visibility);
	}

	public void setUnitsButtonsVisibility(boolean visibility) {
		createActionAttackButton.setVisible(visibility);
		createActionMoveButton.setVisible(visibility);
		createActionDestroyUnitButton.setVisible(visibility);
	}

	public void setBuildingButtonsVisibility(boolean visibility) {
		createActionDestroyBuildingtButton.setVisible(visibility);
		createActionCreateUnitButton.setVisible(visibility);
	}

	public void setCreationButtonsVisibility(boolean visibility) {
		createActionConstructButton.setVisible(visibility);
	}


	public int getState() {
		return state;
	}

	public void setStateWaitingFromPosition() {

		state = STATE_WAITING_FROM_POSTION;
	}

	public void setStateWaitingTargetPosition() {
		state = STATE_WAITING_TARGET_POSTION;
	}

	public boolean isWaitingFromPosition() {
		return state == STATE_WAITING_FROM_POSTION;
	}

	public boolean isWaitingTargetPosition() {
		return state == STATE_WAITING_TARGET_POSTION;
	}

	class ActionListenerBreakAlliance implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Power power = context.getPlayer();
			if (!power.isAllied()) {
				JOptionPane.showMessageDialog(null, "Vous n'avez pas d'alli�", "Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				int answer = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment mettre fin � votre alliance?",
						"Fin d'alliance?", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						action = context.getActionValidator().createActionBreakAlliance(power, power.getAlly());
						context.addAction(action, ActionTypes.ACTION_BREAK_ALLIANCE);
					}
					catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			}
		}
	}

	class ActionListenerMakeAlliance implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Power power = context.getPlayer();
			if(power.isAllied())
				JOptionPane.showMessageDialog(null, "Vous avez d�j� un alli�", "Erreur", JOptionPane.ERROR_MESSAGE);
			else {
				//get player number
				String choices[] = new String[context.getPlayersNumber() - 1];
				Power[] powers = context.getPowers();
				for(int i = 1; i < powers.length; i++){
					choices[i - 1] = powers[i].getName();
				}
				JComboBox<String> powersComboBox = new JComboBox<>(choices);
				int answer = JOptionPane.showConfirmDialog(null, powersComboBox,
						"Avec quelle puissance voulez-vous vous allier?", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						int selectedIndex = powersComboBox.getSelectedIndex();
						action = context.getActionValidator().createActionMakeAlliance(power, powers[selectedIndex+1]);
						context.addAction(action, ActionTypes.ACTION_MAKE_ALLIANCE);
					}
					catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			}
		}

	}

	class ActionListenerUpgradeCapital implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = 0;
			result = JOptionPane.showConfirmDialog(null, "Voulez-vous am�liorer votre capitale?");
			if (result == 0) {
				try {
					action = context.getActionValidator().createActionUpgradeCapital(context.getPlayer());
					context.addAction(action, ActionTypes.ACTION_UPGRADE_CAPITAL);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		}
	}

	class ActionListenerConstruct implements ActionListener {
		String[] choices = { "Caserne (" + Barrack.COST + " bois)", "Atelier ("+Workshop.COST+" pierre)", "Port ("+Dock.COST+" bois)",
				"Mine d'Or ("+Mine.COST+" bois)", "Scierie ("+Sawmill.COST+" bois)", "Moulin ("+Windmill.COST+" bois)", "cari�re ("+Quarry.COST+" bois)",
				"Porte ("+Door.COST+" pierre)","Mur ("+Wall.COST+" pierre)", "Temple ("+Temple.COST+" pierre)" };

		public void actionPerformed(ActionEvent e) {
			JComboBox<String> buildingComboBox = new JComboBox<>(choices);
			int answer = JOptionPane.showConfirmDialog(null, buildingComboBox, "constructions possibles",
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				try {
					action = context.getActionValidator().createActionConstruct(context.getPlayer(),
							buildingComboBox.getSelectedIndex() + 1, context.getFromPosition());
//					gameLoop.addAction(ActionTypes.ACTION_CONSTRUCT, action);
					context.addAction(action, ActionTypes.ACTION_CONSTRUCT);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		}

	}

	// now, this action is done in ValidationPanel, to ensure double click
	class ActionListenerMoveUnits implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// User will have a change in the GUI, which causes to avoid to set up multiple
			// actions, and wait his second selection to be done
			JOptionPane.showMessageDialog(null, "Choisissez une cible");
			gameButtonsPanel.changeMiddlePanel();
			gameButtonsPanel.setValidationActionType(ActionTypes.ACTION_MOVE);
			setStateWaitingTargetPosition();
		}
	}

	class ActionListenerAttack implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Choisissez une cible � attaquer");
			gameButtonsPanel.changeMiddlePanel();
			gameButtonsPanel.setValidationActionType(ActionTypes.ACTION_ATTACK);
			setStateWaitingTargetPosition();
		}
	}

	class ActionListenerCreateUnits implements ActionListener {
		String[] choices = { "Infantrie ("+Infantry.COST+" Or, "+Infantry.COST_PER_TURN+" Nourriture/tour)", "Archer ("+Archer.COST+" Or, "+Archer.COST_PER_TURN+" Nourriture/tour)",
							"Cavalier ("+Cavalry.COST+" Or, "+Cavalry.COST_PER_TURN+" Nourriture/tour)", "Piquier ("+Pikeman.COST+" Or, "+Pikeman.COST_PER_TURN+" Nourriture/tour)",
							"B�lier ("+BatteringRam.COST+" Or, "+BatteringRam.COST_PER_TURN+" Nourriture/tour)",
							"Tr�buchet ("+Trebuchet.COST+" Or, "+Trebuchet.COST_PER_TURN+" Nourriture/tour)", "Bateau ("+Boat.COST+" Or, "+Boat.COST_PER_TURN+" Nourriture/tour)" };
		
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> unitComboBox = new JComboBox<>(choices);
			int answer = JOptionPane.showConfirmDialog(null, unitComboBox, "Quelles unit�s voulez-vous ?",
					JOptionPane.YES_NO_OPTION);
			// we check if user has canceled his choice
			if (answer == JOptionPane.YES_OPTION) {
				int unitType = unitComboBox.getSelectedIndex()+1;
				int maxUnit = UnitManager.getInstance().maxNumberUnit(unitType);
				SliderPanel sliderNumber = new SliderPanel("Choisissez combien d'unit�s souhaitez-vous entrainer ?", maxUnit, 1, 1);
				answer = JOptionPane.showConfirmDialog(null, sliderNumber, "Nombre d'unit�",
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						Power power = context.getPlayer();
						int numberUnits = sliderNumber.getValue();
						Position fromPosition = context.getFromPosition();
						action = context.getActionValidator().createActionCreateUnit(power, unitType, numberUnits,
								fromPosition);
						context.addAction(action, ActionTypes.ACTION_CREATE_UNITS);
					} catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			}
		}
	}

	class ActionListenerDestroyBuilding implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = 0;
			result = JOptionPane.showConfirmDialog(null, "Voulez-vous d�truire ce batiment?");
			if (result == 0) {
				try {
					action = context.getActionValidator().createActionDestroyBuilding(context.getPlayer(),
							context.getFromPosition());
					context.addAction(action, ActionTypes.ACTION_DESTROY_BUILDING);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}

		}
	}

	class ActionListenerDestroyUnits implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = 0;
			result = JOptionPane.showConfirmDialog(null, "Voulez-vous d�truire cette unit�?");
			if (result == 0) {
				try {
					action = context.getActionValidator().createActionDestroyUnits(context.getPlayer(),
							context.getFromPosition());
					context.addAction(action, ActionTypes.ACTION_DESTROY_UNITS);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}

		}

	}
	
}
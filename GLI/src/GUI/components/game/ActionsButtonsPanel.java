package GUI.components.game;

import data.unit.Archer;
import data.unit.Units;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import GUI.components.SliderPanel;
import GUI.sub_panels.GamePanel;
import data.Position;
import data.Power;
import data.actions.Action;
import data.actions.ActionBreakAlliance;
import data.actions.ActionConstruct;
import data.actions.ActionTypes;
import data.boxes.GroundBox;
import data.building.BuildingTypes;
import process.management.ActionValidator;
import process.management.BuildingManager;
import process.management.UnitManager;

/**
 * 
 * @author rdurs this class is use for implements the button of gameButtonPanel
 *
 */
public class ActionsButtonsPanel extends JPanel {

	private JButton actionBreakAllianceButton = new JButton("Briser une alliance");
	private JButton actionMakeAllianceButton = new JButton("Créer une alliance");
	private JButton createActionAttackButton = new JButton("Lancer une attaque");
	private JButton createActionMoveButton = new JButton("Déplacer les troupes");
	private JButton createActionConstructButton = new JButton("Construire un batiment");
	private JButton createActionCreateUnitButton = new JButton("Créer de nouvelles troupes");
	private JButton createActionDestroyUnitButton = new JButton("Détruire des unités");
	private JButton createActionDestroyBuildingtButton = new JButton("Détruire un batiment");
	private JButton createUdapteCapitalButton = new JButton("Améliorer la capitale");

	/*
	 * State system : decide what to do whan user click on map, either modify
	 * fromPosition or targetPosition (in GamePanel) exs: --> if no action button
	 * clicked, user click on map will change fromPosition (state
	 * WAITING_FROM_POSITION) --> if action button clicked, and if needed (depending
	 * on action type), state will be change, so the user click on map will change
	 * targetPosition (state WAITING_TARGET_POSITION) -->when action button if
	 * "finished", state must return to WAITING_FROM_POSITION
	 */
	private final int STATE_WAITING_FROM_POSTION = 0;
	private final int STATE_WAITING_TARGET_POSTION = 1;
	private int state = STATE_WAITING_FROM_POSTION;

	// allows to get Positions, which are in GamePanel
	// TODO : peut-être qu'il y aura une position par défaut, du coup plus besoin de
	// ça...
	private GamePanel context;
	private MapPanel game;
	private ActionsButtonsPanel panel = this;
	private Action action;

	public ActionsButtonsPanel(GamePanel context) {
		this.context = context;
		this.game = context.getMapPanel();
		setLayout(new GridLayout(0, 3));

		actionBreakAllianceButton.addActionListener(new ActionBreakAlliance());
		add(actionBreakAllianceButton);

		actionMakeAllianceButton.addActionListener(new ActionMakeAlliance());
		add(actionMakeAllianceButton);

		createActionAttackButton.addActionListener(new ActionListenerAttack());
		add(createActionAttackButton);

		createActionMoveButton.addActionListener(new ActionListenerMoveUnits());
		add(createActionMoveButton);

		createActionDestroyUnitButton.addActionListener(new ActionListenerDestroyUnits());
		add(createActionDestroyUnitButton);

		createActionDestroyBuildingtButton.addActionListener(new ActionListenerDestroyBuilding());
		add(createActionDestroyBuildingtButton);

		createActionCreateUnitButton.addActionListener(new ActionListenerCreateUnits());
		add(createActionCreateUnitButton);

		createActionConstructButton.addActionListener(new ActionListenerConstrcut());
		add(createActionConstructButton);
		createUdapteCapitalButton.addActionListener(new ActionListenerUdapteCapital());
		add(createUdapteCapitalButton);

	}

	public int getState() {
		return state;
	}

	public void setStateWaitingFromPosition() { // TODO change to private ?? (pas besoin peut-être de l'utiliser à
												// l'extérieur
		state = STATE_WAITING_FROM_POSTION;
	}

	public void setStateWaitingTargetPosition() { // TODO change to private ??
		state = STATE_WAITING_TARGET_POSTION;
	}

	public boolean isWaitingFromPosition() {
		return state == STATE_WAITING_FROM_POSTION;
	}

	public boolean isWaitingTargetPosition() {
		return state == STATE_WAITING_TARGET_POSTION;
	}

	class ActionBreakAlliance implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment détruire cette alliance?",
					"Fin d'alliance ?", JOptionPane.YES_NO_OPTION);
		}
	}

	class ActionMakeAlliance implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

		}

	}

	class ActionListenerConstrcut implements ActionListener {
		String[] choices = { "caserne (100 bois)", "écurie", "port", "mine", "scierie", "moulin", "carière", "porte",
				"mur", "temple" };

		public void actionPerformed(ActionEvent e) {
			JComboBox<String> buildingComboBox = new JComboBox<>(choices);
			JOptionPane.showMessageDialog(null, buildingComboBox, "constructions possibles", 1);
			// TODO vérifier si l'utilisateur a cliqué sur la croix pour fermer la fenetre
			// System.out.println(context.getfromPosition().getX());
			try {
				action = context.getActionValidator().createActionConstruct(context.getPlayer(),
						buildingComboBox.getSelectedIndex() + 1, context.getPositionFrom());
//				gameLoop.addAction(ActionTypes.ACTION_CONSTRUCT, action);
				context.addAction(action, ActionTypes.ACTION_CONSTRUCT);
			} catch (IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			} catch (NullPointerException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage());
			}

		}

	}

	
	//NE FONCTIONNE PAS, PAS MOYEN DE CHOISIR LA TARGET POUR L'INSTANT (à venir tkt pas)
	class ActionListenerMoveUnits implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// first, we want to wait user to select where he wants to move his units
			int result;
			result = JOptionPane.showConfirmDialog(null, "Voulez-vous déplacer vos troupes?");
			if (result == JOptionPane.YES_OPTION) {
				try {
					Power power = context.getPlayer();
					Position targetPosition = context.getPositiontarget();
					Position fromPosition = context.getPositionFrom();
					action = context.getActionValidator().createActionMove(power, fromPosition, targetPosition);
					context.addAction(action, ActionTypes.ACTION_MOVE);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		}
	}

	class ActionListenerCreateUnits implements ActionListener {
		String[] choices = { "Infantrie", "Archer", "Cavalier", "Piquier", "Char", "Trébuchet", "Bateau" };
		int maxUnit = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> unitComboBox = new JComboBox<>(choices);
			int answer = JOptionPane.showConfirmDialog(null, unitComboBox, "Quelle(s) unité(s) voulez-vous?",
					JOptionPane.YES_NO_CANCEL_OPTION);
			// we check if user has canceled his choice
			if (answer == JOptionPane.YES_OPTION) {
				int unitType = unitComboBox.getSelectedIndex();
				maxUnit = UnitManager.getInstance().maxNumberUnit(unitType);
				SliderPanel sliderNumber = new SliderPanel("Nombre d'unités", maxUnit, 1, 1);
				answer = JOptionPane.showConfirmDialog(null, sliderNumber, "Combien d'unité(s) voulez-vous",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						Power power = context.getPlayer();
						int numberUnits = sliderNumber.getValue();
						Position fromPosition = context.getPositionFrom();
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
			result = JOptionPane.showConfirmDialog(null, "voulez vous détruire ce batiment");
			if (result == 0) {
				try {
					action = context.getActionValidator().createActionDestroyBuilding(context.getPlayer(),
							context.getPositionFrom());
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
			result = JOptionPane.showConfirmDialog(null, "voulez vous détruire ces unité ");
			if (result == 0) {
				try {
					action = context.getActionValidator().createActionDestroyUnits(context.getPlayer(),
							context.getPositionFrom());
					context.addAction(action, ActionTypes.ACTION_DESTROY_UNITS);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}

		}

	}

	class ActionListenerUdapteCapital implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = 0;
			result = JOptionPane.showConfirmDialog(null, "voulez vous améliorer votre capital");
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

	class ActionListenerAttack implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = 0;
			result = JOptionPane.showConfirmDialog(null, "voulez vous améliorer votre capital");
			if (result == 0) {
				try {
					action = context.getActionValidator().createActionAttack(context.getPlayer(),
							context.getPositiontarget(), context.getPositionFrom());
					context.addAction(action, ActionTypes.ACTION_ATTACK);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		}

	}
	
	class MouseTargetPosition implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}

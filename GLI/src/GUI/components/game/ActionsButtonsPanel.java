package GUI.components.game;
import data.unit.Archer;
import data.unit.Units;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
 * @author rdurs
 * this class is use for implements the button of gameButtonPanel
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
	private JButton createUdapteCapitalButton = new JButton("Am�liorer la capitale");
	
	/*State system : 
	 *decide what to do whan user click on map, either modify fromPosition or targetPosition (in GamePanel)
	 *	 exs: 
	 *	--> if no action button clicked, user click on map will change fromPosition (state WAITING_FROM_POSITION)
	 *	--> if action button clicked, and if needed (depending on action type), state will
	 * be change, so the user click on map will change targetPosition (state WAITING_TARGET_POSITION)
	 * 	-->when action button if "finished", state must return to WAITING_FROM_POSITION
	 */
	private final int STATE_WAITING_FROM_POSTION = 0;
	private final int STATE_WAITING_TARGET_POSTION = 1;
	private int state = STATE_WAITING_FROM_POSTION;
	
	//allows to get Positions, which are in GamePanel 
	// TODO : peut-�tre qu'il y aura une position par d�faut, du coup plus besoin de �a...
	private GamePanel context;
	private MapPanel game;
	private Action action;

	public ActionsButtonsPanel(GamePanel context) {
		this.context = context;
		this.game=context.getMapPanel();
		setLayout(new GridLayout(0, 3));
		
		actionBreakAllianceButton.addActionListener(new ActionBreakAlliance());
		add(actionBreakAllianceButton);
		
		actionMakeAllianceButton.addActionListener(new ActionMakeAlliance());
		add(actionMakeAllianceButton);
		
		createActionAttackButton.addActionListener(new ActionBreakAlliance());
		add(createActionAttackButton);
		
		createActionMoveButton.addActionListener(new ActionListenerMoveUnits());
		add(createActionMoveButton);
		
		createActionDestroyUnitButton.addActionListener(new ActionBreakAlliance());
		add(createActionDestroyUnitButton);
		
		createActionDestroyBuildingtButton.addActionListener(new ActionBreakAlliance());
		add(createActionDestroyBuildingtButton);
		
		createActionCreateUnitButton.addActionListener(new ActionListenerCreateUnits());
		add(createActionCreateUnitButton);
		
		createActionConstructButton.addActionListener(new ActionListenerConstrcut());
		add(createActionConstructButton);
		
		add(createUdapteCapitalButton);
		
		 
	}
	
	public int getState() {
		return state;
	}
	
	public void setStateWaitingFromPosition() { // TODO change to private ?? (pas besoin peut-�tre de l'utiliser � l'ext�rieur
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
	
	class ActionBreakAlliance implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment d�truire cette alliance?","Fin d'alliance ?", JOptionPane.YES_NO_OPTION);
		}
	}
	
	class ActionMakeAlliance implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Archer test =  new Archer(10);
			game.getBoxByPosition(context.getPositionFrom()).setUnit(test);
			
		}
		
	}
	class ActionListenerConstrcut implements ActionListener{
		String[] choices = { "caserne (100 bois)","�curie","port",  "mine","scierie","moulin","cari�re","porte", "mur","temple"};
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> buildingComboBox = new JComboBox<>(choices);
			JOptionPane.showMessageDialog(null, buildingComboBox, "constructions possibles", 1);
			//TODO v�rifier si l'utilisateur a cliqu� sur la croix pour fermer la fenetre
			//System.out.println(context.getfromPosition().getX());
			try {
				action = context.getActionValidator().createActionConstruct(context.getPlayer() ,buildingComboBox.getSelectedIndex()+1 ,context.getPositionFrom());
//				gameLoop.addAction(ActionTypes.ACTION_CONSTRUCT, action);
				context.addAction(action, ActionTypes.ACTION_CONSTRUCT);
			}catch( IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null,e1.getMessage());
			}catch(NullPointerException e2) {
				JOptionPane.showMessageDialog(null,e2.getMessage());
			}

		
				
		}
		
	}
	class ActionListenerMoveUnits implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showConfirmDialog(null, "voulez vous d�placer vos trouppe");
			try {
				action = context.getActionValidator().createActionMove(context.getPlayer(), context.getPositiontarget(), context.getPositionFrom());
			}catch(IllegalArgumentException e1){
				JOptionPane.showMessageDialog(null,e1.getMessage());
			}
		
	
		}
	}
	class ActionListenerCreateUnits implements ActionListener{
		String[] choices = {"aucune unit�", "infantrie ","archer","cavalier",  "piquier","char","tr�buchet","bateau"};
		int maxUnit=0;
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> unitComboBox = new JComboBox<>(choices);
			JOptionPane.showMessageDialog(null, unitComboBox, "quel unit� voulais vous", 1);
			
				if(unitComboBox.getSelectedIndex()>0) {
					maxUnit = UnitManager.getInstance().maxNumberUnit(unitComboBox.getSelectedIndex()-1);
					JSlider number = new JSlider(1,maxUnit);
					number.setMajorTickSpacing (10);
					number.setMinorTickSpacing (1);
					number.setPaintTicks (true);
					number.setPaintLabels (true);
					JOptionPane.showMessageDialog(null, number, "combien d'unit� voulais vous", 1);
					try {
						action = context.getActionValidator().createActionCreateUnit(context.getPlayer(), unitComboBox.getSelectedIndex()-1, number.getValue(), context.getPositionFrom());
						context.addAction(action, ActionTypes.ACTION_CREATE_UNITS);
					}catch(IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null,e1.getMessage());
					}
				}
		}
		
	}

	
}

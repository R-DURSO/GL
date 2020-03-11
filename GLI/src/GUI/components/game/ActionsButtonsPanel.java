package GUI.components.game;

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
/**
 * 
 * @author rdurs
 * this class is use for implements the button of gameButtonPanel
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
	// TODO : peut-être qu'il y aura une position par défaut, du coup plus besoin de ça...
	private GamePanel context;
	private MainGamePanel game;
	private Action action;

	public ActionsButtonsPanel(GamePanel context) {
		this.context = context;
		this.game=context.getMainGamePanel();
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
		
		createActionCreateUnitButton.addActionListener(new ActionBreakAlliance());
		add(createActionCreateUnitButton);
		
		createActionConstructButton.addActionListener(new ActionListenerConstrcut());
		add(createActionConstructButton);
		
		add(createUdapteCapitalButton);
		
		 
	}
	
	public int getState() {
		return state;
	}
	
	public void setStateWaitingFromPosition() { // TODO change to private ?? (pas besoin peut-être de l'utiliser à l'extérieur
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
			int answer = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment détruire cette alliance?","Fin d'alliance ?", JOptionPane.YES_NO_OPTION);
		}
	}
	
	class ActionMakeAlliance implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment créer une alliance?","Création d'alliance ?", JOptionPane.YES_NO_OPTION);
			
		}
		
	}
	class ActionListenerConstrcut implements ActionListener{
		String[] choices = { "caserne (100 bois)","écurie","port",  "mine","scierie","moulin","carière","porte", "mur","temple"};
		public void actionPerformed(ActionEvent e) {
			JComboBox building = new JComboBox(choices);
			JOptionPane.showMessageDialog(null, building, "constructions possibles", 1);
			//System.out.println(context.getfromPosition().getX());
			try {
				
				action = context.getActionValidator().createActionConstruct(context.getPlayer() ,building.getSelectedIndex()+1 ,context.getPositionFrom());
//				gameLoop.addAction(ActionTypes.ACTION_CONSTRUCT, action);
				context.addAction(action, ActionTypes.ACTION_CONSTRUCT);
			}catch( IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null,e1.getMessage());
			}catch(NullPointerException e2) {
				e2.printStackTrace();
			}
			building.getSelectedIndex();
			//System.out.println(building.getSelectedIndex());
		
				
		}
		
	}
	class ActionListenerMoveUnits implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(context.getPositionFrom().getY());
			System.out.println(context.getPositionFrom().getX());
			System.out.println(context.getPositiontarget().getX());
			System.out.println();
			
		}
		
	
	}
}

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
import data.actions.ActionBreakAlliance;

public class chooseActionBoutonsPanel extends JPanel {
	private JButton actionBreakAllianceButton = new JButton("casser une alliance ");
	private JButton actionMakeAllianceButton = new JButton("cr�er une alliance ");
	private JButton createActionAttackButton = new JButton("lancer une attaque");
	private JButton createActionMoveButton = new JButton("d�placer les trouppes");
	private JButton createActionConstructButton = new JButton("contruire un batiment ");
	private JButton createActionCreateUnitButton = new JButton("cr�er de nouvelle troupe");
	private JButton createActionDestroyUnitButton = new JButton("d�truire les unit�");
	private JButton createActionDestroyBuildingtButton = new JButton("destruit le batiment");
	private JButton createUdapteCapitalButton = new JButton("am�lioration de la capital");
	private Dimension  a = new Dimension(30,100);
	
	public chooseActionBoutonsPanel() {
		setLayout(new GridLayout(5, 0));
		
		actionBreakAllianceButton.addActionListener(new ActionBreakAlliance());
		add(actionBreakAllianceButton);
		
		actionMakeAllianceButton.addActionListener(new ActionMakeAlliance());
		add(actionMakeAllianceButton);
		
		createActionAttackButton.addActionListener(new ActionBreakAlliance());
		add(createActionAttackButton);
		
		createActionMoveButton.addActionListener(new ActionBreakAlliance());
		add(createActionMoveButton);
		
		createActionDestroyUnitButton.addActionListener(new ActionBreakAlliance());
		add(createActionDestroyUnitButton);
		
		createActionDestroyBuildingtButton.addActionListener(new ActionBreakAlliance());
		add(createActionDestroyBuildingtButton);
		
		createActionCreateUnitButton.addActionListener(new ActionBreakAlliance());
		add(createActionCreateUnitButton);
		
		createActionConstructButton.addActionListener(new ActionContrcut());
		add(createActionConstructButton);
		
		add(createUdapteCapitalButton);
		
		 
	}
	
	class ActionBreakAlliance implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null, "voulez vous vraiment d�truire cette  l'alliance","fin d'allaince d'allaince", JOptionPane.YES_NO_OPTION);
		}
	}
	
	class ActionMakeAlliance implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null, "voulez vous vraiment cr�er l'alliance","creation d(allaince", JOptionPane.YES_NO_OPTION);
			
		}
		
	}
	class ActionContrcut implements ActionListener{
		String[] choices = { "caserne (100 bois)", "scierie", "mine", "port", "�curie","caserne","mur","porte","temple"};
		public void actionPerformed(ActionEvent e) {
			JComboBox building = new JComboBox(choices);
			JOptionPane.showMessageDialog(null, building, "construction possible", 0);
			building.getSelectedIndex();
			System.out.println(building.getSelectedIndex());
		
				
		}
		
	}
	
}

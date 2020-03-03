package GUI.components.game;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class chooseActionBoutonsPanel extends JPanel {
	private JButton actionBreakAllianceButton = new JButton("casser une alliance ");
	private JButton actionMakeAllianceButton = new JButton("créer une alliance ");
	private JButton createActionAttackButton = new JButton("lancer une attaque");
	private JButton createActionMoveButton = new JButton("déplacer les trouppes");
	private JButton createActionConstructButton = new JButton("contruire un batiment ");
	private JButton createActionCreateUnitButton = new JButton("créer de nouvelle troupe");
	private JButton createActionDestroyUnitButton = new JButton("détruire les unité  troupe");
	private JButton createActionDestroyBuildingtButton = new JButton("destruit le batiment");
	private Dimension  a = new Dimension(30,100);
	
	public chooseActionBoutonsPanel() {
		setLayout(new GridLayout(4, 3));
		add(actionBreakAllianceButton);
		add(actionMakeAllianceButton);
		add(createActionAttackButton);
		add(createActionMoveButton);
		add(createActionDestroyUnitButton);
		add(createActionDestroyBuildingtButton);
		add(createActionCreateUnitButton);
		add(createActionConstructButton);
		 
	}
}

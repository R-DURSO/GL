package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Game extends JFrame {

	private JPanel contentPane;
	private final JLabel map = new JLabel("affichage de la map");

/*
* 
*/
	private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800,400);
	private JLabel positionLabel = new JLabel("position");
	private	JLabel actionLabel = new JLabel("action");
	private	JLabel goldLabel = new JLabel("or");
	private	JLabel woodLabel = new JLabel("bois");
	private	JButton quitButton = new JButton("Quitter la partie");	
	private	JLabel stoneLabel = new JLabel("pierre");
	private	JLabel foodLabel = new JLabel("nouriture");	
	private	JLabel actionReadyComboBox = new JLabel("on en fait des liste avec bouton ou bien un menu ?");	
	private	JLabel label = new JLabel("New label");	
	private	JLabel table = new JLabel("tableau ou le r\u00E9sumer et marquer");
	private	JLabel action_doComboBox = new JLabel("on en fait des liste avec bouton ou bien un menu ?");
	private	JLabel costLabel = new JLabel("cout btaiment/unit\u00E9");		
	private	JButton endTurnButton = new JButton("fin de tour");	
	private int saveGame;
	/**
	 * Create the frame.
	 */
	public Game() {
		super("Conquête");
		init();		
	}
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(IDEAL_MAIN_DIMENSION);
		setBounds(100, 100, 967, 503);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		positionLabel.setBounds(0, 0, 77, 40);
		contentPane.add(positionLabel);
		
		
		actionLabel.setBounds(85, 0, 54, 40);
		contentPane.add(actionLabel);
		

		goldLabel.setBounds(138, 0, 54, 40);
		contentPane.add(goldLabel);
		

		woodLabel.setBounds(194, 0, 54, 40);
		contentPane.add(woodLabel);
		
		
		stoneLabel.setBounds(252, 0, 54, 40);
		contentPane.add(stoneLabel);

		foodLabel.setBounds(313, 0, 54, 40);
		contentPane.add(foodLabel);
		

		actionReadyComboBox.setBounds(368, 0, 578, 40);
		contentPane.add(actionReadyComboBox);
		

		label.setBounds(-20, 26, 17, 14);
		contentPane.add(label);
		map.setHorizontalAlignment(SwingConstants.CENTER);
		map.setBounds(-10, 42, 778, 303);
		contentPane.add(map);
		

		table.setHorizontalAlignment(SwingConstants.TRAILING);
		table.setBounds(775, 36, 171, 303);
		contentPane.add(table);

		endTurnButton.setBounds(0, 356, 209, 101);
		contentPane.add(endTurnButton);
		

		costLabel.setBounds(538, 356, 161, 101);
		contentPane.add(costLabel);
		action_doComboBox.setBounds(219, 363, 290, 86);
		contentPane.add(action_doComboBox);
		

		quitButton.setBounds(700, 356, 246, 112);
		quitButton.addActionListener(new quitAction());
		contentPane.add(quitButton);
		setVisible(true);
	}
	private void udapteValues() {
		/*
		 * on écrira ici les modfication des valeur de ressource ect ....
		 */
	}
	private class quitAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			 saveGame = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder la partie ? ");
			
		}
		
	}
}

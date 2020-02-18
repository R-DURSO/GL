package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Game extends JFrame {

	private JPanel contentPane;
	private final JLabel map = new JLabel("affichage de la map");

/*
* 
*/
	private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800,400);
	JLabel position = new JLabel("position");
	JLabel Action = new JLabel("action");
	JLabel gold = new JLabel("or");
	JLabel wood = new JLabel("bois");
	JButton EndButton = new JButton("Quitter la partie");	
	JLabel Stone = new JLabel("pierre");
	JLabel food = new JLabel("nouriture");	
	JLabel action_ReadyComboBox = new JLabel("on en fait des liste avec bouton ou bien un menu ?");	
	JLabel label = new JLabel("New label");	
	JLabel table = new JLabel("tableau ou le r\u00E9sumer et marquer");
	JLabel action_doComboBox = new JLabel("on en fait des liste avec bouton ou bien un menu ?");
	JLabel cost = new JLabel("cout btaiment/unit\u00E9");		
	JButton end_TurnButton = new JButton("fin de tour");	
	
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
		
		
		position.setBounds(0, 0, 77, 40);
		contentPane.add(position);
		
		
		Action.setBounds(85, 0, 54, 40);
		contentPane.add(Action);
		

		gold.setBounds(138, 0, 54, 40);
		contentPane.add(gold);
		

		wood.setBounds(194, 0, 54, 40);
		contentPane.add(wood);
		
		
		Stone.setBounds(252, 0, 54, 40);
		contentPane.add(Stone);

		food.setBounds(313, 0, 54, 40);
		contentPane.add(food);
		

		action_ReadyComboBox.setBounds(368, 0, 578, 40);
		contentPane.add(action_ReadyComboBox);
		

		label.setBounds(-20, 26, 17, 14);
		contentPane.add(label);
		map.setHorizontalAlignment(SwingConstants.CENTER);
		map.setBounds(-10, 42, 778, 303);
		contentPane.add(map);
		

		table.setHorizontalAlignment(SwingConstants.TRAILING);
		table.setBounds(775, 36, 171, 303);
		contentPane.add(table);

		end_TurnButton.setBounds(0, 356, 209, 101);
		contentPane.add(end_TurnButton);
		

		cost.setBounds(538, 356, 161, 101);
		contentPane.add(cost);
		action_doComboBox.setBounds(219, 363, 290, 86);
		contentPane.add(action_doComboBox);
		

		EndButton.setBounds(700, 356, 246, 112);
		contentPane.add(EndButton);
		setVisible(true);
	}
	private void udapteValues() {
		/*
		 * on écrira ici les modfication des valeur de ressource ect ....
		 */
	}
}

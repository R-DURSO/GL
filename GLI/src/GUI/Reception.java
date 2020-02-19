package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Reception extends JFrame {
	private String aiLevels[] = {"facile", "moyen", "difficile"};
	private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800,400);
	private JPanel contentPaneBombox;
	private	JPanel titleTextArea = new JPanel();
	private	JLabel Conquête = new JLabel("Conquête");
	private	JLabel creationTextArea = new JLabel("Creation de la partie ");
	private	JLabel numberOfPlayerTextArea = new JLabel("nombre de joueurs");
	private	JLabel MapTextArea = new JLabel("creation de la carte");
	private	JComboBox number = new JComboBox();
	private	JButton lessBouton = new JButton("-");
	private	JTextArea sideTextArea = new JTextArea();
	private	JButton moreButon = new JButton("+");
	private	JComboBox levelIa1Bombox= new  JComboBox(aiLevels);
	private	JComboBox levelIa2Bombox = new  JComboBox(aiLevels);
	private	JComboBox levelIa3Bombox = new  JComboBox(aiLevels);
	private	JLabel levelOfIaTextArea = new JLabel("niveau des ia ");
	private	JLabel IA1TextArea = new JLabel("ia 1");
	private	JLabel IA2TextArea = new JLabel("ia 2");
	private	JLabel IA3TextArea = new JLabel("ia 3");		
	private	JLabel waterTextArea = new JLabel("eau");
	private	JComboBox howwaterBombox = new JComboBox();
	private int value;
	private JFrame game = new Game();
	JButton ResumeBouton = new JButton("charger la derni\u00E8re partie ");	
	JButton StartBouton = new JButton("Comencer une nouvelle partie");	
	
	public Reception() {
		super("Conquête accueil");
		init();
	}
	
	private void init() {
		game.setVisible(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(IDEAL_MAIN_DIMENSION);
		setBounds(100, 100, 856, 376);
		contentPaneBombox = new JPanel();
		contentPaneBombox.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPaneBombox);
		contentPaneBombox.setLayout(null);
	
		titleTextArea.setBounds(5, 5, 832, 24);
		contentPaneBombox.add(titleTextArea);
		
		titleTextArea.add(Conquête);
	
		creationTextArea.setBounds(376, 40, 129, 53);
		contentPaneBombox.add(creationTextArea);
	

		numberOfPlayerTextArea.setBounds(0, 103, 146, 58);
		contentPaneBombox.add(numberOfPlayerTextArea);
	

		number.setBounds(156, 111, 38, 42);
		number.addItem("1");
		number.addItem("2");
		number.addItem("3");
		contentPaneBombox.add(number);
	

		MapTextArea.setBounds(0, 175, 117, 53);
		contentPaneBombox.add(MapTextArea);
	

		lessBouton.setBounds(127, 190, 41, 23);
		contentPaneBombox.add(lessBouton);
	
		value=0; 
		sideTextArea.setText("0");
		sideTextArea.setBounds(178, 189, 51, 24);
		contentPaneBombox.add(sideTextArea);
	

		moreButon.setBounds(248, 190, 51, 23);
		//moreButon.addActionListener(new moreAction());
		contentPaneBombox.add(moreButon);
	

		levelOfIaTextArea.setBounds(442, 111, 116, 42);
		contentPaneBombox.add(levelOfIaTextArea);

		levelIa1Bombox.setBounds(688, 118, 78, 28);
		contentPaneBombox.add(levelIa1Bombox);

		levelIa2Bombox.setBounds(688, 161, 78, 28);
		contentPaneBombox.add(levelIa2Bombox);

		levelIa3Bombox.setBounds(688, 200, 78, 28);
		contentPaneBombox.add(levelIa3Bombox);
	

		IA1TextArea.setBounds(599, 121, 51, 32);
		contentPaneBombox.add(IA1TextArea);

		IA2TextArea.setBounds(599, 157, 51, 32);
		contentPaneBombox.add(IA2TextArea);

		IA3TextArea.setBounds(599, 202, 59, 24);
		contentPaneBombox.add(IA3TextArea);

		
		waterTextArea.setBounds(322, 189, 38, 24);
		contentPaneBombox.add(waterTextArea);
	

		howwaterBombox.setBounds(388, 190, 78, 32);
		howwaterBombox.addItem("un peu");
		howwaterBombox.addItem("normal");
		howwaterBombox.addItem("beacoup");
		contentPaneBombox.add(howwaterBombox);

		StartBouton.setBounds(74, 267, 225, 53);
		StartBouton.addActionListener(new ActionCreatGame());
		contentPaneBombox.add(StartBouton);
	

		ResumeBouton.setBounds(459, 264, 275, 58);
		//ResumeBouton.addActionListener(new test());
		contentPaneBombox.add(ResumeBouton);
		setVisible(true);	
	}
	
	private class ActionCreatGame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			game.setVisible(true);
			setVisible(false);
		}
		private class test implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				game.setVisible(true); // on a pas encore crée la possibilité de sauvegarde 
			}
		}
		private class moreAction implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				value++;
			}
		}
	}
}

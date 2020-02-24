package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Reception extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1732173298441163253L;
	private static final String aiLevelsList[] = {"facile", "moyen", "difficile"};
	private static final String waterAmountsList[] = {"Un peu", "Normal", "Beaucoup"};
	private static final Integer playerNumbersList[] = {1, 2, 3};
	
	private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800,400);
	private JPanel contentPaneBombox;
	private	JPanel titlePanel = new JPanel();
	private	JLabel ConquêteLabel = new JLabel("Conquête");
	private	JLabel creationLabel = new JLabel("Creation de la partie ");
	private	JLabel playerNumberLabel = new JLabel("nombre de joueurs");
	private	JLabel MapLabel = new JLabel("creation de la carte");
	private	JComboBox<Integer> playerNumberComboBox = new JComboBox<>(playerNumbersList);
	private	JButton lessBouton = new JButton("-");
	private	JTextArea sizeTextArea = new JTextArea();
	private	JButton moreButon = new JButton("+");
	private	JComboBox<String> levelIa1Bombox= new  JComboBox<>(aiLevelsList);
	private	JComboBox<String> levelIa2Bombox = new  JComboBox<>(aiLevelsList);
	private	JComboBox<String> levelIa3Bombox = new  JComboBox<>(aiLevelsList);
	private	JLabel LevelIaLabel = new JLabel("niveau des ia ");
	private	JLabel IA1Label = new JLabel("IA 1");
	private	JLabel IA2Label = new JLabel("IA 2");
	private	JLabel IA3Label= new JLabel("IA 3");		
	private	JLabel waterLabel = new JLabel("quantité d'eau");
	private	JComboBox<String> waterAmountBombox = new JComboBox<>(waterAmountsList);
	private int value;
	private int newGame;
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
	
		titlePanel.setBounds(5, 5, 832, 24);
		contentPaneBombox.add(titlePanel);
		
		titlePanel.add(ConquêteLabel);
	
		creationLabel.setBounds(376, 40, 129, 53);
		contentPaneBombox.add(creationLabel);
	

		playerNumberLabel.setBounds(0, 103, 146, 58);
		contentPaneBombox.add(playerNumberLabel);
	

		playerNumberComboBox.setBounds(156, 111, 38, 42);
		contentPaneBombox.add(playerNumberComboBox);
	

		MapLabel.setBounds(0, 175, 117, 53);
		contentPaneBombox.add(MapLabel);
	

		lessBouton.setBounds(127, 190, 41, 23);
		lessBouton.addActionListener(new lessAction());
		contentPaneBombox.add(lessBouton);
	
		value=20; 
		sizeTextArea.setText(Integer.toString(value));
		sizeTextArea.setBounds(178, 189, 51, 24);
		contentPaneBombox.add(sizeTextArea);
	

		moreButon.setBounds(248, 190, 51, 23);
		moreButon.addActionListener(new moreAction());
		contentPaneBombox.add(moreButon);
	

		LevelIaLabel.setBounds(442, 111, 116, 42);
		contentPaneBombox.add(LevelIaLabel);

		levelIa1Bombox.setBounds(688, 118, 78, 28);
		contentPaneBombox.add(levelIa1Bombox);

		levelIa2Bombox.setBounds(688, 161, 78, 28);
		contentPaneBombox.add(levelIa2Bombox);

		levelIa3Bombox.setBounds(688, 200, 78, 28);
		contentPaneBombox.add(levelIa3Bombox);
	

		IA1Label.setBounds(599, 121, 51, 32);
		contentPaneBombox.add(IA1Label);

		IA2Label.setBounds(599, 157, 51, 32);
		contentPaneBombox.add(IA2Label);

		IA3Label.setBounds(599, 202, 59, 24);
		contentPaneBombox.add(IA3Label);

		
		waterLabel.setBounds(322, 189, 38, 24);
		contentPaneBombox.add(waterLabel);
	

		waterAmountBombox.setBounds(388, 190, 78, 32);
		contentPaneBombox.add(waterAmountBombox);

		StartBouton.setBounds(74, 267, 225, 53);
		StartBouton.addActionListener(new ActionCreatGame());
		contentPaneBombox.add(StartBouton);
	

		ResumeBouton.setBounds(459, 264, 275, 58);
		ResumeBouton.addActionListener(new test());
		contentPaneBombox.add(ResumeBouton);
		setVisible(true);	
	}
	
	private class ActionCreatGame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			 newGame = JOptionPane.showConfirmDialog(null, "crée une nouvelle partie superimera l'ancienne");
			 if(newGame==0) {
				 game.setVisible(true);
				 setVisible(false);
			 }
		}
	}
	private class test implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			game.setVisible(true); // on a pas encore crée la possibilité de sauvegarde 
		}
	}
	private class moreAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			value++;
			sizeTextArea.setText(Integer.toString(value));
		}
	}
	private class lessAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			value--;
			sizeTextArea.setText(Integer.toString(value));
			
		}
		
	}
}

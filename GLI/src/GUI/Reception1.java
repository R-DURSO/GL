package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Reception1 extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reception1 frame = new Reception1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Reception1() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 856, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel title = new JPanel();
		title.setBounds(5, 5, 832, 24);
		contentPane.add(title);
		
		JLabel Conquête = new JLabel("Conquête");
		title.add(Conquête);
		
		JLabel creation = new JLabel("Creation de la partie ");
		creation.setBounds(376, 40, 129, 53);
		contentPane.add(creation);
		
		JLabel numberOfPlayer = new JLabel("nombre de joueurs");
		numberOfPlayer.setBounds(0, 103, 146, 58);
		contentPane.add(numberOfPlayer);
		
		JComboBox number = new JComboBox();
		number.setBounds(156, 111, 38, 42);
		number.addItem("1");
		number.addItem("2");
		number.addItem("3");
		contentPane.add(number);
		
		JLabel Map = new JLabel("creation de la carte");
		Map.setBounds(0, 175, 92, 53);
		contentPane.add(Map);
		
		JButton less = new JButton("-");
		less.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		less.setBounds(102, 190, 41, 23);
		contentPane.add(less);
		
		JTextArea textArea = new JTextArea();
		textArea.setText("0");
		textArea.setBounds(156, 189, 51, 24);
		contentPane.add(textArea);
		
		JButton more = new JButton("+");
		more.setBounds(218, 190, 51, 23);
		contentPane.add(more);
		
		JLabel levelOfIa = new JLabel("niveau des ia ");
		levelOfIa.setBounds(442, 111, 116, 42);
		contentPane.add(levelOfIa);
		
		JComboBox levelIa1 = new JComboBox();
		levelIa1.setBounds(688, 118, 78, 28);
		levelIa1.addItem("facile");
		levelIa1.addItem("normal");
		levelIa1.addItem("dificile");
		contentPane.add(levelIa1);
		
		JComboBox levelIa2 = new JComboBox();
		levelIa2.setBounds(688, 161, 78, 28);
		levelIa2.addItem("facile");
		levelIa2.addItem("normal");
		levelIa2.addItem("dificile");
		contentPane.add(levelIa2);
		
		JComboBox levelIa3 = new JComboBox();
		levelIa3.setBounds(688, 200, 78, 28);
		levelIa3.addItem("facile");
		levelIa3.addItem("normal");
		levelIa3.addItem("dificile");
		contentPane.add(levelIa3);
		
		JLabel IA1 = new JLabel("ia 1");
		IA1.setBounds(599, 121, 51, 32);
		contentPane.add(IA1);
		
		JLabel IA2 = new JLabel("ia 2");
		IA2.setBounds(599, 157, 51, 32);
		contentPane.add(IA2);
		
		JLabel IA3 = new JLabel("ia 3");
		IA3.setBounds(599, 202, 59, 24);
		contentPane.add(IA3);
		
		JLabel water = new JLabel("eau");
		water.setBounds(291, 194, 78, 24);
		contentPane.add(water);
		
		JComboBox howwater = new JComboBox();
		howwater.setBounds(388, 190, 78, 32);
		howwater.addItem("un peu");
		howwater.addItem("normal");
		howwater.addItem("beacoup");
		contentPane.add(howwater);
		
		JButton Start = new JButton("Comencer une nouvelle partie");
		Start.setBounds(74, 267, 225, 53);
		contentPane.add(Start);
		
		JButton resume = new JButton("charger la derni\u00E8re partie ");
		resume.setBounds(459, 264, 275, 58);
		contentPane.add(resume);
		
	}
}

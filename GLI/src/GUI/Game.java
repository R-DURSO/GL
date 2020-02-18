package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Game extends JFrame implements Runnable{

	private JPanel contentPane;
	private final JLabel map = new JLabel("affichage de la map");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public class test implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	/**
	 * Create the frame.
	 */
	public Game() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 505);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel position = new JLabel("position");
		position.setBounds(0, 0, 77, 40);
		contentPane.add(position);
		
		JLabel Action = new JLabel("action");
		Action.setBounds(85, 0, 54, 40);
		contentPane.add(Action);
		
		JLabel gold = new JLabel("or");
		gold.setBounds(138, 0, 54, 40);
		contentPane.add(gold);
		
		JLabel wood = new JLabel("bois");
		wood.setBounds(194, 0, 54, 40);
		contentPane.add(wood);
		
		JLabel Stone = new JLabel("pierre");
		Stone.setBounds(252, 0, 54, 40);
		contentPane.add(Stone);
		
		JLabel food = new JLabel("nouriture");
		food.setBounds(313, 0, 54, 40);
		contentPane.add(food);
		
		JLabel action_Ready = new JLabel("on en fait des liste avec bouton ou bien un menu ?");
		action_Ready.setBounds(368, 0, 578, 40);
		contentPane.add(action_Ready);
		
		JLabel label = new JLabel("New label");
		label.setBounds(-20, 26, 17, 14);
		contentPane.add(label);
		map.setHorizontalAlignment(SwingConstants.CENTER);
		map.setBounds(-10, 42, 778, 303);
		contentPane.add(map);
		
		JLabel table = new JLabel("tableau ou le r\u00E9sumer et marquer");
		table.setHorizontalAlignment(SwingConstants.TRAILING);
		table.setBounds(775, 36, 171, 303);
		contentPane.add(table);
		
		JButton end_Turn = new JButton("fin de tour");
		end_Turn.setBounds(0, 356, 209, 101);
		contentPane.add(end_Turn);
		
		JLabel cost = new JLabel("cout btaiment/unit\u00E9");
		cost.setBounds(538, 356, 200, 101);
		contentPane.add(cost);
		
		JLabel action_Ready_1 = new JLabel("on en fait des liste avec bouton ou bien un menu ?");
		action_Ready_1.setBounds(219, 363, 290, 86);
		contentPane.add(action_Ready_1);
		
		JButton End = new JButton("Quitter la partie");
		End.setBounds(700, 356, 246, 112);
		contentPane.add(End);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

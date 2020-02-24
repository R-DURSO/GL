package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.components.GuiPreferences;
import GUI.sub_panels.GamePanel;
import GUI.sub_panels.MenuPanel;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6569351248766478602L;
	private JPanel gamePanel = new GamePanel();
	private JPanel menuPanel = new MenuPanel();
	private JFrame context = this;
	private CardLayout cardLayout = new CardLayout();
	

	public MainWindow() {
		super("Conquête");
		System.out.println(GuiPreferences.WIDTH + " " + GuiPreferences.HEIGHT);
		setPreferredSize(new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT));
		init();
		getContentPane().add(gamePanel, "game");
		getContentPane().add(menuPanel, "menu");
		cardLayout.show(getContentPane(), "menu");
	}

	private void init() {
		this.setLayout(cardLayout);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}

}

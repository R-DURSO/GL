package GUI.sub_panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import data.GameConstants;

/**
 * Panel that is displaed when a player win the game.
 * @author Aldric Vitali Silvestre
 *
 */
public class VictoryPanel extends JPanel {
	
	private MainWindow window;

	private JLabel winnerLabel;
	private JLabel victoryTypeLabel;
	private JLabel scoreLabel;
	
	private Image image;
	
	private JButton returnButton = new JButton("Retour au menu principal");
	private JButton quitButton = new JButton("Quitter le jeu");
	
	private final Dimension DIM_TOP = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 4);
	private final Dimension DIM_IMAGE_PANEL = new Dimension(GuiPreferences.WIDTH / 4, GuiPreferences.HEIGHT / 2);
	private final Dimension DIM_SCORE = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 16);
	private final Dimension DIM_BUTTONS_PANELS = new Dimension(GuiPreferences.WIDTH, 3 * GuiPreferences.HEIGHT / 16);
	
	
	private final int PANEL_BUTTONS_WIDTH = (int)MenuPanel.CHOICE_DIMENSION.getWidth();
	private final int PANEL_BUTTONS_HEIGHT = (int)MenuPanel.CHOICE_DIMENSION.getHeight();
	private final Dimension DIM_BUTTON = new Dimension(PANEL_BUTTONS_WIDTH / 4, PANEL_BUTTONS_HEIGHT / 2);
	
	JPanel topPanel = new JPanel(new GridLayout(0,1));
	JPanel imagePanel = new JPanel();
	JPanel scorePanel = new JPanel(new BorderLayout());
	JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, (int)DIM_BUTTON.getHeight() / 2, (int)DIM_BUTTON.getHeight()));
	
	public VictoryPanel(MainWindow window) {
		this.window = window;
		try {
			image = ImageIO.read(new File("GLI/src/images/misc/cup.png"));
			image = image.getScaledInstance((int)DIM_IMAGE_PANEL.getWidth(), (int)DIM_IMAGE_PANEL.getHeight(), Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	/**
	 * Initilizes all thae data that VictoryPanel needs to be constructed
	 * @param winnerName the name of the power who win
	 * @param victoryType the type of victory (values are in [{@linkplain data.GameConstants})
	 * @param territorySize the territory size of the player who win
	 */
	public void initVictoryPanel(String winnerName, int victoryType, int territorySize) {
		//top panel
		winnerLabel = new JLabel("Victoire du joueur " + winnerName +"!", SwingConstants.CENTER);
		
		if (victoryType == GameConstants.VICTORY_TYPE_MILITARY) {
			victoryTypeLabel = new JLabel("Domination totale!", SwingConstants.CENTER);
		}else {
			victoryTypeLabel = new JLabel("Temple entièrement construit", SwingConstants.CENTER);
		}
		scoreLabel = new JLabel("Taille du territoire du vainqueur : " + territorySize + " cases", SwingConstants.CENTER);
		
		topPanel.setPreferredSize(DIM_TOP);
		topPanel.add(winnerLabel);
		topPanel.add(victoryTypeLabel);
		
		
		//image panel
		imagePanel.setPreferredSize(DIM_IMAGE_PANEL);
		imagePanel.add(new JLabel(new ImageIcon(image)));
		
		//score panel 
		scorePanel.setPreferredSize(DIM_SCORE);
		scorePanel.add(scoreLabel, BorderLayout.CENTER);
		
		
		//buttons
		buttonsPanel.setPreferredSize(DIM_BUTTONS_PANELS);
		returnButton.addActionListener(new ActionReturn());
		returnButton.setPreferredSize(DIM_BUTTON);
		
		quitButton.addActionListener(new ActionQuit());
		quitButton.setPreferredSize(DIM_BUTTON);
		
		buttonsPanel.add(returnButton);
		buttonsPanel.add(quitButton);
		
		//add all panels
		add(topPanel);
		add(scorePanel);
		add(imagePanel);
		
		add(buttonsPanel);
		initFont(this);
		winnerLabel.setFont(new Font("Serif", Font.BOLD, GuiPreferences.WIDTH / 40));
	}
	
	private void initFont(Container container) {
		for(Component component : container.getComponents()) {
			component.setFont(GuiPreferences.BASE_FONT);
			if(component instanceof Container) {
				Container childContainer = (Container)component;
				initFont(childContainer);
			}
		}
	}
	
	private void removeAllPanels() {
		remove(buttonsPanel);
		remove(topPanel);
		remove(imagePanel);
		remove(scorePanel);
	}

	class ActionReturn implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			removeAllPanels();
			window.changeWindow(MainWindow.MENU_WINDOW);
		}
	}
	
	class ActionQuit implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(window, "Voulez-vous vraiment quitter le jeu", "Confirmation", JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}
}

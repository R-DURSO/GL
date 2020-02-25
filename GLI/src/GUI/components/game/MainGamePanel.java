package GUI.components.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.components.GuiPreferences;

public class MainGamePanel extends JPanel{
	private static final long serialVersionUID = -4989371043690170741L;
	
	private final Dimension DIMENSION_MAIN = 
			new Dimension(GuiPreferences.WIDTH * GuiPreferences.GAME_MAIN_RATIO_SUBSTRACT_WIDTH / GuiPreferences.GAME_MAIN_RATIO_WIDTH,
					GuiPreferences.HEIGHT * GuiPreferences.GAME_PANELS_SUBSTRACT_HEIGHT / GuiPreferences.GAME_PANELS_RATIO_HEIGHT);
	private final Dimension DIMENSION_ASIDE = 
			new Dimension((GuiPreferences.WIDTH / GuiPreferences.GAME_MAIN_RATIO_WIDTH) - 10,
					GuiPreferences.HEIGHT * GuiPreferences.GAME_PANELS_SUBSTRACT_HEIGHT / GuiPreferences.GAME_PANELS_RATIO_HEIGHT);
	private JPanel mapPanel = new JPanel();
	private JPanel infosPanel = new JPanel();

	public MainGamePanel() {
		init();

	}

	private void init() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		
		initMapPanel();
		initInfosPanel();
		
		add(mapPanel);
		add(infosPanel);
	}

	private void initMapPanel() {
		mapPanel.setPreferredSize(DIMENSION_MAIN);
		mapPanel.setLayout(new GridLayout(0,1));
		mapPanel.add(new JLabel("affichage de la map", SwingConstants.CENTER));
	}
	
	private void initInfosPanel() {
		infosPanel.setPreferredSize(DIMENSION_ASIDE);
		infosPanel.setLayout(new GridLayout(0,1));
		infosPanel.add(new JLabel("<html><p>infos du joueur, affiche:</p>"
				+ "<ul><li>liste des troupes (nombre de chaque unité par type)</li>"
				+ "<li>liste des batiments (pareil)</li></ul>"
				+ "</html>", SwingConstants.RIGHT));
		infosPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}


}

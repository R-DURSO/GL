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
	public MainGamePanel() {
		init();

	}

	private void init() {
		setLayout(new GridLayout(0,1));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		setLayout(new GridLayout(0,1));
		add(new JLabel("affichage de la map", SwingConstants.CENTER));
	}

}

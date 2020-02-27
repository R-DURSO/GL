package GUI.sub_panels;


import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.menu.ChoicePanel;
import GUI.components.menu.PreferencesPanel;
import GUI.components.menu.TitlePanel;

public class MenuPanel extends JPanel{
	private static final long serialVersionUID = 5832326052399006926L;
	
	private JPanel titlePanel = new TitlePanel();
	private JPanel preferencesPanel = new PreferencesPanel();
	private JPanel choicePanel;
	
	private final Dimension TITLE_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 10);
	private final Dimension PREFERENCES_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 2);
	private final Dimension CHOICE_DIMENSION = TITLE_DIMENSION;

	public MenuPanel(MainWindow context) {
		choicePanel = new ChoicePanel(context);
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		preferencesPanel.setPreferredSize(PREFERENCES_DIMENSION);
		choicePanel.setPreferredSize(CHOICE_DIMENSION);
		add(titlePanel);
		add(preferencesPanel);
		add(choicePanel);
	}
	
	public JPanel getPreferencesPanel() {
		return preferencesPanel;
	}

}

package GUI.sub_panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.menu.ChoicePanel;
import GUI.components.menu.PreferencesPanel;
import GUI.components.menu.TitlePanel;

public class MenuPanel extends JPanel {
	private MainWindow context;

	private JPanel titlePanel = new TitlePanel();
	private PreferencesPanel preferencesPanel = new PreferencesPanel();
	private JPanel choicePanel;

	public static final Dimension TITLE_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 8);
	public static final Dimension PREFERENCES_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 2);
	public static final Dimension CHOICE_DIMENSION = TITLE_DIMENSION;

	public MenuPanel(MainWindow context) {
		this.context = context;
		choicePanel = new ChoicePanel(context);
		init();
//		apply base font to all components, except title panel 
		initFont(preferencesPanel);
		initFont(choicePanel);
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		preferencesPanel.setPreferredSize(PREFERENCES_DIMENSION);
		choicePanel.setPreferredSize(CHOICE_DIMENSION);
		// change background color to all panels
		add(titlePanel);
		add(preferencesPanel);
		add(choicePanel);
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

	public PreferencesPanel getPreferencesPanel() {
		return preferencesPanel;
	}

}

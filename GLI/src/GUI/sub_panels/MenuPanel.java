package GUI.sub_panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import GUI.MainWindow;
import GUI.components.GuiPreferences;
import GUI.components.menu.ChoicePanel;
import GUI.components.menu.OptionsPanel;
import GUI.components.menu.TitlePanel;

/**
 * <p>Represent the Main Menu of the Game
 * <br>where the Player can choose different options to customize the {@link GUI.sub_panels.GamePanel Game}</p>
 * @author Aldric
 */
public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MainWindow context;

	private JPanel titlePanel = new TitlePanel();
	private OptionsPanel optionsPanel = new OptionsPanel();
	private JPanel choicePanel;

	public static final Dimension TITLE_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 8);
	public static final Dimension PREFERENCES_DIMENSION = new Dimension(GuiPreferences.WIDTH, GuiPreferences.HEIGHT / 2);
	public static final Dimension CHOICE_DIMENSION = TITLE_DIMENSION;

	public MenuPanel(MainWindow context) {
		this.context = context;
		choicePanel = new ChoicePanel(context);
		init();
//		apply base font to all components, except title panel 
		initFont(choicePanel);
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		optionsPanel.setPreferredSize(PREFERENCES_DIMENSION);
		choicePanel.setPreferredSize(CHOICE_DIMENSION);
		// change background color to all panels
		add(titlePanel);
		add(optionsPanel);
		add(choicePanel);
		
		initFont(optionsPanel);
		initFont(choicePanel);
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

	public OptionsPanel getOptionsPanel() {
		return optionsPanel;
	}
}

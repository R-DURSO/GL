package GUI.sub_panels;


import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5832326052399006926L;
	private JPanel context = this;

	public MenuPanel() {
		init();
	}

	private void init() {
		setLayout(new BoxLayout(context, BoxLayout.Y_AXIS));
	}

}

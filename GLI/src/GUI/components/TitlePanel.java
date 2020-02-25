package GUI.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TitlePanel extends JPanel{
	private static final long serialVersionUID = 8345861158523288004L;
	private JLabel titleLabel = new JLabel("Conquête", SwingConstants.CENTER);
	private JLabel subtitleLabel = new JLabel("Création de la partie", SwingConstants.CENTER);
	
	//title is a little bit larger than other labels, so it has a différent font_size
	private final Font titleFont = new Font("Serif", Font.BOLD, GuiPreferences.WIDTH / 30);
	private final Font subtitleFont = new Font("Serif", Font.PLAIN, GuiPreferences.FONT_SIZE);

	public TitlePanel() {
		setLayout(new GridLayout(2, 1, 16, 16));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
		titleLabel.setFont(titleFont);
		subtitleLabel.setFont(subtitleFont);
		add(titleLabel);
		add(subtitleLabel);
	}

}

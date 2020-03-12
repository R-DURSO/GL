package GUI.components.game;

import java.awt.GridLayout;

import javax.swing.JPanel;

import GUI.sub_panels.GamePanel;
import data.Position;
import data.boxes.Box;

public class InfosPanel extends JPanel{
	private BoxSelectedPanel boxSelectedPanel;
	private JPanel statsPanel = new JPanel();
	private GamePanel context;
	
	public InfosPanel() {
		setLayout(new GridLayout(0,1));
		
	}
	
	public void initInfosPanel(GamePanel context) {
		this.context = context;
		boxSelectedPanel = new BoxSelectedPanel(context.getMapPanel().getBoxByPosition(new Position(0,0)));
		add(boxSelectedPanel);
		add(statsPanel);
	}
	
	public void refreshBoxHover(Box box) {
		boxSelectedPanel.refresh(box);
	}
	
}

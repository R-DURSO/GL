package GUI.components.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Graphics;

import GUI.components.GuiPreferences;
import data.GameMap;
import data.Power;
import process.management.MapBuilder;

public class MainGamePanel extends JPanel{
	private static final long serialVersionUID = -4989371043690170741L;
	private GameMap map;
	private int scaleWidth;
	private int scaleHeight;
	
	private final int MAP_SIZE = 10;
	
	private boolean debugMode = true;
	
	public MainGamePanel() {
		super();
		init();
		//en attendant la reception des paramètres
		buildMap();
		setBackground(Color.WHITE);
	}
	
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(debugMode) {
			drawDebugModeGrid(g);
		}
		drawMap(g);
	}
	


	private void drawMap(Graphics g) {
        super.paintComponent(g);
        Random rand = new Random();
        g.clearRect(0, 0, getWidth(), getHeight());
        int rectWidth = getWidth() / map.getSize();
        int rectHeight = getHeight() / map.getSize();

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                int x = i * rectWidth;
                int y = j * rectHeight;
                float r = rand.nextFloat();
            	float gr = rand.nextFloat();
            	float b = rand.nextFloat();
            	g.setColor(new Color(r, gr, b));
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
	}



	private void drawDebugModeGrid(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.setColor(Color.GRAY);
		
		for(int i = scaleWidth; i < MAP_SIZE * scaleWidth; i += scaleWidth){
			g.drawLine(i, 1, i, height);
		}
		for(int i = scaleHeight; i < MAP_SIZE * scaleHeight; i += scaleHeight){
			g.drawLine(1, i, width, i);
		}
	}

	private void buildMap() {
		Power powers[] = new Power[4];
		for(int i = 1; i < 5; i++) {
			powers[i-1] = new Power("joueur "+ i);
		}
		MapBuilder mb = new MapBuilder(MAP_SIZE, 20, powers);
		map = mb.buildMap();
	}

	private void init() {
		setLayout(new GridLayout(0,1));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		setLayout(new GridLayout(0,1));
	}
	
	

}

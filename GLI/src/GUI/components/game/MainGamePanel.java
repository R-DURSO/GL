package GUI.components.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import GUI.colors.ColorData;
import data.GameMap;
import data.Power;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.boxes.WaterBox;

public class MainGamePanel extends JPanel{
	private static final long serialVersionUID = -4989371043690170741L;
	private GameMap map;
	private Power powers[];
	
	public MainGamePanel(GameMap map, Power powers[]) {
		super();
		init();
		
		this.powers = powers;
		this.map = map;
		setBackground(Color.BLACK);
	}
	
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
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
                //draw background of box 
                determineBoxColor(g, i, j);
                g.fillRect(x, y, rectWidth, rectHeight);
                
                //border color
                determineBoxBorder(g, i, j);
                g.drawRect(x, y, rectWidth-1, rectHeight-1);
                
                // TODO now, we want to draw shapes to show what resource, unit, building are in box and who has this box
                
            }
        }
	}


	/*
     * Border Color:
     * 	Each color represents a belonging
     *		-Black : no one
     *		-Red : player 1
     *		-Yellow : player 2
     *		-Green : player 3
     *		-White : player 4*/
	private void determineBoxBorder(Graphics g, int i, int j) {
		Box box = map.getBox(i, j);
		if(box.hasOwner()) {
			Power power = box.getOwner();
			if(powers[0] == power) {
				g.setColor(ColorData.POWER_1_COLOR);
			}else if(powers[1] == power) {
				g.setColor(ColorData.POWER_2_COLOR);
			}else if(powers[2] == power) {
				g.setColor(ColorData.POWER_3_COLOR);
			}else{ //no other choice than power 4
				g.setColor(ColorData.POWER_4_COLOR);
			}
		}else {
			g.setColor(ColorData.NO_POWER_COLOR);
		}
		
	}



	private void determineBoxColor(Graphics g, int i, int j) {
		Box box = map.getBox(i, j);
		if (box instanceof GroundBox) {
			g.setColor(ColorData.GROUND_COLOR);
		} else if(box instanceof WaterBox) {
			g.setColor(ColorData.WATER_COLOR);
		}
	}

	private void init() {
		setLayout(new GridLayout(0,1));
	}
	
	

}

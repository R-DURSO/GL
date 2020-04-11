package GUI.components.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import GUI.drawing.ColorData;
import GUI.drawing.ImagesUtility;
import GUI.sub_panels.GamePanel;
import data.GameMap;
import data.Position;
import data.Power;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.boxes.WaterBox;
import data.building.Building;
import data.resource.ResourceTypes;
import data.unit.PhantomUnit;
import data.unit.Units;

public class MapPanel extends JPanel{
	private static final long serialVersionUID = -4989371043690170741L;
	private GamePanel gamePanel;
	
	private final int MINI_BOX_PART = 12;
	private GameMap map;
	private Power powers[];
	
	private int rectWidth;
	private int rectHeight;
	
	private Position fromPosition;
	private Position targetPosition;
	
	private ImagesUtility imagesUtility = new ImagesUtility();

	
	public MapPanel(GamePanel gamePanel, GameMap map, Power powers[]) {
		super();
		
		this.gamePanel = gamePanel;
		this.powers = powers;
		this.map = map;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		rectWidth = getWidth() / map.getSize();
		rectHeight = getHeight() / map.getSize();
		
		drawMap(g);
		
		if(fromPosition != null)
			drawFromPostion(g);
		
		if(targetPosition != null)
			drawTargetPostion(g);
	}
	


	private void drawMap(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());
        int miniBoxWidth = rectWidth * 4 / MINI_BOX_PART;
        int miniBoxHeight = rectHeight * 4 / MINI_BOX_PART;
        
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                int x = i * rectWidth;
                int y = j * rectHeight;
                //draw background of box 
                determineBoxColor(g2, i, j);
                g2.fillRect(x, y, rectWidth, rectHeight);
                
                //border color
                g2.setColor(ColorData.NO_POWER_COLOR);
                g2.drawRect(x, y, rectWidth, rectHeight);
                
                /*We draw resources, units, building here (just colored rectangles for now, except for Units)*/

                int startX = x + (rectWidth/MINI_BOX_PART);
                int startY = y + (rectHeight/MINI_BOX_PART);
                
                if(map.getBox(i, j) instanceof GroundBox) {
                	GroundBox groundBox = (GroundBox) map.getBox(i, j);
                	//we check if groundBox have resource
                	int resourceType = groundBox.getResourceType();
                	if(resourceType != ResourceTypes.NO_RESOURCE) {
						Image resourceImage = imagesUtility.getResourceImage(resourceType);
						g2.drawImage(resourceImage, startX, startY, miniBoxWidth, miniBoxHeight, null);
                	}
                	Building building = groundBox.getBuilding();
                	if(building != null) {
                		int drawX = startX + miniBoxWidth + miniBoxWidth/2;
                		int drawY = startY;
                		int buildingType = building.getType();
                		//get building image
                		Image buildingImage = imagesUtility.getBuildingImage(buildingType);
                		g2.drawImage(buildingImage, drawX, drawY, miniBoxWidth, miniBoxHeight * 5 / 2, null);
                		//we check if building is enabled or not, in order to change opacity in this case
                		if(building.getBuildTime() > 0) { //never null (checked in 'determineBuildingColor()')
                			//in this case, we simply draw a crossed white line to show his "state"
                			g2.setColor(Color.RED); //capital can't be disabled
                            g2.setStroke(new BasicStroke(2));
                			g2.drawLine(drawX, drawY, drawX + miniBoxWidth, drawY + miniBoxHeight * 5 / 2);
                			g2.setStroke(new BasicStroke(1));
                		}
                	}
                	
                }
                Box box = map.getBox(i, j);
                //if there is any unit to draw
                Units units = box.getUnit();
                if(units != null && !(units instanceof PhantomUnit)) {
                	int drawX = startX;
                	int drawY = startY + miniBoxHeight + miniBoxHeight/2;
                	Image unitImage = imagesUtility.getUnitsImage(units.getTypes());
            		g2.drawImage(unitImage, drawX, drawY, miniBoxWidth, miniBoxHeight, null);
            		//we need to know who have those units (if units are on ally's territory)
            		determineBoxBorder(g2, units.getOwner());
            		g2.setStroke(new BasicStroke(3));
            		g2.drawRect(drawX, drawY, miniBoxWidth, miniBoxHeight);
            		g2.setStroke(new BasicStroke(1));
            	}
                
            }
        }
        //we draw "box belonging" here (to avoid an overdraw with boxes under)
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                int x = i * rectWidth;
                int y = j * rectHeight;
                Box box = map.getBox(i,j);
                Power power = box.getOwner();
                determineBoxBorder(g2, power);
                g2.setStroke(new BasicStroke(3));
                if(g2.getColor() != ColorData.NO_POWER_COLOR)
                	g2.drawRect(x, y, rectWidth, rectHeight);
                
            }
        }  
	}

	private void drawFromPostion(Graphics g) {
		g.setColor(ColorData.FROM_SELECTION_COLOR);
		drawSelectionRect(g, fromPosition);
	}
	
	private void drawTargetPostion(Graphics g) {
		g.setColor(ColorData.TARGET_SELECTION_COLOR);
		drawSelectionRect(g, targetPosition);
	}
	
	private void drawSelectionRect(Graphics g, Position position) {
		int posX = position.getX() * rectWidth;
		int posY = position.getY() * rectHeight;
		g.fillRect(posX, posY, rectWidth, rectHeight);
	}
	
	/*
     * Border Color:
     * 	Each color represents a belonging
     *		-Black : no one
     *		-Red : player 1
     *		-Yellow : player 2
     *		-Green : player 3
     *		-White : player 4*/
	private void determineBoxBorder(Graphics g, Power power) {
		if(power != null) {
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

	public void refreshSelection(Position fromPosition, Position targetPosition) {
		this.fromPosition = fromPosition;
		this.targetPosition = targetPosition;
		repaint();
	}
	

	public Box getBoxFromCoordinates(int x, int y) {
		Position position = getPositionFromCoordinates(x, y);
		return map.getBox(position.getX(), position.getY());
	}

	
	public Position getPositionFromCoordinates(int x, int y) {
		int w = getWidth() / map.getSize();
		int h = getHeight() / map.getSize();
		int xMapRelative = x  / w;
		int yMapRelative = y  / h;
		
		//sometimes, map will be a little bit smaller than panel, so mouse can be out of the map
		//and therefore, bugs can occur, so we check if mouse is still on map to avoid those problems
		if(xMapRelative >= map.getSize())
			xMapRelative = map.getSize() - 1;
		if(yMapRelative >= map.getSize())
			yMapRelative = map.getSize() - 1;
		
		return new Position(xMapRelative, yMapRelative);
	}
	
	public Position getPositionFrom() {
		return fromPosition ;
	}
	
	public Position getPositionTarget() {
		return targetPosition;
	}
	
	public Box getBoxByPosition(Position pos) {
		return map.getBox(pos);
	}
	
	public Box getBox(int x, int y) {
		return map.getBox(x, y);
	}
	
	public int getMapSize() {
		return map.getSize();
	}

}

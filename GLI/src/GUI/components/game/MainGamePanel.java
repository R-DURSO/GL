package GUI.components.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import GUI.colors.ColorData;
import data.GameMap;
import data.Power;
import data.boxes.Box;
import data.boxes.GroundBox;
import data.boxes.WaterBox;
import data.building.Building;
import data.building.BuildingTypes;
import data.resource.ResourceTypes;
import data.unit.UnitTypes;
import data.unit.Units;
import process.management.BuildingManager;
import process.management.UnitManager;

public class MainGamePanel extends JPanel{
	private static final long serialVersionUID = -4989371043690170741L;
	private final int MINI_BOX_PART = 12;
	private GameMap map;
	private Power powers[];
	
	public MainGamePanel(GameMap map, Power powers[]) {
		super();
		
		this.powers = powers;
		this.map = map;
		//montre affichage units sur cases
		UnitManager.getInstance().addUnits(powers[0], map.getBox(1, 0), UnitTypes.UNIT_INFANTRY, 50);
		//montrre affihage batiments sur case
		BuildingManager.getInstance().addNewBuilding(powers[0], BuildingTypes.BUILDING_SAWMILL, (GroundBox) map.getBox(1, 0));
	}
	
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawMap(g);
		
	}
	


	private void drawMap(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        int rectWidth = getWidth() / map.getSize();
        int rectHeight = getHeight() / map.getSize();
        int miniBoxWidth = rectWidth * 4 / MINI_BOX_PART;
        int miniBoxHeight = rectHeight * 4 / MINI_BOX_PART;
        
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                int x = i * rectWidth;
                int y = j * rectHeight;
                //draw background of box 
                determineBoxColor(g, i, j);
                g.fillRect(x, y, rectWidth, rectHeight);
                
                //border color
                g.setColor(ColorData.NO_POWER_COLOR);
                g.drawRect(x, y, rectWidth, rectHeight);
                
                /*We draw resources, units, building here (just colored rectangles for now*/

                int startX = x + (rectWidth/MINI_BOX_PART);
                int startY = y + (rectHeight/MINI_BOX_PART);
                
                if(map.getBox(i, j) instanceof GroundBox) {
                	GroundBox groundBox = (GroundBox) map.getBox(i, j);
                	if(determineResourceColor(g, groundBox)) {
						g.fillRect(startX, startY, miniBoxWidth, miniBoxHeight);
                	}
                	if(determineBuildingColor(g, groundBox)) {
                		System.out.println(g.getColor());
                		g.fillRect( startX, startY + miniBoxHeight + miniBoxHeight/2, miniBoxWidth, miniBoxHeight);
                	}
                	
                }
                Box box = map.getBox(i, j);
                if(determineUnitColor(g, box)) {
            		g.fillRect(startX + miniBoxWidth + miniBoxWidth/2, startY + miniBoxHeight + miniBoxHeight/2, miniBoxWidth, miniBoxHeight);
            	}
                
            }
        }
        //we draw "box belonging" here (to avoid an overdraw with boxes under)
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                int x = i * rectWidth;
                int y = j * rectHeight;
                
                determineBoxBorder(g, i, j);
                if(g.getColor() != ColorData.NO_POWER_COLOR)
                	g.drawRect(x, y, rectWidth, rectHeight);
                
            }
        }
        
        
	}

	private boolean determineBuildingColor(Graphics g, GroundBox groundBox) {
		Building building = groundBox.getBuilding();
		if(building == null)
			return false;
		
		switch(building.getType()) {
		case BuildingTypes.BUILDING_BARRACK:
			g.setColor(ColorData.BARRACK_COLOR);
			break;
		case BuildingTypes.BUILDING_DOCK:
			g.setColor(ColorData.DOCK_COLOR);
			break;
		case BuildingTypes.BUILDING_WORKSHOP:
			g.setColor(ColorData.WORKSHOP_COLOR);
			break;
		case BuildingTypes.BUILDING_MINE:
			g.setColor(ColorData.MINE_COLOR);
			break;
		case BuildingTypes.BUILDING_QUARRY:
			g.setColor(ColorData.QUARRY_COLOR);
			break;
		case BuildingTypes.BUILDING_SAWMILL:
			g.setColor(ColorData.SAWMILL_COLOR);
			break;
		case BuildingTypes.BUILDING_WINDMILL:
			g.setColor(ColorData.WINDMILL_COLOR);
			break;
		case BuildingTypes.BUILDING_CAPITAL:
			g.setColor(ColorData.CAPITAL_COLOR);
			break;
		case BuildingTypes.BUILDING_DOOR:
			g.setColor(ColorData.DOOR_COLOR);
			break;
		case BuildingTypes.BUILDING_TEMPLE:
			g.setColor(ColorData.TEMPLE_COLOR);
			break;
		case BuildingTypes.BUILDING_WALL:
			g.setColor(ColorData.WALL_COLOR);
			break;
		}
		return true;
	}



	private boolean determineUnitColor(Graphics g, Box box) {
		Units units = box.getUnit();
		if(units == null)
			return false;
		
		switch(units.getTypes()) {
		case UnitTypes.UNIT_ARCHER:
			g.setColor(ColorData.ARCHER_COLOR);
			break;
		case UnitTypes.UNIT_BATTERING_RAM:
			g.setColor(ColorData.BATTERING_RAM_COLOR);
			break;
		case UnitTypes.UNIT_BOAT:
			g.setColor(ColorData.BOAT_COLOR);
			break;
		case UnitTypes.UNIT_CAVALRY:
			g.setColor(ColorData.CAVALRY_COLOR);
			break;
		case UnitTypes.UNIT_INFANTRY:
			g.setColor(ColorData.INFANTRY_COLOR);
			break;
		case UnitTypes.UNIT_TREBUCHET:
			g.setColor(ColorData.TREBUCHET_COLOR);
			break;
		case UnitTypes.UNIT_PIKEMAN:
			g.setColor(ColorData.PIKEMAN_COLOR);
			break;
		}
		return true;
	}



	private boolean determineResourceColor(Graphics g, GroundBox gBox) {
		switch (gBox.getResourceType()) {
		case ResourceTypes.RESOURCE_FOOD:
			g.setColor(ColorData.FOOD_COLOR);
			break;
		case ResourceTypes.RESOURCE_GOLD:
			g.setColor(ColorData.GOLD_COLOR);
			break;
		case ResourceTypes.RESOURCE_STONE:
			g.setColor(ColorData.STONE_COLOR);
			break;
		case ResourceTypes.RESOURCE_WOOD:
			g.setColor(ColorData.WOOD_COLOR);
			break;
		default:
			return false;
		}
		return true;
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

}

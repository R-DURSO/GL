package process.management;
import java.util.Iterator;
import java.awt.Point;
import java.util.*;
import java.util.Random;

import data.Map;
import data.boxes.*;
import data.ressource.ResourceTypes;

/**
 * Builder based on the Conway's game of life
 * @author Aldric Vitali Silvestre
 *
 */
public class MapBuilder {
	private final int SCALE = 5;
	private int size;
	private Random random = new Random();
	private boolean map[][];
	
	
	public MapBuilder(int size, int waterAmount) {
		this.size = size;
		map = new boolean[size][size];
		int nbWaterPoints = size*size * waterAmount /7 / 100;
		
		int minRand = size / SCALE;
		int maxRand = size - minRand;
		
		int randX, randY;
		
		
		for (int i = 0; i < nbWaterPoints; i++) {
			randX = random.nextInt(maxRand - minRand) + minRand;
			randY = random.nextInt(maxRand - minRand) + minRand;
			setRandomPatternWater(randX, randY);
		}
		
		map[size/2][size/2] = false;
	}
	
	
	
	private void setRandomPatternWater(int randX, int randY) {
		switch (random.nextInt(3)) {
		case 0:
			makeCrossWater(randX, randY);
			break;
		case 1:
			makeCircleWater(randX, randY);
			break;
		case 2:
			makeSquareWater(randX, randY);
			break;
		default:
			break;
		}
		
	}



	private void makeSquareWater(int randX, int randY) {
		int x,y;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				x = randX + i;
				y = randY + j;
				map[x][y] = true;
			}
		}
		
	}



	private void makeCircleWater(int randX, int randY) {
		makeSquareWater(randX, randY);
		map[randX + 2][randY] = true;
		map[randX - 2][randY] = true;
		map[randX][randY + 2] = true;
		map[randX][randY - 2] = true;
	}



	private void makeCrossWater(int randX, int randY) {
		
		map[randX][randY] = true;
		map[randX+1][randY] = true;
		map[randX-1][randY] = true;
		map[randX][randY+1] = true;
		map[randX][randY-1] = true;
		
	}



	public Map buildMap() {
		data.boxes.Box[][] boxes = new Box[size][size];
		Box box;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if (map[i][j]) {
					box = new WaterBox();
				}else {
					box = new GroundBox(defineRessourceType());
				}
				boxes[i][j] = box;
			}
		}
		
		return new Map(boxes);
	}

	/*Non définitif*/
	private int defineRessourceType() {
		return random.nextInt(ResourceTypes.NUMBER_TYPE_RESOURCES);
	}



	public void displayMap() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(map[i][j] ? "~ " : "# ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		MapBuilder mb = new MapBuilder(20, 20);
		mb.buildMap();
		mb.displayMap();
	}

}

package process.management;

import java.util.Random;

import data.GameMap;
import data.Position;
import data.Power;
import data.boxes.*;
import data.building.special.Capital;
import data.resource.ResourceTypes;

/**
 * Map generator
 * @author Aldric Vitali Silvestre
 *
 */
public class MapBuilder {
	private final int SCALE = 5;
	private int size;
	private Random random = new Random();
	private boolean map[][];
	private Power powers[];
	
	
	public MapBuilder(int size, int waterAmount, Power powers[]) {
		this.powers = powers;
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
		
		makeSquareGround(size/2, size/2);
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

	private void makeSquareGround(int randX, int randY) {
		int x,y;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				x = randX + i;
				y = randY + j;
				map[x][y] = false;
			}
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


	public GameMap buildMap() {
		Box[][] boxes = new Box[size][size];
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
		//'install' powers 
		installPowers(boxes);
		return new GameMap(boxes);
	}

	private void installPowers(Box boxes[][]) {
		//player 1 on top-left side
		establishInitialTerritory(powers[0], new Position(0,0), boxes);
		//player 2 on bottom-right side
		establishInitialTerritory(powers[1], new Position(size-1, size-1), boxes);
		//installCapital(powers[1], boxes[size - 1][size - 1]);
		if (powers.length > 2) {
			//player 3 (if exists) on bottom-left side
			//installCapital(powers[2], boxes[0][size - 1]);
			establishInitialTerritory(powers[2], new Position(0, size-1), boxes);
		}
		if (powers.length > 3) {
			//player 4 (if exists) on top-right side
			//installCapital(powers[3], boxes[size - 1][0]);
			establishInitialTerritory(powers[3], new Position(size-1, 0), boxes);
		}
	}



	private void establishInitialTerritory(Power power, Position position, Box[][] boxes) {
		Box currentBox;
		installCapital(power, boxes[position.getX()][position.getY()]);
		for(int i = position.getX() - 1; i <= position.getX() + 1; i++){
			for(int j = position.getY() - 1; j <= position.getY() + 1; j++){
				if(i >= 0 && i < size && j >= 0 && j < size) {
					currentBox = boxes[i][j];
				    currentBox.setOwner(power);
				    power.addBox(currentBox);
				}
			}
		}
	}


	private void installCapital(Power power, Box box) {
		//We are sure that this box is a grounded box (because specified it before)
		box.setOwner(power);
		((GroundBox) box).setBuilding(new Capital());
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
}

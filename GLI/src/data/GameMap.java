package data;

import java.io.Serializable;

import data.boxes.*;

/**
 * Implementation of the Map used for the {@link process.game.GameLoop Game}.
 * <br>Use an Array of {@link data.boxes.Box Boxes}.
 */
public class GameMap implements Serializable {
	/*you have to be careful: the first entry is that of the ordinate, 
	and the second that of the abscissa*/
	private Box boxes[][]; 
	private Power powers[];
	
	public GameMap(Box boxes[][], Power powers[]) {
		this.boxes = boxes;
		this.powers = powers;
	}
	
	public int getSize() {
		return boxes.length;
	}
	
	public Box getBox(int positionX, int positionY){
		//abscissa and ordinate inverted
		return boxes[positionY][positionX];
	}
	
	public Box getBox(Position position) {
		if (position != null) {
			return boxes[position.getY()][position.getX()];
		}
		return null;
	}
	
	public Box[][] getBoxes(){
		return boxes;
	}
	
	public Power[] getPowers(){
		return powers;
	}

	public Position getLeftPos(Position position) {
		if ((position.getX()-1) >= 0) {
			return new Position(position.getX()-1,position.getY());
		}
		else {
			return null;
		}
	}
	
	public Position getRightPos(Position position) {
		if ((position.getX()+1) < getSize()) {
			return new Position(position.getX()+1,position.getY());
		}
		else {
			return null;
		}
	}
	
	public Position getUpPos(Position position) {
		if ((position.getY()-1) >= 0) {
			return new Position(position.getX(),position.getY()-1);
		}
		else {
			return null;
		}
	}
	
	public Position getDownPos(Position position) {
		if ((position.getY()+1) < getSize()) {
			return new Position(position.getX(),position.getY()+1);
		}
		else {
			return null;
		}
	}
	
	/**
	 * check if nearby Boxes are made of Water
	 * @param target
	 * @return true if target is near Water
	 */
	public boolean isNearWater(Position target) {
		Box wBox = null;
		for (int d=0 ; d<=4 ; d++) {
			switch(d) {
			case 0:
				wBox = getBox(target);
				break;
			case 1:
				wBox = getBox(getUpPos(target));
				break;
			case 2:
				wBox = getBox(getLeftPos(target));
				break;
			case 3:
				wBox = getBox(getRightPos(target));
				break;
			case 4:
				wBox = getBox(getDownPos(target));
				break;
			default:
				wBox = null;
				break;
			}
			if (wBox != null) {
				if (wBox instanceof WaterBox) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Return the distance between 2 Boxes on the Map
	 * @param a first Position
	 * @param b second Position
	 * @return the distance between those two
	 */
	public int getDistance (Position a, Position b) {
		 int aX = a.getX();
		 int aY = a.getY();
		 int bX = b.getX();
		 int bY = b.getY();
		return (getDifference(aX,bX) + getDifference(aY,bY));
	}
	
	/**
	 * @param a Entier
	 * @param b Entier
	 * @return La difference entre ces 2 entiers
	 */
	private int getDifference (int a, int b) {
		return Math.abs(a - b);
	}
	
	public String toString() {
		String result = "\nMap Boxes:\n\n";
		for(int i = 0; i < boxes.length; i++){
			for (int j = 0; j < boxes[0].length; j++) {
				result += getBox(i, j).toString();
			}
			result += "\n";
		}
		return result;
	}

}

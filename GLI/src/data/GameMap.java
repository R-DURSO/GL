package data;

import java.io.Serializable;

import data.boxes.*;

public class GameMap implements Serializable {
	/*you have to be careful: the first entry is that of the ordinate, 
	and the second that of the abscissa*/
	private Box boxes[][]; 
	
	public GameMap(Box boxes[][]) {
		this.boxes = boxes;
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

package data;

import data.boxes.*;

public class GameMap {
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
	
	public Box getBox(Position position){
		return boxes[position.getY()][position.getX()];
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

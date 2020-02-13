package data;

import data.boxes.*;

public class Map {

	private Box boxes[][]; 
	
	public Map(Box boxes[][]) {
		this.boxes = boxes;
	}
	
	public Box getBox(int positionX, int positionY){
		return boxes[positionX][positionY];
	}
	
	@Override
	public String toString() {
		String result = "Map Boxes:\n";
		for(int i = 0; i < boxes.length; i++){
			for (int j = 0; j < boxes[0].length; j++) {
				result += getBox(i, j).toString();
			}
			result += "\n";
		}
		return result;
	}

}

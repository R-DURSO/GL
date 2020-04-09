package data;

import java.io.Serializable;

public class Position implements Serializable {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Position() {
		this(0, 0);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object object) throws IllegalArgumentException{
		if (this == object) {
			return true;
		} else if (object != null) {
			if (object instanceof Position) {
				Position position = (Position) object;
				//Two Positions are equal when they have the same coordinates.
				return position.getX() == x && position.getY() == y;
			}
		}
		return false;
	}

}

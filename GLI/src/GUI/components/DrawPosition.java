package GUI.components;

public class DrawPosition {
	private int x;
	private int y;
	private int scale;
	
	public DrawPosition(int x, int y, int scale) {
		super();
		this.x = x;
		this.y = y;
		this.scale = scale;
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
	
	public void moveRight() {
		x += scale;
	}
	
	public void moveLeft() {
		x -= scale;
	}
	
	public void moveUp() {
		y -= scale;
	}
	
	public void moveDown() {
		y += scale;
	}

	public int getScale() {
		return scale;
	}
}

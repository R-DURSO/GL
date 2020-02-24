package test;

public class CircleTest {

	public static void main(String[] args) {
		boolean tab[][] = new boolean[20][20];
		drawDiamond(tab, 10, 10, 4);
		displayCircle(tab);
	}
	
	public static void displayCircle(boolean[][] tab) {
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[0].length; j++) {
				if(tab[i][j])
					System.out.print("#");
				else
					System.out.print("-");
			}
			System.out.print("\n");
		}
	}

	public static void drawCircle(boolean tab[][], int cX, int cY, int radius) {
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[0].length; j++) {
				if(( Math.pow((i - cX), 2) + Math.pow((j - cY), 2) <= Math.pow(radius,2))) {
					tab[i][j] = true;
				}
			}
		}
	}
	
	/**
	 * @param tab
	 * @param cX
	 * @param cY
	 * @param radius
	 * 
	 * On dessine 2 triangles, un au-dessus, un en-dessous
	 * Cela forme notre losange de portée, et si le point se trouve dans l'un des triangles alors,
	 * le point est dans le losange
	 */
	public static void drawTriangle(boolean tab[][], int cX, int cY, int radius) { 
		int ax = cX - radius;
		int ay = cY;
		int bx = cX;
		int by = cY - radius;
		int byPrime = cY + radius;
		int cx = cX + radius;
		int cy = cY;
		
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[0].length; j++) {
				if(isInside(i, j, ax, ay, bx, by, cx, cy)) {
					tab[i][j] = true;
				}
				if(isInside(i, j, ax, ay, bx, byPrime, cx, cy)) {
					tab[i][j] = true;
				}
			}
		}
		
		
		
		
	}
	
	private static double area(int ax, int ay, int bx, int by, int cx, int cy) {
		return Math.abs((ax*(by-cy) + bx*(cy-ay) + cx*(ay-by) ) / 2.0);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param ax
	 * @param ay
	 * @param bx
	 * @param by
	 * @param cx
	 * @param cy
	 * 
	 * Soit 4points, si l'aire du triangle de 3 points est égal à la somme des aires des 3 triangles contenant le point recherché
	 * Alors, le point se trouve dans le triangle
	 */
	private static boolean isInside(int x, int y, int ax, int ay, int bx, int by, int cx, int cy) {
		double A = area(ax, ay, bx, by, cx, cy);
		double A1 = area(x, y, bx, by, cx, cy);
		double A2 = area(ax, ay, x, y, cx, cy);
		double A3 = area(ax, ay, bx, by, x, y);
		
		return (A == A1 + A2 + A3);
	}
	
	public static void drawCircle2(boolean tab[][], int cX, int cY, int radius) {
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[0].length; j++) {
				int dist = (int)Math.floor(Math.sqrt(( Math.pow(i - cX, 2) + Math.pow(j - cY, 2) )));
				if(dist <= radius) {
					System.out.println(i + " et " + j);
					tab[i][j] = true; 
				}
			}
		}
	}
	
	/**
	 * @param tab
	 * @param cX
	 * @param cY
	 * @param radius
	 * Dessine tout autour du point, pour un radius donné
	 */
	public static void drawDiamond(boolean tab[][], int cX, int cY, int radius) {
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[0].length; j++) {
				int dist = Math.abs(i - cX) + Math.abs(j - cY);
				if(dist <= radius) {
					System.out.println(i + " et " + j);
					tab[i][j] = true; 
				}
			}
		}
	}

}

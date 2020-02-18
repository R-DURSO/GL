package test;

import data.GameMap;
import data.Power;
import process.management.MapBuilder;
import process.management.PowerFactory;

public class MapBuilderTest {

	public static void main(String[] args) {
		int nbJoueur = 4;
		Power powers[] = new Power[nbJoueur];
		for(int i = 0; i < nbJoueur; i++) {
			powers[i] = PowerFactory.createPower("Joueur " + (i + 1));
		}
		MapBuilder mb = new MapBuilder(20, 20, powers);
		mb.displayMap();
		GameMap map = mb.buildMap();
		
		System.out.println(map);
	}

}

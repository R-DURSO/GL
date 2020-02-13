package data;

import java.util.ArrayList;
import java.util.HashMap;

import data.boxes.Box;
import data.ressource.*;;

public class Power {
	private String name;
	private ArrayList<Box> territory;
	//Transformer l'ArrayListe en HashMap<Position,Case> ????
	private HashMap<String,Resource> ressource;
	//sinon private Ressource[] ressources = new Ressource[6];
	
	public Power(String name) {
		this.name = name;
	}

}

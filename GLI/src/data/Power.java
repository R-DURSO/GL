package data;

import java.util.ArrayList;
import java.util.HashMap;

import data.boxes.Box;
import data.ressource.*;;

public class Power {
	private String name;
	private ArrayList<Box> territory = new ArrayList<>();
	private Resource[] ressources = new Resource[6];
	private Power allied;
	
	public Power(String name) {
		this.name = name;
	}

}

package data;

import java.util.ArrayList;
import java.util.HashMap;

import data.boxes.Box;
import data.ressource.*;;

public class Power {
	private String name;
	private ArrayList<Box> territory = new ArrayList<>();
	private Resource[] ressources;
	private Power allied;
	
	public Power(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setResource(Resource[] resources) {
		this.ressources = resources;
	}
	
	public Resource[] getResource() {
		return ressources;
	}
	
	public void setAllied(Power allied) {
		this.allied = allied;
	}
	
	public Power getAllied() {
		return allied;
	}
	public void addBox(Box b) {
		this.territory.add(b);
	}
	
	public ArrayList<Box> getTerritory() {
		return territory;
	}
}

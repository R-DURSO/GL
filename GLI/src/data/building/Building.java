package data.building;

import data.Power;
import process.visitor.building_visitor.BuildingVisitor;

public abstract class Building {
	
	private int buildTime; //when buildTime == 0, this building can be used
	private int health;
	
	public Building(int buildTime, int health) {
		this.buildTime = buildTime;
		this.health = health;
	}
	
	public abstract <B> B accept(BuildingVisitor<B> visitor);
}
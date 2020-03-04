package data.building;

import data.Power;

public abstract class Building {
	
	private int buildTime; //when buildTime == 0, this building can be used
	private int health;
	
	public Building(int buildTime, int health) {
		this.buildTime = buildTime;
		this.health = health;
	}
		
	public int getBuildTime() {
		return buildTime;
	}

	public int getHealth() {
		return health;
	}
	
	public abstract int getType();

	public void decreaseBuildTime() {
		buildTime--;
	}

	public void applyDamage(int damage) {
		this.health -= damage;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
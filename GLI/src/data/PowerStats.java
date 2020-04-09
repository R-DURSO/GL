package data;

import java.io.Serializable;

/**
 * Storage class that get power stats
 * @author Aldric Vitali Silvestre
 *
 */
public class PowerStats implements Serializable {
	
	private int numberUnits;
	private int numberBuildings;
	private int territorySize;
	
	public PowerStats(int numberUnits, int numberBuildings, int territorySize) {
		super();
		this.numberUnits = numberUnits;
		this.numberBuildings = numberBuildings;
		this.territorySize = territorySize;
	}
	
	public PowerStats() {
		this(0, 0, 0);
	}

	public int getNumberUnits() {
		return numberUnits;
	}

	public int getNumberBuildings() {
		return numberBuildings;
	}

	public int getTerritorySize() {
		return territorySize;
	}

	public void setNumberUnits(int numberUnits) {
		this.numberUnits = numberUnits;
	}

	public void setNumberBuildings(int numberBuildings) {
		this.numberBuildings = numberBuildings;
	}

	public void setTerritorySize(int territorySize) {
		this.territorySize = territorySize;
	}
	
	public void addNumberUnits(int numberUnits) {
		this.numberUnits += numberUnits;
	}
	
	public void numberBuildingsPlus1() {
		numberBuildings++;
	}
	
	public void territorySizePlus1() {
		territorySize++;
	}
}

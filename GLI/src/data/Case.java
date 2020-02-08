package data;

import data.building.Building;
import data.unit.Units;

public class Case {
	private String ressource;
	// TODO �a ferait mieux quand m�me de mettre un attribut de type Ressoure, qui permettrait de faire un Visitor dessus
	private Building building;
	private Units unit;
	private Power owner;
	//mettre ici la position ?
	
	public Case(String ressource, Building building, Power owner) {
		this.ressource = ressource;
		this.building = building; //pas de batiment au d�but, sauf pour la capitale et le temple
		this.owner = owner; //encore une fois, au d�but chacun aura une case (celle de la capitale)
		this.unit = null; // la s�r, il n'y aura pas d'unit�s au d�but
	}

}

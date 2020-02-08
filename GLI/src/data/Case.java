package data;

import data.building.Building;
import data.unit.Units;

public class Case {
	private String ressource;
	// TODO ça ferait mieux quand même de mettre un attribut de type Ressoure, qui permettrait de faire un Visitor dessus
	private Building building;
	private Units unit;
	private Power owner;
	//mettre ici la position ?
	
	public Case(String ressource, Building building, Power owner) {
		this.ressource = ressource;
		this.building = building; //pas de batiment au début, sauf pour la capitale et le temple
		this.owner = owner; //encore une fois, au début chacun aura une case (celle de la capitale)
		this.unit = null; // la sûr, il n'y aura pas d'unités au début
	}

}

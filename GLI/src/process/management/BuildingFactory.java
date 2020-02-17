package process.management;
import data.boxes.GroundBox;
import data.building.Building;
import data.building.army.*;

import data.building.product.*;
import data.building.special.*;
import data.Power;
public class BuildingFactory {

	public BuildingFactory(Power name, int type, GroundBox b) {
		boolean result;
		Building r ;
		result =constructableCase( b , name.getName());
		r=construction(result, type);
		b.setBuilding(r);
	}
	
	public  boolean  constructableCase(GroundBox b, String name) {
		if(b.getBuilding()!= null && b.getOwner().equals(name)) {
			return false;			
		}else {
			return true;
		}
	}
	public Building construction(boolean r, int cons) {
		if(r==true) {
			switch(cons) {
			case 1:
				Building b = new Barrack();
				return b;
			case 2 :
				Building a= new Stable();
				return a;
			case 3:
				Building c = new Dock();
				return c;
			case 4:
				Building e = new Mine();
				return e;
			case 5:
				Building f = new Sawmill();
				return f;
			case 6:
				Building  g = new Windmill();
				return g;
			case 7:
				Building h = new Door();
				return h;
			case 8:
				Building i= new Temple();
				return i;
			case 9:
				Building j= new Wall();
				return j;
			}
			
		}
		return null;
	}
}

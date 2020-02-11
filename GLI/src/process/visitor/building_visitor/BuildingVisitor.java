package process.visitor.building_visitor;

import data.building.army.Barrack;
import data.building.army.Dock;
import data.building.army.Stable;
import data.building.product.Mine;
import data.building.product.Sawmill;
import data.building.product.Windmill;
import data.building.special.Capital;
import data.building.special.Door;
import data.building.special.Temple;
import data.building.special.Wall;

public interface BuildingVisitor<T> {

	T visit(Barrack node);
	
	T visit(Dock node);
	
	T visit(Stable node);
	
	T visit(Mine node);
	
	T visit(Sawmill node);

	T visit(Windmill node);
	
	T visit(Capital node);
	
	T visit(Door node);
	
	T visit(Temple node);
	
	T visit(Wall node);
}

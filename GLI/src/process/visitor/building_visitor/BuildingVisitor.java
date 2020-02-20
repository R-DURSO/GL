package process.visitor.building_visitor;

import data.building.army.*;
import data.building.product.*;
import data.building.special.*;

public interface BuildingVisitor<T> {

	T visit(Barrack node);
	
	T visit(Dock node);
	
	T visit(Workshop node);
	
	T visit(Mine node);
	
	T visit(Quarry node);
	
	T visit(Sawmill node);

	T visit(Windmill node);
	
	T visit(Capital node);
	
	T visit(Door node);
	
	T visit(Temple node);
	
	T visit(Wall node);
}

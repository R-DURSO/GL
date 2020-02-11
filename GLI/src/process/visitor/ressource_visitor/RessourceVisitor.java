package process.visitor.ressource_visitor;

import data.ressource.Action;
import data.ressource.Food;
import data.ressource.Gold;
import data.ressource.Score;
import data.ressource.Stone;
import data.ressource.Wood;

public interface RessourceVisitor<T> {

	T visit(Action node);
	
	T visit(Food node);
	
	T visit(Gold node);
	
	T visit(Score node);
	
	T visit(Stone node);
	
	T visit(Wood node);

}

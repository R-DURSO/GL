package process.visitor.ressource_visitor;

import data.resource.ActionPoints;
import data.resource.Food;
import data.resource.Gold;
import data.resource.Score;
import data.resource.Stone;
import data.resource.Wood;

public interface RessourceVisitor<T> {

	T visit(ActionPoints node);
	
	T visit(Food node);
	
	T visit(Gold node);
	
	T visit(Score node);
	
	T visit(Stone node);
	
	T visit(Wood node);

}

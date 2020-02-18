package process.visitor.ressource_visitor;

import data.resource.ActionPoints;
import data.resource.Food;
import data.resource.Gold;
import data.resource.Score;
import data.resource.Stone;
import data.resource.Wood;

/**
 * Useless visitor for the project, just for testing visitor implementation
 * Show ressource type and amount
 * @author Aldric Vitali Silvestre
 *
 */
public class TestVisitor implements RessourceVisitor<Void>{

	@Override
	public Void visit(ActionPoints node) {
		System.out.println("This is Action ressource. Value = "+ node.getAmount());
		return null;
	}

	@Override
	public Void visit(Food node) {
		System.out.println("This is Food ressource. Value = " + node.getAmount());
		return null;
	}

	@Override
	public Void visit(Gold node) {
		System.out.println("This is Gold ressource. Value = " + node.getAmount());
		return null;
	}

	@Override
	public Void visit(Score node) {
		System.out.println("This is Score ressource. Value = " + node.getAmount());
		return null;
	}

	@Override
	public Void visit(Stone node) {
		System.out.println("This is Stone ressource. Value = " + node.getAmount());
		return null;
	}

	@Override
	public Void visit(Wood node) {
		System.out.println("This is Wood ressource. Value = " + node.getAmount());
		return null;
	}

}

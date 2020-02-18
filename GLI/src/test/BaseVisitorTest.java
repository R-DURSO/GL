package test;

import data.resource.Gold;
import data.resource.Wood;
import process.visitor.ressource_visitor.RessourceVisitor;
import process.visitor.ressource_visitor.TestVisitor;


/**
 * Testing visitor only class
 * 
 * @author Aldric Vitali Silvestre
 *
 */
public class BaseVisitorTest {
	
	public static void main(String[] args) {
		Gold gold = new Gold();
		gold.addValue(80);
		
		Wood wood = new Wood();
		wood.addValue(150);
		
		RessourceVisitor<Void> visitor = new TestVisitor();
		gold.accept(visitor);
		wood.accept(visitor);
		
		
	}

}

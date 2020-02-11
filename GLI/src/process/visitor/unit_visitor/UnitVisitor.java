package process.visitor.unit_visitor;

import data.unit.Archer;
import data.unit.BatteringRam;
import data.unit.Boat;
import data.unit.Cavalry;
import data.unit.Infantry;
import data.unit.Pikeman;
import data.unit.Trebuchet;

/**
 * Generic Unit visitor 
 * 
 * @author Aldric Vitali Silvestre
 *
 */
public interface UnitVisitor<T> {
	
	T visit(BatteringRam node);
	
	T visit(Boat node);
	
	T visit(Cavalry node);
	
	T visit(Infantry node);
	
	T visit(Pikeman node);
	
	T visit(Trebuchet node);
	
	T visit(Archer node);

}

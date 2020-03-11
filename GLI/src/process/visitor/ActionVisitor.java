package process.visitor;

import data.actions.ActionAttack;
import data.actions.ActionBreakAlliance;
import data.actions.ActionConstruct;
import data.actions.ActionCreateUnit;
import data.actions.ActionDestroyBuilding;
import data.actions.ActionDestroyUnits;
import data.actions.ActionMakeAlliance;
import data.actions.ActionMove;
import data.actions.ActionUpgradeCapital;

public interface ActionVisitor<A> {
	
	A visit(ActionAttack action);
	
	A visit(ActionBreakAlliance action);
	
	A visit(ActionConstruct action);
	
	A visit(ActionCreateUnit action);
	
	A visit(ActionDestroyBuilding action);
	
	A visit(ActionDestroyUnits action);
	
	A visit(ActionMakeAlliance action);
	
	A visit(ActionMove action);
	
	A visit(ActionUpgradeCapital action);
}

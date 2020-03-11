package process.visitor;

import data.Position;
import data.Power;
import data.actions.ActionAttack;
import data.actions.ActionBreakAlliance;
import data.actions.ActionConstruct;
import data.actions.ActionCreateUnit;
import data.actions.ActionDestroyBuilding;
import data.actions.ActionDestroyUnits;
import data.actions.ActionMakeAlliance;
import data.actions.ActionMove;
import data.actions.ActionUpgradeCapital;
import process.management.UnitManager;

public class ExcecuteActionVisitor implements ActionVisitor<Void>{

	@Override
	public Void visit(ActionAttack action) {
		Power powerConcerned = action.getPowerConcerned();
		Position from = action.getFrom();
		Position target = action.getTarget();
		//UnitManager.getInstance().attackUnits(powerConcerned, from, target);
		return null;
	}

	@Override
	public Void visit(ActionBreakAlliance action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionConstruct action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionCreateUnit action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionDestroyBuilding action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionDestroyUnits action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionMakeAlliance action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionMove action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ActionUpgradeCapital action) {
		// TODO Auto-generated method stub
		return null;
	}

}

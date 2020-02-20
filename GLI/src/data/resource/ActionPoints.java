package data.resource;

import process.visitor.ressource_visitor.RessourceVisitor;

public class ActionPoints extends Resource {
	private int Action_Number = 3; 
	private static final int Action_Max = 5 ; 
	int variation () {
		return 0;
	}
	public int GetAction_Number() {
		return Action_Number;
	}
	@Override
	public <R> R accept(RessourceVisitor<R> visitor) {
		return visitor.visit(this);
	}
}

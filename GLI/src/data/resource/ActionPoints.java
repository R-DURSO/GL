package data.resource;


/**
 * <p>ActionPoints represents the number of action a player can do each turn.</p>
 * <p>Useable Action can be seen in "data.actions"</p>
 * @author Maxence HENNEKEIN
 */

public class ActionPoints extends Resource {
//	private static final int ACTIONS_NUMBER= 3;
//	private static final int MAX_ACTIONS = 6 ;
	public ActionPoints(int number) {
		super(number);
	}

	public ActionPoints(int ActionInitialValue, int ActionBaseProduction) {
		super(ActionInitialValue, ActionBaseProduction);
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_ACTIONS;
	}
	
	int variation () {
		return 0;
	}

}

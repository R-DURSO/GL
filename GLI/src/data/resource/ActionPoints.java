package data.resource;

import java.io.Serializable;

/**
 * <p>ActionPoints represents the number of action a player can do each turn.</p>
 * <p>Useable Action can be seen in "data.actions"</p>
 * @author Maxence HENNEKEIN
 */

public class ActionPoints extends Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6279039584293741802L;
	public static final int BASE_MAX_ACTIONS = 6 ;
	private int MaxActions = BASE_MAX_ACTIONS;
	
	public ActionPoints(int number) {
		super(number);
		this.MaxActions = BASE_MAX_ACTIONS;
	}

	public ActionPoints(int ActionInitialValue, int ActionBaseProduction) {
		super(ActionInitialValue, ActionBaseProduction);
	}
	
	public int getResourceType() {
		return ResourceTypes.RESOURCE_ACTIONS;
	}
	
	public int getMaxActions() {
		return MaxActions;
	}
	
	public void addMaxActions(int value) {
		this.MaxActions += value;
	}
	
	int variation () {
		return 0;
	}

}

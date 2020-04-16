package data;

import java.io.Serializable;

/**
 * Constants of initial values for creating resources
 * @author Aldric Vitali Silvestre
 * @see process.management.ResourcesFactory
 *
 */
public class InitialValues implements Serializable {
	public static final int NUMBER_INITIAL_VALUE = 500 ;
	public static final int ACTION_INITIAL_VALUE = 2;
	public static final int WOOD_INITIAL_VALUE = 150;
	public static final int GOLD_INITIAL_VALUE = 100;
	public static final int FOOD_INITIAL_VALUE = 200;
	public static final int STONE_INITIAL_VALUE = 50;
	public static final int SCORE_INITIAL_VALUE = 0;

	public static final int ACTION_BASE_PRODUCTION = 2;
	public static final int FOOD_BASE_PRODUCTION = 20;
	public static final int GOLD_BASE_PRODUCTION = 20;
	public static final int WOOD_BASE_PRODUCTION = 20;
	public static final int STONE_BASE_PRODUCTION = 20;
	
	private InitialValues() {}
}

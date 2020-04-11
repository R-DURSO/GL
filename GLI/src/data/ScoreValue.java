package data;

import java.io.Serializable;

public class ScoreValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 606771431858962754L;
	// score for building 
	public static final int SCORE_VALUE_BUILDING_PRODUCT = 50 ;
	public static final int SCORE_VALUE_BUILDING_ARMY = 50 ;
	public static final int SCORE_VALUE_BUILDING_SPECIAL = 50 ;
	public static final int SCORE_VALUE_BUILDING_TEMPLE = 50 ;
	public static final int SCORE_VALUE_UPGRADE_CAPITALE = 50;
	
	//score for unit 
	public static final int SCORE_VALUE_UNITS_INFANTRY = 1 ;
	public static final int SCORE_VALUE_UNITS_ARCHER = 3 ;
	public static final int SCORE_VALUE_UNITS_CAVALRY = 5 ;
	public static final int SCORE_VALUE_UNITS_PIKEMAN = 10 ;
	public static final int SCORE_VALUE_UNITS_BATTERING_RAM = 25 ;
	public static final int SCORE_VALUE_UNITS_TREBUCHET = 30 ;
	public static final int SCORE_VALUE_UNITS_BOAT= 30 ;

	// score from ressource of player 
	public static final int SCORE_VALUE_RESOURCE = 50 ;
	public static final int SCORE_VALUE_POWER = 500 ;
	
	//default score if unknown type
	public static final int SCORE_VALUE_DEFAULT = 10;
	
}

package process.game;
import java.util.ArrayList;

import data.actions.*;
import data.GameMap;
import data.Power;
import data.resource.*;
import process.management.ActionManager;
public class GameLoop {
	private ArrayList<Action> action[]= new ArrayList[8] ; 
	private ActionManager useAction;
	// constante temporaire 

	
	
	public   GameLoop( Power[] powers,  GameMap  map ) {
		InitTurn();
		useAction = new ActionManager(map);
		
		
		
		
		for( int i=0 ; i<powers.length ;  i++) {

			while(wouldturn((ActionPoints) powers[i].getResource(4))) {
				// test des action possible 

				useAction.createActionMakeAlliance(powers[i], powers[i+1]);
				if(useAction != null) {
					System.out.println(useAction);
					powers[i].removeActionPoint();
				}else {
					System.out.println("test");
				}
			}
		}
	}
	
	public void InitTurn() {
		for (int i = 0; i < ActionTypes.NUMBER_ACTIONS; i++) {
			action[i]= new ArrayList<Action>();
		}

	}
	public Boolean  wouldturn( ActionPoints action) {
		if( action.GetAction_Number() >0 ) { // on rajoute plus tard l'potion du bouton fin de tour 
			return true;
		}else {
			return false;
		}
	}
	
}

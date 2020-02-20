package process.game;
import java.util.ArrayList;

import data.actions.*;
import data.GameMap;
import data.Power;
import data.resource.*;
public class GameLoop {
	private ArrayList<Action> action[]= new ArrayList[8] ; 

	// constante temporaire 
	private ActionPoints nbaction = new ActionPoints();
	private Power power1 = new Power("player1");
	private Power power2 = new Power("player2");
	
	
	public   GameLoop( Power[] powers,  GameMap  map ) {
		InitTurn();
		for( int i=0 ; i<powers.length ;  i++) {

			while(wouldturn((ActionPoints) powers[i].getResource(4))) {
				// test des action possible 

				
			}
		}
	}
	
	public void InitTurn() {
		for (int i=0; i<8; i++) {
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
